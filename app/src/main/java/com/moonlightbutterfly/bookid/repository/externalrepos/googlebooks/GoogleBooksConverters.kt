package com.moonlightbutterfly.bookid.repository.externalrepos.googlebooks

import com.moonlightbutterfly.bookid.repository.database.entities.*
import com.moonlightbutterfly.bookid.repository.externalrepos.googlebooks.dtos.IndustryIdentifierDto
import com.moonlightbutterfly.bookid.repository.externalrepos.googlebooks.dtos.VolumeListDto

object GoogleBooksConverters {

    fun extractBookListFromDto(response: VolumeListDto?): List<Book> = response?.items?.map {
            Book(
                id = it.id!!,
                title = it.volumeInfo?.title,
                subtitle = it.volumeInfo?.subtitle,
                authors = it.volumeInfo?.authors,
                description = it.volumeInfo?.description,
                cathegories = it.volumeInfo?.categories,
                publicationInfo = PublicationInfo(
                    it.volumeInfo?.publishedDate,
                    it.volumeInfo?.publisher),
                pageCount = it.volumeInfo?.pageCount,
                isbn = resolveISBN(it.volumeInfo?.industryIdentifiers),
                rating = Rating(
                    it.volumeInfo?.averageRating,
                    it.volumeInfo?.ratingsCount),
                accessInfo = AccessInfo(
                    it.volumeInfo?.infoLink?.convertToHTTPS(),
                    it.volumeInfo?.previewLink?.convertToHTTPS(),
                    it.saleInfo?.retailPrice?.amount,
                    it.saleInfo?.retailPrice?.currencyCode,
                    it.saleInfo?.buyLink?.convertToHTTPS(),
                    it.accessInfo?.pdf?.isAvailable,
                    it.accessInfo?.epub?.isAvailable,
                    it.saleInfo?.isEbook
                ),
                imageUrl = it.volumeInfo?.imageLinks?.thumbnail?.convertToHTTPS()
            )
        } ?: ArrayList()

    private fun resolveISBN(identifiers: List<IndustryIdentifierDto>?): ISBN {
        var isbn13: String? = null
        var isbn10: String? = null
        if (identifiers != null) {
            if (identifiers.size > 1) {
                val first = identifiers[0]
                if (first.type!!.contains("13")) {
                    isbn13 = first.identifier
                    isbn10 = identifiers[1].identifier
                } else {
                    isbn10 = first.identifier
                    isbn13 = identifiers[1].identifier
                }
            } else {
                val first = identifiers[0]
                if (first.type!!.contains("13")) {
                    isbn13 = first.identifier
                    isbn10 = null
                } else {
                    isbn10 = first.identifier
                    isbn13 = null
                }
            }
        }
        return ISBN(isbn10, isbn13)
    }

    private fun String.convertToHTTPS(): String =
        if(!startsWith("https")) {
            replaceRange(0,4,"https")
        } else {
            this
        }
}