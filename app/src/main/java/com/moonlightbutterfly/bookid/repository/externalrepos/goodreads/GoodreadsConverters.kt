package com.moonlightbutterfly.bookid.repository.externalrepos.goodreads

import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.externalrepos.goodreads.dtos.GoodreadsResponseDto

object GoodreadsConverters {

    fun extractBookListFromDto(response: GoodreadsResponseDto?): List<Book>? {
        val worksList = response?.search?.results?.works
        return worksList
            ?.map { workDto ->
                Book(
                    workDto.bestBook?.id.toString(),
                    workDto.bestBook?.title,
                    workDto.bestBook?.author?.id.toString(),
                    String.format(
                        "%d.%d.%d",
                        workDto.originalPublicationDay,
                        workDto.originalPublicationMonth,
                        workDto.originalPublicationYear
                    ),
                    workDto.averageRating,
                    workDto.bestBook?.imageUrl
                )
            }
            ?.toList()
    }

    fun extractAuthorBookListFromDto(response: GoodreadsResponseDto?): List<Book>? {
        val worksList = response?.author?.books?.books
        return worksList
            ?.map { book ->
                Book(
                    book.id.toString(),
                    book.title!!,
                    book.authors?.authors?.get(0)?.id.toString(),
                    String.format(
                        "%d.%d.%d",
                        book.publicationDay,
                        book.publicationMonth,
                        book.publicationYear
                    ),
                    book.averageRating,
                    book.imageUrl
                )
            }
            ?.toList()
    }
}