package com.moonlightbutterfly.bookid.repository.externalrepos.googlebooks

import com.moonlightbutterfly.bookid.repository.database.entities.*
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
                isbn = ISBN(
                    it.volumeInfo?.industryIdentifiers?.get(0)?.identifier,
                    if (it.volumeInfo?.industryIdentifiers?.size!! > 1) {
                        it.volumeInfo?.industryIdentifiers?.get(1)?.identifier
                    } else {
                        null
                    }
                ),
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

    private fun String.convertToHTTPS(): String =
        if(!startsWith("https")) {
            replaceRange(0,4,"https")
        } else {
            this
        }
}