package com.moonlightbutterfly.bookid.repository.externalrepos

import androidx.lifecycle.LiveData
import com.moonlightbutterfly.bookid.repository.database.entities.Author
import com.moonlightbutterfly.bookid.repository.database.entities.Book

interface ExternalRepository {
    suspend fun loadAuthorBooks(author: Author): List<Book>
    suspend fun loadSearchedBooks(query: String?, page: Int): List<Book>
    suspend fun loadAuthorInfo(author:Author): Author
}