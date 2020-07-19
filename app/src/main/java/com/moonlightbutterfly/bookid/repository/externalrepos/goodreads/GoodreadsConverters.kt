package com.moonlightbutterfly.bookid.repository.externalrepos.goodreads

import com.moonlightbutterfly.bookid.repository.database.entities.Author
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.externalrepos.goodreads.dtos.GoodreadsResponseDto

object GoodreadsConverters {

    fun extractBookListFromDto(response: GoodreadsResponseDto?): List<Book>? {
        val worksList = response?.search?.results?.works
        return worksList
            ?.map { workDto ->
                Book(
                    workDto.bestBook?.id!!,
                    workDto.bestBook?.title,
                    Author(
                        workDto.bestBook?.author?.id!!,
                        workDto.bestBook?.author?.name,
                        null
                    ),
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
                    book.id!!,
                    book.title!!,
                    Author(
                        book.authors?.authors?.get(0)?.id!!,
                        book.authors?.authors?.get(0)?.name,
                        null
                    ),
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

    fun extractAuthorFromDto(response: GoodreadsResponseDto?): Author? {
        val author = response?.author
        if (author == null) return author
        return Author(author?.id!!, author.name, author.imageUrl)
    }
}