package com.moonlightbutterfly.bookid.repository.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
}
