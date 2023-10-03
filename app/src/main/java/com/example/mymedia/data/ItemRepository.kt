package com.example.mymedia.data

import android.util.Log
import android.widget.Toast
import com.example.mymedia.main.MainActivity
import com.example.mymedia.search.SearchFragment
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.coroutines.coroutineContext

class ItemRepository {

    suspend fun findItemByCategory(id: String): Response<MutableList<MediaItem>> {
        return fetchCategoryMostList {
            RetrofitInstance.api.getVideoByCategory(
                videoCategoryId = id
            )
        }
    }

    suspend fun findChannelByID(channelId: String): Response<ChannelItem> {
        return fetchChannel {
            RetrofitInstance.api.getChannel(
                id = channelId
            )
        }
    }

//    suspend fun findMostVideo(): Response<MutableList<MediaItem>> {
//        return fetchItemList{
//                RetrofitInstance.api.searchMostPopularVideos(
//                    maxResults = 25,
//                )
//            }
//    }

    suspend fun findMostVideo(): Response<MutableList<MediaItem>> {
        return fetchMostVideo{
            RetrofitInstance.api.MostPopularVideos()
        }
    }

    suspend fun findMostLiveVideo(): Response<MutableList<MediaItem>> {
        return fetchItemList {
            RetrofitInstance.api.searchMostPopularLiveVideos()
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
            RetrofitInstance.api.searchChannel(
                query = text
            )
        }
    }

    private inline fun fetchItemList(
        fetchFunction: () -> Response<ApiResponse<SearchItem>>,
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

                val title = items.snippet?.title?.replace("&#39;", "'")?.replace("&quot;", "\"")

                // media type 확인
                when (items.id?.kind ?: "none type") {
                    "youtube#video" -> {
                        val item = VideoItem(
                            id = items.id?.videoId ?: "",
                            title = title ?: "none-title",
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

            // response.code : 200은 성공, 429는 사용자가 주어진 시간 동안 너무 많은 요청을 보냈음, 403은 클라이언트 오류 상태 응답 코드는 서버에 요청이 전달되었지만, 권한 때문에 거절
            if (response.code() == 403 || response.code() == 429) {
                Toast.makeText(
                    MainActivity.getContext(),
                    "API 호출 제한 오류! 나중에 다시 시도해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    MainActivity.getContext(),
                    "네트워크 오류! 에러 코드 ${response.code()}",
                    Toast.LENGTH_SHORT
                ).show()
            }

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
                // shorts 판단
                val playTime = items.contentDetails?.duration ?: return@forEach
                if (isShorts(playTime)) return@forEach

                // 날짜 변환
                val dateString = items.snippet?.publishedAt ?: ""
                val dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
                val date = stringToDate(dateString, dateFormat) ?: Date()

                val title = items.snippet?.title?.replace("&#39;", "'")?.replace("&quot;", "\"")

                val item = VideoItem(
                    id = items.id ?: "",
                    title = title ?: "none-title",
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
                if (items.snippet?.assignable == false) return@forEach

                val title = items.snippet?.title?.replace("&#39;", "'")?.replace("&quot;", "\"")

                val category = Category(
                    id = items.id ?: "0",
                    title = title ?: "none-title",
                )
                categoryList.add(category)
            }
            return Response.success(categoryList)
        } else {

            return Response.error(response.code(), response.errorBody())
        }
    }

    private inline fun fetchChannel(
        fetchFunction: () -> Response<ApiResponse<NoneSearchItem>>
    ): Response<ChannelItem> {
        val response = fetchFunction()

        if (response.isSuccessful) {
            val videoResponse = response.body()
            var item: ChannelItem? = null

            videoResponse?.items?.forEach { items ->
                // 날짜 변환
                val dateString = items.snippet?.publishedAt ?: ""
                val dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
                val date = stringToDate(dateString, dateFormat) ?: Date()

                val title = items.snippet?.title?.replace("&#39;", "'")?.replace("&quot;", "\"")

                item = ChannelItem(
                    id = items.id ?: "",
                    title = title ?: "none-title",
                    description = items.snippet?.description ?: "",
                    datetime = date,
                    thumbnail = items.snippet?.thumbnails?.default?.url ?: "",
                    isFavorite = false,
                )
            }

            return Response.success(item)
        } else {

            return Response.error(response.code(), response.errorBody())
        }
    }

    private inline fun fetchMostVideo(
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

                val title = items.snippet?.title?.replace("&#39;", "'")?.replace("&quot;", "\"")

                val item = VideoItem(
                    id = items.id ?: "",
                    title = title ?: "none-title",
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

    private fun stringToDate(dateString: String, dateFormat: String): Date? {
        return try {
            val sdf = SimpleDateFormat(dateFormat)
            sdf.parse(dateString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun isShorts(duration: String): Boolean {
        return duration.matches(Regex("PT\\d+S")) || duration == "PT1M"
    }
}