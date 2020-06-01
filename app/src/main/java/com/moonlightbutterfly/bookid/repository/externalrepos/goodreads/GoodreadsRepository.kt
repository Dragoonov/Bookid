package com.moonlightbutterfly.bookid.repository.externalrepos.goodreads

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.moonlightbutterfly.bookid.repository.database.entities.Author
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.externalrepos.ExternalRepository
import com.moonlightbutterfly.bookid.repository.externalrepos.goodreads.dtos.GoodreadsResponseDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class GoodreadsRepository :
    ExternalRepository {

    private var retrofit: Retrofit? = null
    private val BASE_URL = "https://www.goodreads.com"

    private fun getRetrofitService() = retrofit?.create(RetrofitServiceGoodreads::class.java)
        ?: Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build().also { retrofit = it }
            .create(RetrofitServiceGoodreads::class.java)


    interface RetrofitServiceGoodreads {

        @GET("/search/index.xml")
        fun getBooksBySearchString(
            @Query("q") query: String,
            @Query("page") page: Int = 1,
            @Query("key") developerKey: String = "RxcevZGjLRZAdWYapNJBBg",
            @Query("search[field]") searchField: String = "all"
        ): Call<GoodreadsResponseDto>

        @GET("/author/show")
        fun getAuthorInfo(
            @Query("id") id: Int,
            @Query("key") developerKey: String = "RxcevZGjLRZAdWYapNJBBg"
        ): Call<GoodreadsResponseDto>
    }

    override fun getSimilarBooks(book: Book): LiveData<List<Book>> {
        TODO("Not yet implemented")
    }

    override fun getAuthorBooks(author: Author): LiveData<List<Book>> {
        val books = MutableLiveData<List<Book>>()
        getRetrofitService()
            .getAuthorInfo(author.id)
            .enqueue(object : Callback<GoodreadsResponseDto> {
                override fun onResponse(
                    call: Call<GoodreadsResponseDto>,
                    response: Response<GoodreadsResponseDto>
                ) {
                    books.value = GoodreadsConverters.extractAuthorBookListFromDto(response.body())
                }

                override fun onFailure(call: Call<GoodreadsResponseDto>, t: Throwable) {
                    Log.v("Error", t.localizedMessage!!)
                }

            })
        return books
    }

    override fun getSearchedBooks(query: String, page: Int): LiveData<List<Book>> {
        val books = MutableLiveData<List<Book>>()
        getRetrofitService()
            .getBooksBySearchString(query, page)
            .enqueue(object : Callback<GoodreadsResponseDto> {
                override fun onResponse(
                    call: Call<GoodreadsResponseDto>,
                    response: Response<GoodreadsResponseDto>
                ) {
                    books.value = GoodreadsConverters.extractBookListFromDto(response.body()!!)
                }

                override fun onFailure(call: Call<GoodreadsResponseDto>, t: Throwable) {
                    Log.v("Error", t.localizedMessage!!)
                }

            })
        return books
    }

    override fun getAuthorInfo(author: Author): LiveData<Author> {
        val authorResult = MutableLiveData<Author>()
        getRetrofitService()
            .getAuthorInfo(author.id)
            .enqueue(object : Callback<GoodreadsResponseDto> {
                override fun onResponse(
                    call: Call<GoodreadsResponseDto>,
                    response: Response<GoodreadsResponseDto>
                ) {
                    authorResult.value = GoodreadsConverters.extractAuthorFromDto(response.body()!!)
                }

                override fun onFailure(call: Call<GoodreadsResponseDto>, t: Throwable) {
                    Log.v("Error", t.localizedMessage!!)
                }

            })
        return authorResult
    }
}