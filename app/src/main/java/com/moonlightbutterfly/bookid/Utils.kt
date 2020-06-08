package com.moonlightbutterfly.bookid

import com.google.gson.Gson

object Utils {

    val gson: Gson = Gson()

    fun <T> convertToJSONString(obj: T?) = gson.toJson(obj)

    inline fun <reified T> convertToObject(string: String?): T = gson.fromJson(string, T::class.java)
}