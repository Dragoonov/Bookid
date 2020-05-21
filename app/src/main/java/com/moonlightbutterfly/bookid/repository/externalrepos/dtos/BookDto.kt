package com.moonlightbutterfly.bookid.repository.externalrepos.dtos

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "best_book", strict = false)
data class BookDto @JvmOverloads constructor(
    @field:Element(name = "id") var id: Int? = null,
    @field:Element(name = "title") var title: String? = null,
    @field:Element(name = "author") var author: AuthorDto? = null,
    @field:Element(name = "image_url", required = false) var imageUrl: String? = null,
    @field:Element(name = "small_image_url", required = false) var smallImageUrl: String? = null
)