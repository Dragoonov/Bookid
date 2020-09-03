package com.moonlightbutterfly.bookid.repository.database.entities

import androidx.room.ColumnInfo

data class Cover (
    @ColumnInfo(name = "background") val background: Int,
    @ColumnInfo(name = "iconId") var iconId: Int
)