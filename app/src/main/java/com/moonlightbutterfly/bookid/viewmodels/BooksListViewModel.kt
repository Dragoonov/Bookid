package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.*
import com.moonlightbutterfly.bookid.Manager
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import com.moonlightbutterfly.bookid.utils.BasicShelfsId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class BooksListViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val internalRepository: InternalRepository
) : ViewModel() {

    private val observer: Observer<Any> = Observer { }

    private val favoriteShelfLiveData: LiveData<Shelf> = liveData {
        internalRepository.getShelfById(BasicShelfsId.FAVORITES.id)?.collect {
            emit(it)
        }
    }

    private val savedShelfLiveData: LiveData<Shelf> = liveData {
        internalRepository.getShelfById(BasicShelfsId.SAVED.id)?.collect {
            emit(it)
        }
    }

    private val customShelfIdLiveData = MutableLiveData(1000)
    val customShelfLiveData: LiveData<List<Book>> = Transformations.switchMap(customShelfIdLiveData) {
        liveData {
            internalRepository.getShelfById(it)?.collect {
                emit(it.books)
            }
        }
    }

    init {
        favoriteShelfLiveData.observeForever(observer)
        savedShelfLiveData.observeForever(observer)
    }

    fun setCustomShelfId(id: Int) = run { customShelfIdLiveData.value = id }

    fun handleSavedOperation(book: Book) = if (isBookInSaved(book)) {
        deleteBookFromSaved(book)
    } else {
        insertBookToSaved(book)
    }

    fun handleFavoriteOperation(book: Book) = if (isBookInFavorites(book)) {
        deleteBookFromFavorites(book)
    } else {
        insertBookToFavorites(book)
    }

    fun isBookInSaved(book: Book) = savedShelfLiveData.value?.books?.find { it.id == book.id } != null

    fun isBookInFavorites(book: Book) = favoriteShelfLiveData.value?.books?.find { it.id == book.id } != null

    private fun insertBookToFavorites(book: Book) = viewModelScope.launch(dispatcher) {
        insertBookToShelf(book, favoriteShelfLiveData.value)
    }

    private fun deleteBookFromFavorites(book: Book) = viewModelScope.launch(dispatcher) {
        deleteBookFromShelf(book, favoriteShelfLiveData.value)
    }

    private fun insertBookToSaved(book: Book) = viewModelScope.launch(dispatcher) {
        insertBookToShelf(book, savedShelfLiveData.value)
    }

    private fun deleteBookFromSaved(book: Book) = viewModelScope.launch(dispatcher) {
        deleteBookFromShelf(book, savedShelfLiveData.value)
    }

    private suspend fun insertBookToShelf(book: Book?, shelf: Shelf?) {
        if (shelf != null && book != null && !shelf.books.contains(book)) {
            shelf.books = shelf.books.toMutableList().apply {
                add(book)
            }
            internalRepository.updateShelf(shelf)
        }
    }

    private suspend fun deleteBookFromShelf(book: Book?, shelf: Shelf?) {
        if (book != null && shelf != null) {
            shelf.books = shelf.books.filter { it.id != book.id }
            internalRepository.updateShelf(shelf)
        }
    }

    override fun onCleared() {
        favoriteShelfLiveData.removeObserver(observer)
        savedShelfLiveData.removeObserver(observer)
        super.onCleared()
    }

}