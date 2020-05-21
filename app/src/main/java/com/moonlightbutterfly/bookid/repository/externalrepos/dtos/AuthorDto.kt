package com.moonlightbutterfly.bookid.repository.externalrepos.dtos

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "author", strict = false)
data class AuthorDto @JvmOverloads constructor(
    @field:Element(name = "id") var id: Int? = null,
    @field:Element(name = "name") var name: String? = null
)
