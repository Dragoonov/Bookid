package com.moonlightbutterfly.bookid.repository.externalrepos.goodreads.dtos

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "book", strict = false)
data class AuthorBookDto @JvmOverloads constructor(
    @field:Element(name = "id") var id: Int? = null,
    @field:Element(name = "title") var title: String? = null,
    @field:Element(name = "authors") var authors: AuthorsDto? = null,
    @field:Element(name = "publication_year", required = false)var publicationYear: Int? = null,
    @field:Element(name = "publication_month", required = false)var publicationMonth: Int? = null,
    @field:Element(name = "publication_day", required = false)var publicationDay: Int? = null,
    @field:Element(name = "average_rating", required = false)var averageRating: Double? = null,
    @field:Element(name = "image_url", required = false) var imageUrl: String? = null
)