package com.moonlightbutterfly.bookid.repository.externalrepos.goodreads

import com.moonlightbutterfly.bookid.repository.database.entities.Author
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.externalrepos.ExternalRepository

class GoodreadRepositoryFake : ExternalRepository {

    override suspend fun loadAuthorBooks(author: Author): List<Book> = listOf(
        Book(1, "testAuthorTitle1", author, "01.01.1970", 4.5, null),
        Book(1, "testAuthorTitle2", author, "02.01.1970", 4.5, null),
        Book(1, "testAuthorTitle3", author, "03.01.1970", 4.5, null),
        Book(1, "testAuthorTitle4", author, "04.01.1970", 4.5, null)
        )

    override suspend fun loadSearchedBooks(query: String?, page: Int): List<Book> = listOf(
        Book(1, "testSearchedTitle1", null, "01.01.1970", 4.5, null),
        Book(1, "testSearchedTitle2", null, "02.01.1970", 4.5, null),
        Book(1, "testSearchedTitle3", null, "03.01.1970", 4.5, null),
        Book(1, "testSearchedTitle4", null, "04.01.1970", 4.5, null)
    )

    override suspend fun loadAuthorInfo(author: Author): Author = author
}