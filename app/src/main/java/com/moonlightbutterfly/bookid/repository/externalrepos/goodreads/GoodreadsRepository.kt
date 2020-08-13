package com.moonlightbutterfly.bookid.repository.externalrepos.goodreads

import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.externalrepos.ExternalRepository
import com.moonlightbutterfly.bookid.repository.externalrepos.goodreads.dtos.GoodreadsResponseDto
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

@Deprecated(message = "Old provider not compatible with current app", replaceWith = ReplaceWith("GoogleBooksRepository"))
class GoodreadsRepository @Inject constructor() : ExternalRepository {

    private var retrofit: Retrofit? = null

    private fun getRetrofitService() = retrofit?.create(RetrofitServiceGoodreads::class.java)
        ?: Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build().also { retrofit = it }
            .create(RetrofitServiceGoodreads::class.java)


    interface RetrofitServiceGoodreads {

        @GET("/search/index.xml")
        suspend fun getBooksBySearchString(
            @Query("q") query: String?,
            @Query("page") page: Int = 1,
            @Query("key") developerKey: String = "RxcevZGjLRZAdWYapNJBBg",
            @Query("search[field]") searchField: String = "all"
        ): Response<GoodreadsResponseDto>

        @GET("/author/show")
        suspend fun getAuthorInfo(
            @Query("id") id: Int,
            @Query("key") developerKey: String = "RxcevZGjLRZAdWYapNJBBg"
        ): Response<GoodreadsResponseDto>
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override suspend fun loadAuthorBooks(authorId: String?): List<Book> {
        val response = getRetrofitService().getAuthorInfo(authorId?.toInt()!!)
        return if (response.isSuccessful) {
            GoodreadsConverters.extractAuthorBookListFromDto(response.body())!!
        } else {
            ArrayList()
        }
    }

    override suspend fun loadSimilarBooks(author: String?): List<Book> {
        TODO("Goodreads doesn't provide that functionality")
    }

    override suspend fun loadSearchedBooks(query: String?, page: Int): List<Book> {
        val response = getRetrofitService().getBooksBySearchString(query, page)
        return if (response.isSuccessful) {
            GoodreadsConverters.extractBookListFromDto(response.body())!!
        } else {
            ArrayList()
        }
    }

    private companion object {
        private const val BASE_URL = "https://www.goodreads.com"
    }
}