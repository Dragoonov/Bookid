package com.moonlightbutterfly.bookid.viewmodels

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moonlightbutterfly.bookid.repository.database.entities.Author
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.externalrepos.ExternalRepository
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import javax.inject.Inject

class BookViewModel @Inject constructor() : ViewModel() {

    lateinit var book: LiveData<Book>
    lateinit var similarBooks: LiveData<List<Book>>
    lateinit var authorsBooks: LiveData<List<Book>>
    lateinit var authorDetailed: LiveData<Author>
    val allDataLoaded: MutableLiveData<Boolean> = MutableLiveData(false)

    fun init(book: Book, repository: ExternalRepository) {
        this.book = MutableLiveData(book)
        similarBooks = repository.getSimilarBooks(book)
        authorsBooks = repository.getAuthorBooks(book.author)
        authorDetailed = repository.getAuthorInfo(book.author)
    }

    fun updateDataLoaded() {
        allDataLoaded.value = authorDetailed.value != null && authorsBooks.value != null
    }

    fun removeDisplayedBookFromList(): List<Book> = authorsBooks.value!!
        .toMutableList()
        .filter{it.id != book.value!!.id}


}