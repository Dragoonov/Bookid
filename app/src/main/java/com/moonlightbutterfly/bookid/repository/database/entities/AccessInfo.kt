package com.moonlightbutterfly.bookid.repository.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class AccessInfo (
    @ColumnInfo(name = "infoLink") val infoLink: String? = null,
    @ColumnInfo(name = "previewLink") val previewLink: String? = null,
    @ColumnInfo(name = "price") val price: Double? = null,
    @ColumnInfo(name = "priceCurrency") val priceCurrency: String? = null,
    @ColumnInfo(name = "purchaseLink") val purchaseLink: String? = null,
    @ColumnInfo(name = "pdf") val pdf: Boolean? = null,
    @ColumnInfo(name = "epub") val epub: Boolean? = null,
    @ColumnInfo(name = "isEbook") val isEbook: Boolean? = null
)