package com.moonlightbutterfly.bookid.repository.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book (
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "author") val author: Author?,
    @ColumnInfo(name = "publicationDate") val publicationDate: String?,
    @ColumnInfo(name = "rating") val rating: Double?,
    @ColumnInfo(name = "imageUrl") val imageUrl: String?)