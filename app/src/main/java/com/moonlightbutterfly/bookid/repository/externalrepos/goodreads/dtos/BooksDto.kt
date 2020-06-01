package com.moonlightbutterfly.bookid.repository.externalrepos.goodreads.dtos

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "books", strict = false)
data class BooksDto @JvmOverloads constructor(
    @field:ElementList(inline = true) var books: List<AuthorBookDto>? = null)