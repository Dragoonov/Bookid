package com.moonlightbutterfly.bookid.repository.externalrepos

import com.moonlightbutterfly.bookid.repository.database.entities.Book
import io.reactivex.Single

interface ExternalRepository {
    fun loadAuthorBooks(author: String?): Single<List<Book>>
    fun loadSimilarBooks(cathegory: String?): Single<List<Book>>
    fun loadSearchedBooks(query: String?, page: Int): Single<List<Book>>
}