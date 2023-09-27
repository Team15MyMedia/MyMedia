package com.example.mymedia.data

import com.example.mymedia.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {
    @GET("search")
    suspend fun getAllByCategory(
        @Query("key") key: String = BuildConfig.YOUTUBE_API_KEY,
        @Query("part") part: String = "snippet",
        @Query("maxResults") maxResults: Int = 25,
    ): Response<ApiResponse>

    @GET("search")
    suspend fun getMostPopularVideos(
        @Query("key") key: String = BuildConfig.YOUTUBE_API_KEY,
        @Query("chart") chart: String = "mostPopular",
        @Query("type") type: String = "video",
        @Query("part") part: String = "snippet",
        @Query("maxResults") maxResults: Int = 25,
    ): Response<ApiResponse>
}