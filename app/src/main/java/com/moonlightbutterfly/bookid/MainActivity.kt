package com.moonlightbutterfly.bookid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.moonlightbutterfly.bookid.repository.externalrepos.dtos.GoodreadsResponseDto
import com.moonlightbutterfly.bookid.repository.externalrepos.retrofit.GoodreadsRetrofit.RetrofitInstanceGoodreads
import com.moonlightbutterfly.bookid.repository.externalrepos.retrofit.GoodreadsRetrofit.RetrofitServiceGoodreads
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val service = RetrofitInstanceGoodreads
            .getRetrofitInstance()
            .create(RetrofitServiceGoodreads::class.java)
        val call = service.getBooks("Harry potter")
        call.enqueue(object : Callback<GoodreadsResponseDto> {

            override fun onFailure(call: Call<GoodreadsResponseDto>?, t: Throwable?) {
                Log.e("Fail", t?.message!!)
            }

            override fun onResponse(
                call: Call<GoodreadsResponseDto>?,
                response: Response<GoodreadsResponseDto>?
            ) {
                Log.d("Response", response.toString())
            }

        })
    }
}
