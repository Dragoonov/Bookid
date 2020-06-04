package com.moonlightbutterfly.bookid.repository.externalrepos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.moonlightbutterfly.bookid.repository.database.entities.Author
import com.moonlightbutterfly.bookid.repository.database.entities.Book

interface ExternalRepository {
    fun getSimilarBooks(book: Book): LiveData<List<Book>>
    fun getAuthorBooks(author: Author): LiveData<List<Book>>
    fun getSearchedBooks(query: String, page: Int): LiveData<List<Book>>
    fun getAuthorInfo(author:Author): LiveData<Author>
}