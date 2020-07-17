package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.*
import com.moonlightbutterfly.bookid.UserManager
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.database.entities.User
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.simpleframework.xml.transform.Transform
import javax.inject.Inject

class ShelfViewModel @Inject constructor(private val userManager: UserManager,
                                         private val repository: InternalRepository): ViewModel() {

    val shelfsLiveData: LiveData<List<Shelf>> = liveData {
        repository.getUserShelfs(userManager.user.value?.id!!).collect { data -> emit(data) }
    }

    fun deleteShelf(shelf: Shelf) = viewModelScope.launch {
        repository.deleteShelf(shelf)
    }

    fun deleteBookFromShelf(book: Book, shelf: Shelf) = viewModelScope.launch {
        shelf.books = shelf.books.filter { it.id != book.id }
        repository.updateShelf(shelf)
    }


    fun updateShelfName(shelf: Shelf, name: String) = viewModelScope.launch {
        shelf.name = name
        repository.updateShelf(shelf)
    }


    fun insertBookToShelf(shelf: Shelf, book: Book) = viewModelScope.launch {
        if(!shelf.books.contains(book)) {
            shelf.books = shelf.books.toMutableList().apply { add(book) }
            repository.updateShelf(shelf)
        }
    }

    fun insertShelf(name: String) = viewModelScope.launch {
        repository.insertShelf(Shelf(
            name,
            ArrayList(),
            userManager.user.value?.id!!
        ))
    }
}