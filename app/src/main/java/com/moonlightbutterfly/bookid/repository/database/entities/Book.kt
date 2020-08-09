package com.moonlightbutterfly.bookid.repository.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "title") val title: String? = null,
    @ColumnInfo(name = "subtitle") val subtitle: String? = null,
    @ColumnInfo(name = "authors") val authors: List<String>? = null,
    @ColumnInfo(name = "description") val description: String? = null,
    @ColumnInfo(name = "cathegories") val cathegories: List<String>? = null,
    @ColumnInfo(name = "publicationInfo") val publicationInfo: PublicationInfo? = null,
    @ColumnInfo(name = "pageCount") val pageCount: Int? = null,
    @ColumnInfo(name = "isbn") val isbn: ISBN? = null,
    @ColumnInfo(name = "rating") val rating: Rating? = null,
    @ColumnInfo(name = "accessInfo") val accessInfo: AccessInfo? = null,
    @ColumnInfo(name = "imageUrl") val imageUrl: String? = null
)