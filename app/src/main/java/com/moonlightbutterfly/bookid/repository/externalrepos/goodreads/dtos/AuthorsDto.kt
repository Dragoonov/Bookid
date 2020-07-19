package com.moonlightbutterfly.bookid.repository.externalrepos.goodreads.dtos

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "authors", strict = false)
data class AuthorsDto @JvmOverloads constructor(
    @field:ElementList(inline = true) var authors: List<AuthorDto>? = null
)