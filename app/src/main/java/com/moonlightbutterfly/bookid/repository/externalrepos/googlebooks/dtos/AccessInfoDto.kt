package com.moonlightbutterfly.bookid.repository.externalrepos.googlebooks.dtos

data class AccessInfoDto(
    var country: String?,
    var viewability: String?,
    var embeddable: Boolean?,
    var publicDomain: Boolean?,
    var textToSpeechPermission: String?,
    var epub: FormatDto?,
    var pdf: FormatDto?,
    var webReaderLink: String?,
    var accessViewStatus: String?,
    var downloadAccess: DownloadAccessDto?
)