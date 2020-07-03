package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moonlightbutterfly.bookid.UserManager
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import javax.inject.Inject

class ShelfViewModel @Inject constructor(private val userManager: UserManager,
                                         private val repository: InternalRepository): ViewModel() {

    val shelfsLiveData: LiveData<List<Shelf>> = repository.getUserShelfs(userManager.loggedUser.value?.id!!)

    fun deleteShelf(shelf: Shelf) = repository.deleteShelf(shelf)

    fun deleteBookFromShelf(book: Book, shelf: Shelf) {
        shelf.books = shelf.books.filter { it.id != book.id }
        repository.updateShelf(shelf)
    }

    fun updateShelfName(shelf: Shelf, name: String) {
        shelf.name = name
        repository.updateShelf(shelf)
    }

    fun insertBookToShelf(shelf: Shelf, book: Book) {
        if(!shelf.books.contains(book)) {
            shelf.books = shelf.books.toMutableList().apply { add(book) }
            repository.updateShelf(shelf)
        }
    }

    fun insertShelf(name: String) {
        repository.insertShelf(Shelf(
            name,
            ArrayList(),
            userManager.loggedUser.value?.id!!
        ))
    }
}