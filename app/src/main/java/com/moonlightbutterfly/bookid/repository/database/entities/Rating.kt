package com.moonlightbutterfly.bookid.repository.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class Rating (
    @ColumnInfo(name = "ratingValue") val ratingValue: Float? = null,
    @ColumnInfo(name = "ratingsCount") val ratingsCount: Int? = null
)