package com.example.mymedia.data

import com.example.mymedia.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {
    @GET("videos")
    suspend fun getVideoByCategory(
        @Query("key") key: String = BuildConfig.YOUTUBE_API_KEY,
        @Query("part") part: String = "snippet",
        @Query("chart") chart: String = "mostPopular",
        @Query("regionCode") regionCode: String = "KR",
        @Query("videoCategoryId") videoCategoryId: String,
    ): Response<ApiResponse<CategoryItem>>

    @GET("search")
    suspend fun getMostPopularVideos(
        @Query("key") key: String = BuildConfig.YOUTUBE_API_KEY,
        @Query("chart") chart: String = "mostPopular",
        @Query("type") type: String = "video",
        @Query("part") part: String = "snippet",
        @Query("maxResults") maxResults: Int = 25,
    ): Response<ApiResponse<Item>>

    @GET("videoCategories")
    suspend fun getCategoryList(
        @Query("key") key: String = BuildConfig.YOUTUBE_API_KEY,
        @Query("regionCode") regionCode: String = "KR",
        @Query("part") part: String = "snippet",
    ): Response<ApiResponse<CategoryItem>>

    @GET("search")
    suspend fun searchVideos(
        @Query("key") key: String = BuildConfig.YOUTUBE_API_KEY,
        @Query("q") query: String,
        @Query("chart") chart: String = "mostPopular",
        @Query("type") type: String = "video",
        @Query("part") part: String = "snippet",
        @Query("maxResults") maxResults: Int = 25,
    ): Response<ApiResponse<Item>>

    @GET("search")
    suspend fun searchCategory(
        @Query("key") key: String = BuildConfig.YOUTUBE_API_KEY,
        @Query("q") query: String,
        @Query("chart") chart: String = "mostPopular",
        @Query("type") type: String = "channel",
        @Query("part") part: String = "snippet",
        @Query("maxResults") maxResults: Int = 25,
    ): Response<ApiResponse<Item>>
}