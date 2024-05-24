package com.example.geetagyaan.db.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiUtilities {

    private val headers = mapOf(
        "Accept" to "application/json",
        "X-RapidAPI-Key" to "c27f1e4c94mshd1759ce357064fap19a4cbjsn507a43c7586f",
        "X-RapidAPI-Host" to "bhagavad-gita3.p.rapidapi.com"
    )

     private val client: OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor { chain->
            val newRequest = chain.request().newBuilder().apply {
                headers.forEach { (key, value) -> addHeader(key , value)  }
            }.build()
            chain.proceed(newRequest)
        }
    }.build()

    val api : ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl("https://bhagavad-gita3.p.rapidapi.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}