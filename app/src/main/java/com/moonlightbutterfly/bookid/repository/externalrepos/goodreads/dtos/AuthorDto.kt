package com.moonlightbutterfly.bookid.repository.externalrepos.goodreads.dtos

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "author", strict = false)
data class AuthorDto @JvmOverloads constructor(
    @field:Element(name = "id") var id: Int? = null,
    @field:Element(name = "name") var name: String? = null,
    @field:Element(name = "image_url") var imageUrl: String? = null,
    @field:Element(name = "books", required = false) var books: BooksDto? = null
)
