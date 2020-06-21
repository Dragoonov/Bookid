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
import javax.inject.Inject

class GoodreadsRepository @Inject constructor() : ExternalRepository {

    private var retrofit: Retrofit? = null
    private val BASE_URL = "https://www.goodreads.com"

    private val _similarBooksLiveData = MutableLiveData<List<Book>>()
    override val similarBooksLiveData: LiveData<List<Book>> get() = _similarBooksLiveData

    private val _searchedBooksLiveData = MutableLiveData<List<Book>>()
    override val searchedBooksLiveData: LiveData<List<Book>> get() = _searchedBooksLiveData

    private val _authorBooksLiveData = MutableLiveData<List<Book>>()
    override val authorBooksLiveData: LiveData<List<Book>> get() = _authorBooksLiveData

    private val _authorInfoLiveData = MutableLiveData<Author>()
    override val authorInfoLiveData: LiveData<Author> get() = _authorInfoLiveData

    private var currentCall: Call<GoodreadsResponseDto>? = null

    private fun getRetrofitService() = retrofit?.create(RetrofitServiceGoodreads::class.java)
        ?: Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build().also { retrofit = it }
            .create(RetrofitServiceGoodreads::class.java)


    interface RetrofitServiceGoodreads {

        @GET("/search/index.xml")
        fun getBooksBySearchString(
            @Query("q") query: String?,
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

    override fun loadSimilarBooks(book: Book): LiveData<List<Book>> {
        return similarBooksLiveData
    }

    override fun loadAuthorBooks(author: Author): LiveData<List<Book>> {
        getRetrofitService()
            .getAuthorInfo(author?.id)
            .enqueue(object : Callback<GoodreadsResponseDto> {
                override fun onResponse(
                    call: Call<GoodreadsResponseDto>,
                    response: Response<GoodreadsResponseDto>
                ) {
                    _authorBooksLiveData.value = GoodreadsConverters.extractAuthorBookListFromDto(response.body())
                }

                override fun onFailure(call: Call<GoodreadsResponseDto>, t: Throwable) {
                    Log.v("Error", t.localizedMessage!!)
                }

            })
        return authorBooksLiveData
    }

    override fun loadSearchedBooks(query: String?, page: Int): LiveData<List<Book>> {
        cancelCurrentRequest()
        getRetrofitService()
            .getBooksBySearchString(query, page).apply {
                currentCall = this
                enqueue(object : Callback<GoodreadsResponseDto> {
                override fun onResponse(
                    call: Call<GoodreadsResponseDto>,
                    response: Response<GoodreadsResponseDto>
                ) {
                    _searchedBooksLiveData.value =
                        GoodreadsConverters.extractBookListFromDto(response.body())
                }

                override fun onFailure(call: Call<GoodreadsResponseDto>, t: Throwable) {
                    Log.v("Error", t.localizedMessage!!)
                }

            })
            }
        return searchedBooksLiveData
    }

    private fun cancelCurrentRequest() {
        if (currentCall != null && !currentCall!!.isCanceled) {
            currentCall!!.cancel()
        }
    }

    override fun loadAuthorInfo(author: Author): LiveData<Author> {
        getRetrofitService()
            .getAuthorInfo(author.id).apply {
                currentCall = this
                enqueue(object : Callback<GoodreadsResponseDto> {
                override fun onResponse(
                    call: Call<GoodreadsResponseDto>,
                    response: Response<GoodreadsResponseDto>
                ) {
                    _authorInfoLiveData.value =
                        GoodreadsConverters.extractAuthorFromDto(response.body())
                }

                override fun onFailure(call: Call<GoodreadsResponseDto>, t: Throwable) {
                    Log.v("Error", t.localizedMessage!!)
                }

            })
            }
        return authorInfoLiveData
    }
}