package com.moonlightbutterfly.bookid.repository.externalrepos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.moonlightbutterfly.bookid.repository.database.entities.Author
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.externalrepos.dtos.GoodreadsResponseDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class GoodreadsRepository : ExternalRepository {

    private var retrofit: Retrofit? = null
    private val BASE_URL = "https://www.goodreads.com"

    private fun getRetrofitService() = retrofit?.create(RetrofitServiceGoodreads::class.java)
        ?: Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build().also { retrofit = it }
            .create(RetrofitServiceGoodreads::class.java)

    private fun extractBookListFromDto(response: GoodreadsResponseDto): List<Book> {
        val worksList = response.search?.results?.works
        return worksList!!
            .map { workDto ->
                Book(
                    workDto.id!!,
                    workDto.bestBook?.title!!,
                    Author(
                        workDto.bestBook?.author?.id!!,
                        workDto.bestBook?.author?.name!!
                    ),
                    String.format(
                        "%d.%d.%d",
                        workDto.originalPublicationDay,
                        workDto.originalPublicationMonth,
                        workDto.originalPublicationYear
                    ),
                    workDto.averageRating!!,
                    workDto.bestBook?.imageUrl!!,
                    workDto.bestBook?.smallImageUrl!!
                )
            }
            .toList()
    }

    interface RetrofitServiceGoodreads {

        @GET("/search/index.xml")
        fun getBooksBySearchString(
            @Query("q") query: String,
            @Query("page") page: Int = 1,
            @Query("key") developerKey: String = "RxcevZGjLRZAdWYapNJBBg",
            @Query("search[field]") searchField: String = "all"
        ): Call<GoodreadsResponseDto>
    }

    override fun getSimilarBooks(book: Book): LiveData<List<Book>> {
        TODO("Not yet implemented")
    }

    override fun getAuthorBooks(author: Author): LiveData<List<Book>> {
        TODO("Not yet implemented")
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
                    books.value = extractBookListFromDto(response.body()!!)
                }

                override fun onFailure(call: Call<GoodreadsResponseDto>, t: Throwable) {
                    Log.v("Error", t.localizedMessage!!)
                }

            })
        return books
    }
}