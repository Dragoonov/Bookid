package com.moonlightbutterfly.bookid.repository.externalrepos.googlebooks.dtos

data class VolumeListDto (
    var kind: String?,
    var items: List<VolumeDto>?,
    var totalItems: Int?
)