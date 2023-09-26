package com.example.mymedia.data

import android.util.Log
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date

class ItemRepository {

    suspend fun findVideoByCategory(): Response<MutableList<VideoItem>> {
        return fetchVideoList {
            RetrofitInstance.api.getVideoByCategory()
        }
    }

    suspend fun findMostVideo(): Response<MutableList<VideoItem>> {
        return fetchVideoList {
            RetrofitInstance.api.getMostPopularVideos(
                chart = "mostPopular",
                maxResults = 25,
            )
        }
    }

    private inline fun fetchVideoList(
        fetchFunction: () -> Response<ApiResponse>
    ): Response<MutableList<VideoItem>> {
        val response = fetchFunction()

        if (response.isSuccessful) {
            val videoResponse = response.body()
            val videoList = mutableListOf<VideoItem>()

            videoResponse?.items?.forEach { items ->
                // 날짜 변환
                val dateString = items.snippet?.publishedAt ?: ""
                val dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
                val date = stringToDate(dateString, dateFormat) ?: Date()

                val item = VideoItem(
                    id = items.id?.videoId ?: "",
                    title = items.snippet?.title ?: "none-title",
                    description = items.snippet?.description ?: "",
                    datetime = date,
                    thumbnail = items.snippet?.thumbnails?.default?.url ?: "",
                    isFavorite = false,
                )
                videoList.add(item)
            }


            return Response.success(videoList)
        } else {
            return Response.error(response.code(), response.errorBody())
        }
    }

    private fun stringToDate(dateString: String, dateFormat: String): Date? {
        return try {
            val sdf = SimpleDateFormat(dateFormat)
            sdf.parse(dateString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}