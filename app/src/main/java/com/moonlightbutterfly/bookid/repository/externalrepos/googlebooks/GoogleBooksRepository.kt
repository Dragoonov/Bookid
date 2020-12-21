package com.moonlightbutterfly.bookid.repository.externalrepos.googlebooks

import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.externalrepos.ExternalRepository
import com.moonlightbutterfly.bookid.repository.externalrepos.googlebooks.dtos.VolumeListDto
import io.reactivex.Single
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

class GoogleBooksRepository @Inject constructor() : ExternalRepository {
    private var retrofit: Retrofit? = null

    private val responseMapper: (Response<VolumeListDto>) -> List<Book> = {
        if (it.isSuccessful) {
            GoogleBooksConverters.extractBookListFromDto(it.body())
        } else {
            ArrayList()
        }
    }

    private fun getRetrofitService() = retrofit?.create(RetrofitServiceGoogleBooks::class.java)
        ?: Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().also { retrofit = it }
            .create(RetrofitServiceGoogleBooks::class.java)


    interface RetrofitServiceGoogleBooks {

        @GET("volumes")
        fun getBooksBySearchString(
            @Query("q") query: String?,
            @Query("page") page: Int = 1,
            @Query("startIndex") startIndex: Int = 0,
            @Query("maxResults") maxResults: Int = ITEMS_PER_REQUEST,
            @Query("projection") projection: String = "full",
            @Query("key") developerKey: String = "AIzaSyAKofDXfSaF5-75HyFBJLJt-2pApcP2Dw0"
        ): Single<Response<VolumeListDto>>

        companion object {
            const val ITEMS_PER_REQUEST = 20
        }
    }

    override fun loadAuthorBooks(author: String?): Single<List<Book>> = getRetrofitService()
        .getBooksBySearchString("inauthor:$author")
        .map(responseMapper)


    override fun loadSimilarBooks(cathegory: String?): Single<List<Book>> = getRetrofitService()
        .getBooksBySearchString("subject:$cathegory")
        .map(responseMapper)

    override fun loadSearchedBooks(query: String?, page: Int): Single<List<Book>> {
        val googleStartIndex = RetrofitServiceGoogleBooks.ITEMS_PER_REQUEST * (page - 1)
        return getRetrofitService().getBooksBySearchString(query, page, googleStartIndex)
            .map(responseMapper)
    }

    private companion object {
        private const val BASE_URL = "https://www.googleapis.com/books/v1/"
    }
}