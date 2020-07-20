package com.moonlightbutterfly.bookid.repository.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title") val title: String? = null,
    @ColumnInfo(name = "author") val author: Author? = null,
    @ColumnInfo(name = "publicationDate") val publicationDate: String? = null,
    @ColumnInfo(name = "rating") val rating: Double? = null,
    @ColumnInfo(name = "imageUrl") val imageUrl: String? = null
)