package com.moonlightbutterfly.bookid.repository.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.moonlightbutterfly.bookid.repository.database.entities.Author
import com.moonlightbutterfly.bookid.repository.database.entities.Book

object Converters {
    @TypeConverter
    @JvmStatic
    fun fromStringToBookList(value: String): List<Book> {
        val type = object : TypeToken<List<Book>>() {}.type
        return Gson().fromJson<List<Book>>(value, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromBookListToString(list: List<Book>) = Gson().toJson(list)

    @TypeConverter
    @JvmStatic
    fun fromStringToAuthor(value: String): Author {
        val type = object : TypeToken<List<Book>>() {}.type
        return Gson().fromJson<Author>(value, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromAuthorToString(author: Author) = Gson().toJson(author)
}
