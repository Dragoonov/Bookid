package com.moonlightbutterfly.bookid.repository.externalrepos.googlebooks.dtos

data class DownloadAccessDto (
    var kind: String?,
    var volumeId: String?,
    var restricted: Boolean?,
    var deviceAllowed: Boolean?,
    var justAcquired: Boolean?,
    var maxDownloadDevices: Int?,
    var downloadsAcquired: Int?,
    var nonce: String?,
    var source: String?,
    var reasonCode: String?,
    var message: String?,
    var signature: String?
)