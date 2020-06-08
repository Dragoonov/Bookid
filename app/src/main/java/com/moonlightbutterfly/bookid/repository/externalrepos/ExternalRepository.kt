package com.moonlightbutterfly.bookid.repository.externalrepos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.moonlightbutterfly.bookid.repository.database.entities.Author
import com.moonlightbutterfly.bookid.repository.database.entities.Book

interface ExternalRepository {
    fun loadSimilarBooks(book: Book): LiveData<List<Book>>
    fun loadAuthorBooks(author: Author): LiveData<List<Book>>
    fun loadSearchedBooks(query: String, page: Int): LiveData<List<Book>>
    fun loadAuthorInfo(author:Author): LiveData<Author>

    val similarBooksLiveData: LiveData<List<Book>>
    val searchedBooksLiveData: LiveData<List<Book>>
    val authorBooksLiveData: LiveData<List<Book>>
    val authorInfoLiveData: LiveData<Author>
}