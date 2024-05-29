package com.example.newsmd.data.remote

import com.example.newsmd.domain.Model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface newsAPI {



    @GET("top-headlines")
    suspend fun getBreakingNews(
        @Query("category") category: String,
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = API_KEY
    ): NewsResponse


    companion object {
        const val API_KEY = "94c93cde765a47a2930daf366f83ae0a"
        const val BASE_URL = "https://newsapi.org/v2/"
    }


}