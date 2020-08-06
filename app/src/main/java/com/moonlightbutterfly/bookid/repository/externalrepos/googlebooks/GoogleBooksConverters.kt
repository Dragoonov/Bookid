package com.moonlightbutterfly.bookid.repository.externalrepos.googlebooks

import com.moonlightbutterfly.bookid.repository.database.entities.Author
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.externalrepos.googlebooks.dtos.VolumeListDto

object GoogleBooksConverters {

    fun extractBookListFromDto(response: VolumeListDto?): List<Book> = response?.items?.map {
            Book(
                id = it.id!!,
                title = it.volumeInfo?.title,
                author = it.volumeInfo?.authors?.get(0),
                publicationDate = it.volumeInfo?.publishedDate,
                rating = it.volumeInfo?.averageRating,
                imageUrl = it.volumeInfo?.imageLinks?.thumbnail?.convertToHTTPS()
            )
        } ?: ArrayList()

    private fun String.convertToHTTPS(): String =
        if(!startsWith("https")) {
            replaceRange(0,4,"https")
        } else {
            this
        }
}