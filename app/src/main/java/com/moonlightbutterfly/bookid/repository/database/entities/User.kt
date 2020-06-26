package com.moonlightbutterfly.bookid.repository.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (
    @PrimaryKey val id: String,
    @ColumnInfo(name = "nick") val nick: String?,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "avatar") val avatar: String?
)