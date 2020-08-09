package com.moonlightbutterfly.bookid.repository.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "authors")
@Deprecated("Left only for GoodreadsRepository")
data class Author(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo(name = "imageUrl") val imageUrl: String? = null
)
