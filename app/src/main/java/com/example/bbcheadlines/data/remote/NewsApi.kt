package com.example.bbcheadlines.data.remote

import com.example.bbcheadlines.data.remote.dto.TopHeadlinesResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("sources") source: String,
        @Query("apiKey") apiKey: String
    ): TopHeadlinesResponseDto
}