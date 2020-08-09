package com.moonlightbutterfly.bookid.repository.database.entities

import androidx.room.ColumnInfo

data class ISBN (
    @ColumnInfo(name = "isbn10") val isbn10: String? = null,
    @ColumnInfo(name = "isbn13") val isbn13: String? = null
)