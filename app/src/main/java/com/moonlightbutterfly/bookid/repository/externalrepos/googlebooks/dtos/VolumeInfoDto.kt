package com.moonlightbutterfly.bookid.repository.externalrepos.googlebooks.dtos

data class VolumeInfoDto (
    var title: String?,
    var subtitle: String?,
    var authors: List<String>?,
    var publisher: String?,
    var publishedDate: String?,
    var description: String?,
    var industryIdentifiers: List<IndustryIdentifierDto>?,
    var pageCount: Int?,
    var dimensions: DimensionsDto,
    var printType: String?,
    var mainCategory: String?,
    var categories: List<String>,
    var averageRating: Double?,
    var ratingsCount: Int?,
    var contentVersion: String?,
    var imageLinks: ImageLinksDto,
    var language: String?,
    var previewLink: String?,
    var infoLink: String?,
    var canonicalVolumeLink: String?
    )