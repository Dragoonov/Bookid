package com.moonlightbutterfly.bookid.repository.database.entities

import androidx.room.ColumnInfo
import com.moonlightbutterfly.bookid.utils.Logos

data class Cover (
    @ColumnInfo(name = "background") val background: Int,
    @ColumnInfo(name = "icon") var icon: Logos
)