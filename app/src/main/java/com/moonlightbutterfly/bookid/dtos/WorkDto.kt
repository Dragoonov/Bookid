package com.moonlightbutterfly.bookid.dtos

import com.moonlightbutterfly.bookid.repository.database.entities.Book

data class WorkDto (
    val id: Int,
    val bookCount: Int,
    val ratingsCount: Int,
    val textReviewsCount: Int,
    val originalPublicationYear: Int,
    val originalPublicationMonth: Int,
    val originalPublicationDay: Int,
    val averageRating: Int,
    val bestBook: Book
)