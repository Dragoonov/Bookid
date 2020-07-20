package com.moonlightbutterfly.bookid

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.moonlightbutterfly.bookid.repository.database.entities.Author
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf

object Converters {

    @TypeConverter
    @JvmStatic
    fun fromStringToBookList(value: String): List<Book>? {
        val type = object : TypeToken<List<Book>>() {}.type
        return try {
            Gson().fromJson(value, type)
        } catch (e: JsonSyntaxException) {
            null
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromBookListToString(list: List<Book>): String? = Gson().toJson(list)

    @TypeConverter
    @JvmStatic
    fun fromStringToAuthor(value: String): Author? {
        val type = object : TypeToken<Author>() {}.type
        return try {
            Gson().fromJson(value, type)
        } catch (e: JsonSyntaxException) {
            null
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromAuthorToString(author: Author): String? = Gson().toJson(author)

    @TypeConverter
    @JvmStatic
    fun fromStringToShelfList(value: String): List<Shelf>? {
        val type = object : TypeToken<List<Shelf>>() {}.type
        return try {
            Gson().fromJson(value, type)
        } catch (e: JsonSyntaxException) {
            null
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromShelfListToString(shelfList: List<Shelf>): String? = Gson().toJson(shelfList)

    fun <T> convertToJSONString(obj: T?): String? = Gson().toJson(obj)

    inline fun <reified T> convertToObject(string: String?): T? =
        try {
            Gson().fromJson(string, T::class.java)
        } catch (e: JsonSyntaxException) {
            null
        }

}
