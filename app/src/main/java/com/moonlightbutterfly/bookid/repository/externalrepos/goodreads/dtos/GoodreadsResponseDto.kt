package com.moonlightbutterfly.bookid.repository.externalrepos.goodreads.dtos

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


@Root(name = "GoodreadsResponse", strict = false)
data class GoodreadsResponseDto @JvmOverloads constructor(
    @field:Element(name = "search", required = false) var search: SearchDto? = null,
    @field:Element(name = "author", required = false) var author: AuthorDto? = null)