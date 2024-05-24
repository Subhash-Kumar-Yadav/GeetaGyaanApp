package com.example.geetagyaan.db.api

import com.example.geetagyaan.models.ChaptersItem
import com.example.geetagyaan.models.VersesItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ApiInterface {

    @GET("/v2/chapters/")
    fun getAllChapter() : Call<List<ChaptersItem>>

    @GET("/v2/chapters/{chapterNumber}/verses/")
    fun getAllVerses(@Path("chapterNumber") chapterNumber : Int): Call<List<VersesItem>>

    @GET("/v2/chapters/{chapterNumber}/verses/{verseNumber}/'")
    fun getParticularVerse(
        @Path("chapterNumber") chapterNumber: Int,
        @Path("verseNumber") verseNumber: Int
    ):Call<VersesItem>
}