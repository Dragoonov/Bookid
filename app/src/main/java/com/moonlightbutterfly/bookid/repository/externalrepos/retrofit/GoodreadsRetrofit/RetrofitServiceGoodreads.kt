package com.moonlightbutterfly.bookid.repository.externalrepos.retrofit.GoodreadsRetrofit

import com.moonlightbutterfly.bookid.repository.externalrepos.dtos.GoodreadsResponseDto
import com.moonlightbutterfly.bookid.repository.externalrepos.dtos.ResultsDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitServiceGoodreads {

    @GET("/search/index.xml")
    fun getBooks(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("key") developerKey: String = "RxcevZGjLRZAdWYapNJBBg",
        @Query("search[field]") searchField: String = "all"
    ): Call<GoodreadsResponseDto>
}