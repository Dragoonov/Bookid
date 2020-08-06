package com.moonlightbutterfly.bookid.repository.externalrepos.googlebooks.dtos

data class VolumeDto (
    var kind: String?,
    var id: String?,
    var etag: String?,
    var selfLink: String?,
    var volumeInfo: VolumeInfoDto?,
    var userInfo: UserInfoDto?,
    var saleInfo: SaleInfoDto?,
    var accessInfo: AccessInfoDto?,
    var searchInfo: SearchInfoDto?
)