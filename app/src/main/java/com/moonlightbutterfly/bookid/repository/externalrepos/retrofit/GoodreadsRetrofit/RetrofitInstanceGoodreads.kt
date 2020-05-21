package com.moonlightbutterfly.bookid.repository.externalrepos.retrofit.GoodreadsRetrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class RetrofitInstanceGoodreads {

    companion object {
        private var retrofit: Retrofit? = null
        private val BASE_URL = "https://www.goodreads.com"

        fun getRetrofitInstance() = retrofit
            ?:
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build().also { retrofit = it }
    }
}