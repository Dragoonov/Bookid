package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.moonlightbutterfly.bookid.Manager
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShelfViewModel @Inject constructor(
    private val userManager: Manager,
    private val repository: InternalRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    val shelfsLiveData: LiveData<List<Shelf>> = liveData {
        repository.getUserShelfs(userManager.user.value?.id!!)
            .collect { data -> data?.let { emit(it) } }
    }

    fun deleteShelf(shelf: Shelf?) = shelf?.let {
        viewModelScope.launch(dispatcher) {
            repository.deleteShelf(it)
        }
    }

    fun deleteBookFromShelf(book: Book?, shelf: Shelf?) {
        if (book != null && shelf != null) {
            viewModelScope.launch(dispatcher) {
                shelf.books = shelf.books.filter { it.id != book.id }
                repository.updateShelf(shelf)
            }
        }
    }


    fun updateShelfName(shelf: Shelf?, name: String?) {
        if (shelf != null && name != null) {
            viewModelScope.launch(dispatcher) {
                shelf.name = name
                repository.updateShelf(shelf)
            }
        }
    }


    fun insertBookToShelf(shelf: Shelf?, book: Book?) {
        if (shelf != null && book != null && !shelf.books.contains(book)) {
            viewModelScope.launch(dispatcher) {
                shelf.books = shelf.books.toMutableList().apply { add(book) }
                repository.updateShelf(shelf)
            }
        }
    }

    fun insertShelf(name: String?) = name?.let {
        viewModelScope.launch(dispatcher) {
            repository.insertShelf(
                Shelf(
                    it,
                    ArrayList(),
                    userManager.user.value?.id!!
                )
            )
        }
    }
}