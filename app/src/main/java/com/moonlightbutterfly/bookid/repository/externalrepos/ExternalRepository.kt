package com.moonlightbutterfly.bookid.repository.externalrepos

import androidx.lifecycle.LiveData
import com.moonlightbutterfly.bookid.repository.database.entities.Author
import com.moonlightbutterfly.bookid.repository.database.entities.Book

interface ExternalRepository {
    fun loadAuthorBooks(author: Author): LiveData<List<Book>>
    fun loadSearchedBooks(query: String?, page: Int): LiveData<List<Book>>
    fun loadAuthorInfo(author:Author): LiveData<Author>

    val searchedBooksLiveData: LiveData<List<Book>>
    val authorBooksLiveData: LiveData<List<Book>>
    val authorInfoLiveData: LiveData<Author>
}