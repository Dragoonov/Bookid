package com.moonlightbutterfly.bookid.repository.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "shelfs"
//    foreignKeys = [ForeignKey(
//        entity = User::class,
//        parentColumns = arrayOf("id"),
//        childColumns = arrayOf("userId")
//    )]
)
data class Shelf(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "books") var books: List<Book>,
    @ColumnInfo(name = "userId") val userId: Int
)