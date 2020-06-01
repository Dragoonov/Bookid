package com.moonlightbutterfly.bookid.repository.externalrepos.goodreads.dtos

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "work", strict = false)
data class WorkDto @JvmOverloads constructor (
    @field:Element(name = "id") var id: Int? = null,
    @field:Element(name = "books_count", required = false)var booksCount: Int? = null,
    @field:Element(name = "ratings_count", required = false)var ratingsCount: Int? = null,
    @field:Element(name = "text_reviews_count", required = false)var textReviewsCount: Int? = null,
    @field:Element(name = "original_publication_year", required = false)var originalPublicationYear: Int? = null,
    @field:Element(name = "original_publication_month", required = false)var originalPublicationMonth: Int? = null,
    @field:Element(name = "original_publication_day", required = false)var originalPublicationDay: Int? = null,
    @field:Element(name = "average_rating", required = false)var averageRating: Double? = null,
    @field:Element(name = "best_book")var bestBook: BookDto? = null
)