package com.example.flo_mainpage.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    private const val BASE_URL = "https://aos.inyro.site"

    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit {
        return retrofit ?: Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().also { retrofit = it }
    }
}