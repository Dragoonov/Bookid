package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.*
import com.moonlightbutterfly.bookid.Communicator
import com.moonlightbutterfly.bookid.Manager
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.externalrepos.ExternalRepository
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import com.moonlightbutterfly.bookid.utils.BasicShelfsId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class BookViewModel @Inject constructor(
    private val repository: ExternalRepository,
    private val dispatcher: CoroutineDispatcher,
    private val internalRepository: InternalRepository,
    private val userManager: Manager,
    private val communicator: Communicator
) : ViewModel() {

    private var insertedToRecentlyViewed = false

    private val _authorBooksLiveData = MutableLiveData<List<Book>>()
    val authorBooksLiveData: LiveData<List<Book>?> get() = _authorBooksLiveData

    private val _similarBooksLiveData = MutableLiveData<List<Book>>()
    val similarBooksLiveData: LiveData<List<Book>?> get() = _similarBooksLiveData

    private val favoriteShelfLiveData: LiveData<Shelf> = liveData {
        internalRepository.getShelfById(BasicShelfsId.FAVORITES.id)?.collect {
            emit(it)
        }
    }
    private val _isBookInFavoritesLiveData = Transformations.map(favoriteShelfLiveData) { shelf ->
        shelf.books.find { bookLiveData.value?.id == it.id } != null
    }

    val isBookInFavoritesLiveData: LiveData<Boolean> get() = _isBookInFavoritesLiveData

    private val savedShelfLiveData: LiveData<Shelf> = liveData {
        internalRepository.getShelfById(BasicShelfsId.SAVED.id)?.collect {
            emit(it)
        }
    }
    private val _isBookInSavedLiveData = Transformations.map(savedShelfLiveData) { shelf ->
        shelf.books.find { bookLiveData.value?.id == it.id } != null
    }

    val isBookInSavedLiveData: LiveData<Boolean> get() = _isBookInSavedLiveData

    private var _bookLiveData = MutableLiveData<Book>()
    val bookLiveData: LiveData<Book> get() = _bookLiveData

    private val _allDataLoadedLiveData = MediatorLiveData<Boolean>().apply {
        addSource(authorBooksLiveData) { value = isDataLoaded() }
        addSource(similarBooksLiveData) { value = isDataLoaded() }
        addSource(favoriteShelfLiveData) { value = isDataLoaded() }
    }
    val allDataLoadedLiveData: LiveData<Boolean> get() = _allDataLoadedLiveData


    private fun isDataLoaded() = (
            authorBooksLiveData.value != null
                    && similarBooksLiveData.value != null
                    && isBookInFavoritesLiveData.value != null
                    && favoriteShelfLiveData.value != null)

    fun setBook(book: Book) {
        if (_bookLiveData.value?.id == book.id) {
            return
        }
        this._bookLiveData.value = book
        refreshData()
        insertBookToRecentlyViewed()
    }

    fun refreshData() {
        clearCurrentData()
        viewModelScope.launch(dispatcher) {
            val authorBooks = repository
                .loadAuthorBooks(_bookLiveData.value?.authors?.get(0))
                .removeDisplayedBookFromList(_bookLiveData.value as Book)
            _authorBooksLiveData.value = authorBooks
            val similarBooks = repository
                .loadSimilarBooks(_bookLiveData.value?.cathegories?.get(0))
            _similarBooksLiveData.value = similarBooks
        }
    }

    fun handleFavoriteOperation(messageInsert: String? = null, messageDelete: String? = null) = if (isBookInFavoritesLiveData.value!!) {
        deleteBookFromFavorites(messageDelete)
    } else {
        insertBookToFavorites(messageInsert)
    }

    fun handleSavedOperation(messageInsert: String? = null, messageDelete: String? = null) = if (isBookInSavedLiveData.value!!) {
        deleteBookFromSaved(messageDelete)
    } else {
        insertBookToSaved(messageInsert)
    }

    private fun insertBookToFavorites(message: String? = null) = viewModelScope.launch(dispatcher) {
        insertBookToShelf(bookLiveData.value, favoriteShelfLiveData.value, null, message)
    }

    private fun deleteBookFromFavorites(message: String? = null) = viewModelScope.launch(dispatcher) {
        deleteBookFromShelf(bookLiveData.value, favoriteShelfLiveData.value, message)
    }

    private fun insertBookToSaved(message: String? = null) = viewModelScope.launch(dispatcher) {
        insertBookToShelf(bookLiveData.value, savedShelfLiveData.value, null, message)
    }

    private fun deleteBookFromSaved(message: String? = null) = viewModelScope.launch(dispatcher) {
        deleteBookFromShelf(bookLiveData.value, savedShelfLiveData.value, message)
    }

    private fun insertBookToRecentlyViewed() = viewModelScope.launch(dispatcher) {
        internalRepository.getShelfById(BasicShelfsId.RECENTLY_VIEWED.id)?.collect {
            when {
                insertedToRecentlyViewed -> { }
                it.books.isEmpty() -> {
                    insertBookToShelf(bookLiveData.value, it)
                }
                it.books.contains(bookLiveData.value) -> {
                    if (it.books[0] != bookLiveData.value) {
                        deleteBookFromShelf(bookLiveData.value, it)
                        insertBookToShelf(bookLiveData.value, it, 0)
                    }
                }
                it.books.size >= 20 -> {
                    deleteBookFromShelf(19, it)
                    insertBookToShelf(bookLiveData.value, it, 0)
                }
                else -> insertBookToShelf(bookLiveData.value, it, 0)
            }
            insertedToRecentlyViewed = true
        }
    }

    fun insertBookToBaseShelf(message: String? = null) = viewModelScope.launch(dispatcher) {
        internalRepository.getShelfById(userManager.user.value?.baseShelfId!!)?.collect {
            insertBookToShelf(bookLiveData.value, it,null, message)
        }
    }

    private suspend fun insertBookToShelf(book: Book?, shelf: Shelf?, idx: Int? = null, message: String? = null) {
        if (shelf != null && book != null && !shelf.books.contains(book)) {
            message?.let { communicator.postMessage(it) }
            shelf.books = shelf.books.toMutableList().apply {
                if (idx != null) {
                    add(idx, book)
                } else {
                    add(book)
                }
            }
            internalRepository.updateShelf(shelf)
        }
    }

    private suspend fun deleteBookFromShelf(book: Book?, shelf: Shelf?, message: String? = null) {
        if (book != null && shelf != null) {
            message?.let { communicator.postMessage(it) }
            shelf.books = shelf.books.filter { it.id != book.id }
            internalRepository.updateShelf(shelf)
        }
    }

    private suspend fun deleteBookFromShelf(idx: Int?, shelf: Shelf?, message: String? = null) {
        if (shelf != null && idx != null && idx < shelf.books.size) {
            deleteBookFromShelf(shelf.books[idx], shelf, message)
        }
    }

    private fun clearCurrentData() {
        _authorBooksLiveData.value = null
        _similarBooksLiveData.value = null
    }

    private fun List<Book>.removeDisplayedBookFromList(book: Book): List<Book>? = this
        .toMutableList()
        .filter { it.id != book.id }
}