package com.example.app_trading.kotlin.CRUD.service

import com.example.app_trading.Noticias
import com.example.app_trading.kotlin.CRUD.Entity.Noticia
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FinnhubService {

    @GET("news")
    fun getGeneralNews(
        @Query("category") category: String = "general",
        @Query("token") token: String
    ): Call<List<Noticias>>


    interface FinnhubService {
        @GET("news")
        fun getGeneralNews(@Query("token") token: String): Call<List<Noticia>>
    }
}