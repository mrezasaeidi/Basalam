package com.basalam.storage.repository.remote

import com.basalam.Constants
import com.basalam.storage.repository.remote.entity.ProductQuery
import com.basalam.storage.repository.remote.entity.ProductResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ProductWebService {
    @POST("/api/user")
    fun getProducts(@Body body: ProductQuery): Call<ProductResponse>

    companion object {
        operator fun invoke(): ProductWebService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            return Retrofit.Builder()
                .baseUrl(Constants.END_POINT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(ProductWebService::class.java)
        }
    }
}