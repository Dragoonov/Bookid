package com.moonlightbutterfly.bookid.repository.database.entities

import androidx.room.ColumnInfo

data class PublicationInfo (
    @ColumnInfo(name = "publicationDate") val publicationDate: String? = null,
    @ColumnInfo(name = "publisher") val publisher: String? = null
)