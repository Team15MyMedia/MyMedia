package com.example.mymedia.data

import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date

class ItemRepository {

    suspend fun findItemByCategory(id: String): Response<MutableList<MediaItem>> {
        return fetchCategoryMostList {
            RetrofitInstance.api.getVideoByCategory(
                videoCategoryId = id
            )
        }
    }

    suspend fun findMostVideo(): Response<MutableList<MediaItem>> {
        return fetchItemList {
            RetrofitInstance.api.searchMostPopularVideos(
                chart = "mostPopular",
                maxResults = 25,
            )
        }
    }

    suspend fun findCategoryList(): Response<MutableList<Category>> {
        return fetchCategoryList {
            RetrofitInstance.api.getCategoryList()
        }
    }

    suspend fun searchVideo(text: String): Response<MutableList<MediaItem>> {
        return fetchItemList {
            RetrofitInstance.api.searchVideos(
                query = text
            )
        }
    }

    suspend fun searchChannel(text: String): Response<MutableList<MediaItem>> {
        return fetchItemList {
            RetrofitInstance.api.searchCategory(
                query = text
            )
        }
    }

    private inline fun fetchItemList(
        fetchFunction: () -> Response<ApiResponse<SearchItem>>
    ): Response<MutableList<MediaItem>> {
        val response = fetchFunction()

        if (response.isSuccessful) {
            val videoResponse = response.body()
            val mediaItemList = mutableListOf<MediaItem>()

            videoResponse?.items?.forEach { items ->
                // 날짜 변환
                val dateString = items.snippet?.publishedAt ?: ""
                val dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
                val date = stringToDate(dateString, dateFormat) ?: Date()

                // media type 확인
                when (items.id?.kind ?: "none type") {
                    "youtube#video" -> {
                        val item = VideoItem(
                            id = items.id?.videoId ?: "",
                            title = items.snippet?.title ?: "none-title",
                            description = items.snippet?.description ?: "",
                            datetime = date,
                            thumbnail = items.snippet?.thumbnails?.default?.url ?: "",
                            isFavorite = false,
                            channelId = items.snippet?.channelId ?: "",
                        )
                        mediaItemList.add(item)
                    }

                    "youtube#channel" -> {
                        val item = ChannelItem(
                            id = items.id?.videoId ?: "",
                            title = items.snippet?.title ?: "none-title",
                            description = items.snippet?.description ?: "",
                            datetime = date,
                            thumbnail = items.snippet?.thumbnails?.default?.url ?: "",
                            isFavorite = false,
                        )
                        mediaItemList.add(item)
                    }
                }
            }

            return Response.success(mediaItemList)
        } else {
            return Response.error(response.code(), response.errorBody())
        }
    }

    private inline fun fetchCategoryMostList(
        fetchFunction: () -> Response<ApiResponse<NoneSearchItem>>
    ): Response<MutableList<MediaItem>> {
        val response = fetchFunction()

        if (response.isSuccessful) {
            val videoResponse = response.body()
            val mediaItemList = mutableListOf<MediaItem>()

            videoResponse?.items?.forEach { items ->
                // 날짜 변환
                val dateString = items.snippet?.publishedAt ?: ""
                val dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
                val date = stringToDate(dateString, dateFormat) ?: Date()

                val item = VideoItem(
                    id = items.id ?: "",
                    title = items.snippet?.title ?: "none-title",
                    description = items.snippet?.description ?: "",
                    datetime = date,
                    thumbnail = items.snippet?.thumbnails?.default?.url ?: "",
                    isFavorite = false,
                    channelId = items.snippet?.channelId ?: "",
                )
                mediaItemList.add(item)
            }

            return Response.success(mediaItemList)
        } else {
            return Response.error(response.code(), response.errorBody())
        }
    }

    private inline fun fetchCategoryList(
        fetchFunction: () -> Response<ApiResponse<NoneSearchItem>>
    ): Response<MutableList<Category>> {
        val response = fetchFunction()

        if (response.isSuccessful) {
            val categoryResponse = response.body()
            val categoryList = mutableListOf<Category>()

            categoryResponse?.items?.forEach { items ->
                val category = Category(
                    id = items.id ?: "0",
                    title = items.snippet?.title ?: "none-title",
                )
                categoryList.add(category)
            }
            return Response.success(categoryList)
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