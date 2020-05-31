package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.externalrepos.ExternalRepository
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository

class BookViewModel(book: Book, val repository: ExternalRepository) : ViewModel() {

    var book: LiveData<Book> = MutableLiveData(book)
    val similarBooks: LiveData<List<Book>> = repository.getSimilarBooks(book)
    val authorsBooks: LiveData<List<Book>> = repository.getAuthorBooks(book.author)

}