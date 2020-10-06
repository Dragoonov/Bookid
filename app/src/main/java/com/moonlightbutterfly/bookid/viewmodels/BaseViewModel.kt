package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonlightbutterfly.bookid.Communicator
import com.moonlightbutterfly.bookid.Manager
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import com.moonlightbutterfly.bookid.utils.DefaultShelf
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

open class BaseViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val internalRepository: InternalRepository,
    private val userManager: Manager,
    private val communicator: Communicator
) : ViewModel() {

    val favoriteShelfLiveData: LiveData<Shelf?> = LiveDataReactiveStreams.fromPublisher(
        internalRepository.getShelfByBaseId(DefaultShelf.FAVORITES.id, userManager.user.value!!.id)
    )

    val savedShelfLiveData: LiveData<Shelf?> = LiveDataReactiveStreams.fromPublisher(
        internalRepository.getShelfByBaseId(DefaultShelf.SAVED.id, userManager.user.value!!.id)
    )

    lateinit var bookAddedToFavouritesMessage: String
    lateinit var bookAddedToSavedMessage: String
    lateinit var bookRemovedFromSavedMessage: String
    lateinit var bookRemovedFromFavouritesMessage: String
    lateinit var bookAddedToDefaultsMessage: String

    protected val disposable: CompositeDisposable = CompositeDisposable()

    protected fun insertBookToShelf(book: Book?, shelf: Shelf?, message: String? = null, idx: Int? = null) {
        if (shelf != null && book != null && !shelf.books.contains(book)) {
            shelf.books = shelf.books.toMutableList().apply {
                if (idx != null) {
                    add(idx, book)
                } else {
                    add(book)
                }
            }
            disposable.add(
                internalRepository
                    .updateShelf(shelf)
                    .subscribe { message?.let { communicator.postMessage(it) } }
            )
        }
    }

    protected fun deleteBookFromShelf(book: Book?, shelf: Shelf?, message: String? = null) {
        if (book != null && shelf != null) {
            shelf.books = shelf.books.filter { it.id != book.id }
            internalRepository.updateShelf(shelf)
            message?.let { communicator.postMessage(it) }
        }
    }

    fun isBookInSaved(book: Book) = savedShelfLiveData.value?.books?.find { it.id == book.id } != null

    fun isBookInFavorites(book: Book) = favoriteShelfLiveData.value?.books?.find { it.id == book.id } != null

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

    private fun insertBookToFavorites(book: Book) = viewModelScope.launch(dispatcher) {
        insertBookToShelf(book, favoriteShelfLiveData.value, bookAddedToFavouritesMessage)
    }

    private fun deleteBookFromFavorites(book: Book) = viewModelScope.launch(dispatcher) {
        deleteBookFromShelf(book, favoriteShelfLiveData.value, bookRemovedFromFavouritesMessage)
    }

    private fun insertBookToSaved(book: Book) = viewModelScope.launch(dispatcher) {
        insertBookToShelf(book, savedShelfLiveData.value, bookAddedToSavedMessage)
    }

    private fun deleteBookFromSaved(book: Book) = viewModelScope.launch(dispatcher) {
        deleteBookFromShelf(book, savedShelfLiveData.value, bookRemovedFromSavedMessage)
    }

    fun insertBookToBaseShelf(book: Book?) = viewModelScope.launch(dispatcher) {
        internalRepository.getShelfById(userManager.user.value!!.baseShelfId, userManager.user.value!!.id)?.collect {
            insertBookToShelf(book, it, bookAddedToDefaultsMessage)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}