package com.moonlightbutterfly.bookid.repository.externalrepos

import com.moonlightbutterfly.bookid.repository.database.entities.Book

interface ExternalRepository {
    suspend fun loadAuthorBooks(author: String?): List<Book>
    suspend fun loadSearchedBooks(query: String?, page: Int): List<Book>
}