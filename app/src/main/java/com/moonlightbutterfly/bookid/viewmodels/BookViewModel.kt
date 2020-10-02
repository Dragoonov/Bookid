package com.moonlightbutterfly.bookid.viewmodels

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.lifecycle.*
import com.moonlightbutterfly.bookid.Communicator
import com.moonlightbutterfly.bookid.Manager
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.externalrepos.ExternalRepository
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import com.moonlightbutterfly.bookid.utils.DefaultShelf
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

    private val favoriteShelfLiveData: LiveData<Shelf?> = liveData {
        internalRepository.getShelfById(DefaultShelf.FAVORITES.id)?.collect {
            emit(it)
        }
    }
    private val _isBookInFavoritesLiveData = Transformations.map(favoriteShelfLiveData) { shelf ->
        shelf?.books?.find { bookLiveData.value?.id == it.id } != null
    }

    val isBookInFavoritesLiveData: LiveData<Boolean> get() = _isBookInFavoritesLiveData

    private val savedShelfLiveData: LiveData<Shelf?> = liveData {
        internalRepository.getShelfById(DefaultShelf.SAVED.id)?.collect {
            emit(it)
        }
    }
    private val _isBookInSavedLiveData = Transformations.map(savedShelfLiveData) { shelf ->
        shelf?.books?.find { bookLiveData.value?.id == it.id } != null
    }

    val isBookInSavedLiveData: LiveData<Boolean> get() = _isBookInSavedLiveData

    private var _bookLiveData = MutableLiveData<Book>()
    val bookLiveData: LiveData<Book> get() = _bookLiveData

    val descriptionExpandedMode: LiveData<Boolean> get() = _descriptionExpandedMode
    private var _descriptionExpandedMode = MutableLiveData<Boolean>(false)

    lateinit var bookAddedToDefaultsMessage: String
    lateinit var bookAddedToFavouritesMessage: String
    lateinit var bookAddedToSavedMessage: String
    lateinit var bookRemovedFromSavedMessage: String
    lateinit var bookRemovedFromFavouritesMessage: String


    fun setBook(book: Book) {
        if (_bookLiveData.value?.id == book.id) {
            return
        }
        this._bookLiveData.value = book
        refreshData()
        insertBookToRecentlyViewed()
    }

    fun changeExpanded() {
        _descriptionExpandedMode.value = !_descriptionExpandedMode.value!!
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

    fun openPurchaseLink(view:View) = openLink(view, bookLiveData.value?.accessInfo?.purchaseLink)

    fun openPreviewLink(view: View) = openLink(view, bookLiveData.value?.accessInfo?.previewLink)

    fun openInfoLink(view: View) = openLink(view, bookLiveData.value?.accessInfo?.infoLink)


    private fun openLink(view: View, url: String?) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(view.context.packageManager) != null) {
            view.context.startActivity(intent)
        }
    }


    fun handleFavoriteOperation() = if (isBookInFavoritesLiveData.value!!) {
        deleteBookFromFavorites()
    } else {
        insertBookToFavorites()
    }

    fun handleSavedOperation() = if (isBookInSavedLiveData.value!!) {
        deleteBookFromSaved()
    } else {
        insertBookToSaved()
    }

    private fun insertBookToFavorites() = viewModelScope.launch(dispatcher) {
        insertBookToShelf(bookLiveData.value, favoriteShelfLiveData.value, null, bookAddedToFavouritesMessage)
    }

    private fun deleteBookFromFavorites() = viewModelScope.launch(dispatcher) {
        deleteBookFromShelf(bookLiveData.value, favoriteShelfLiveData.value, bookRemovedFromFavouritesMessage)
    }

    private fun insertBookToSaved() = viewModelScope.launch(dispatcher) {
        insertBookToShelf(bookLiveData.value, savedShelfLiveData.value, null, bookAddedToSavedMessage)
    }

    private fun deleteBookFromSaved() = viewModelScope.launch(dispatcher) {
        deleteBookFromShelf(bookLiveData.value, savedShelfLiveData.value, bookRemovedFromSavedMessage)
    }

    private fun insertBookToRecentlyViewed() = viewModelScope.launch(dispatcher) {
        internalRepository.getShelfById(DefaultShelf.RECENTLY_VIEWED.id)?.collect {
            when {
                insertedToRecentlyViewed -> {
                }
                it!!.books.isEmpty() -> {
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

    fun insertBookToBaseShelf() = viewModelScope.launch(dispatcher) {
        internalRepository.getShelfById(userManager.user.value?.baseShelfId!!)?.collect {
            insertBookToShelf(bookLiveData.value, it, null, bookAddedToDefaultsMessage)
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