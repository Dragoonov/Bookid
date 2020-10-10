package com.moonlightbutterfly.bookid.repository.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moonlightbutterfly.bookid.utils.Logos

@Entity(tableName = "shelfs")
data class Shelf(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "books") var books: List<Book> = listOf(),
    @ColumnInfo(name = "userId") val userId: String = "",
    @ColumnInfo(name = "cover") var cover: Cover = Cover(0, Logos.FAVOURITE),
    @ColumnInfo(name = "baseShelfId") var baseShelfId: Int? = null
)