package com.moonlightbutterfly.bookid.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.moonlightbutterfly.bookid.repository.database.entities.*

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
    fun fromStringListToString(list: List<String>): String? = Gson().toJson(list)


    @TypeConverter
    @JvmStatic
    fun fromStringToStringList(value: String): List<String>? {
        val type = object : TypeToken<List<String>>() {}.type
        return try {
            Gson().fromJson(value, type)
        } catch (e: JsonSyntaxException) {
            null
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromPublicationInfoToString(publicationInfo: PublicationInfo): String? = Gson().toJson(publicationInfo)

    @TypeConverter
    @JvmStatic
    fun fromStringToPublicationInfo(value: String): PublicationInfo? {
        val type = object : TypeToken<PublicationInfo>() {}.type
        return try {
            Gson().fromJson(value, type)
        } catch (e: JsonSyntaxException) {
            null
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromISBNToString(isbn: ISBN): String? = Gson().toJson(isbn)

    @TypeConverter
    @JvmStatic
    fun fromStringToISBN(value: String): ISBN? {
        val type = object : TypeToken<ISBN>() {}.type
        return try {
            Gson().fromJson(value, type)
        } catch (e: JsonSyntaxException) {
            null
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromRatingToString(rating: Rating): String? = Gson().toJson(rating)

    @TypeConverter
    @JvmStatic
    fun fromStringToRating(value: String): Rating? {
        val type = object : TypeToken<Rating>() {}.type
        return try {
            Gson().fromJson(value, type)
        } catch (e: JsonSyntaxException) {
            null
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromStringToCover(value: String): Cover? {
        val type = object : TypeToken<Cover>() {}.type
        return try {
            Gson().fromJson(value, type)
        } catch (e: JsonSyntaxException) {
            null
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromCoverToString(cover: Cover): String? = Gson().toJson(cover)


    @TypeConverter
    @JvmStatic
    fun fromAccessInfoToString(accessInfo: AccessInfo): String? = Gson().toJson(accessInfo)

    @TypeConverter
    @JvmStatic
    fun fromStringToAccessInfo(value: String): AccessInfo? {
        val type = object : TypeToken<AccessInfo>() {}.type
        return try {
            Gson().fromJson(value, type)
        } catch (e: JsonSyntaxException) {
            null
        }
    }

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
