package com.moonlightbutterfly.bookid.repository.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "nick") val nick: String?,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "avatar") val avatar: String?,
    @ColumnInfo(name = "shelfes") val shelfes: List<Shelf>?
)