package com.sunny.handbook.ui.network

import com.sunny.handbook.BASE_URL
import com.sunny.handbook.data.PreferenceManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private var httpClient: OkHttpClient = OkHttpClient.Builder().addInterceptor(logging)
        .addInterceptor { chain ->
            val request =
                chain.request().newBuilder()
            PreferenceManager.token?.let {
                request.addHeader("authtoken", it)
            }


            chain.proceed(request.build())
        }.build()
    val RETROFIT: Api = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(
        GsonConverterFactory
            .create()
    ).client(httpClient).build().create(Api::class.java)
}