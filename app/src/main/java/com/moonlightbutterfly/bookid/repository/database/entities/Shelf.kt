package com.moonlightbutterfly.bookid.repository.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shelfs")
data class Shelf(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "books") var books: List<Book>,
    @ColumnInfo(name = "userId") val userId: String,
    @ColumnInfo(name = "cover") var cover: Cover
)