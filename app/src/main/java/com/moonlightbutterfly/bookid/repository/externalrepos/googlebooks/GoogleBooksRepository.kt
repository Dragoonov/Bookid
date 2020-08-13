package com.moonlightbutterfly.bookid.repository.externalrepos.googlebooks

import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.externalrepos.ExternalRepository
import com.moonlightbutterfly.bookid.repository.externalrepos.googlebooks.dtos.VolumeListDto
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

class GoogleBooksRepository @Inject constructor(): ExternalRepository {
    private var retrofit: Retrofit? = null

    private fun getRetrofitService() = retrofit?.create(RetrofitServiceGoogleBooks::class.java)
        ?: Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().also { retrofit = it }
            .create(RetrofitServiceGoogleBooks::class.java)


    interface RetrofitServiceGoogleBooks {

        @GET("volumes")
        suspend fun getBooksBySearchString(
            @Query("q") query: String?,
            @Query("page") page: Int = 1,
            @Query("startIndex") startIndex: Int = 0,
            @Query("maxResults") maxResults: Int = ITEMS_PER_REQUEST,
            @Query("projection") projection: String = "full",
            @Query("key") developerKey: String = "AIzaSyAKofDXfSaF5-75HyFBJLJt-2pApcP2Dw0"
        ): Response<VolumeListDto>

        companion object {
            const val ITEMS_PER_REQUEST = 20
        }
    }

    override suspend fun loadAuthorBooks(author: String?): List<Book> {
        val response = getRetrofitService().getBooksBySearchString("inauthor:$author")
        return if (response.isSuccessful) {
            GoogleBooksConverters.extractBookListFromDto(response.body())
        } else {
            ArrayList()
        }
    }

    override suspend fun loadSimilarBooks(cathegory: String?): List<Book> {
        val response = getRetrofitService().getBooksBySearchString("subject:$cathegory")
        return if (response.isSuccessful) {
            GoogleBooksConverters.extractBookListFromDto(response.body())
        } else {
            ArrayList()
        }
    }

    override suspend fun loadSearchedBooks(query: String?, page: Int): List<Book> {
        val googleStartIndex = RetrofitServiceGoogleBooks.ITEMS_PER_REQUEST*(page-1)
        val response = getRetrofitService().getBooksBySearchString(query, page, googleStartIndex)
        return if (response.isSuccessful) {
            GoogleBooksConverters.extractBookListFromDto(response.body())
        } else {
            ArrayList()
        }
    }

    private companion object {
        private const val BASE_URL = "https://www.googleapis.com/books/v1/"
    }
}