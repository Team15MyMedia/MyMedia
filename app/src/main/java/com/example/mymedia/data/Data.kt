package com.example.mymedia.data

import java.util.Date
import kotlin.random.Random

object Data {
    private val searchData: MutableList<VideoItem> = mutableListOf()
    private val bookmarkData: MutableList<VideoItem> = mutableListOf()
    private val mediaData: MutableList<MediaItem> = mutableListOf()

    init {
        searchData.add(
            VideoItem(
                "",
                "",
                "",
                Date(),
                "https://i.ytimg.com/vi/n3wr3IOO4N8/default.jpg",
                false
            )
        )
        searchData.apply {
            repeat(10) {
                val randomItem = generateRandomVideoData()
                add(randomItem)
            }
        }
        mediaData.apply {
            repeat(5) {
                val randomChannelItem = generateRandomChannelData()
                add(randomChannelItem)
                val randomVideoItem = generateRandomVideoData()
                add(randomVideoItem)
            }
        }
    }

    fun getSearchData(): MutableList<VideoItem> {
        return searchData
    }

    fun getMediaData(): MutableList<MediaItem> {
        return mediaData
    }

    fun removeSearchItem(videoItem: VideoItem) {
        searchData.remove(videoItem)
    }

    fun addSearchItem(videoItem: VideoItem) {
        searchData.add(videoItem)
    }

    fun getBookmarkData(): MutableList<VideoItem> {
        return bookmarkData
    }

    fun removeBookmarkItem(videoItem: VideoItem) {
        bookmarkData.remove(videoItem)
    }

    fun addBookmarkItem(videoItem: VideoItem) {
        bookmarkData.add(videoItem)
    }


    // 현재 시각을 가져오는 함수
    private fun getCurrentDateTime(): Date {
        return Date()
    }

    // 날짜 포맷을 지정하는 함수


    // 임의의 데이터를 생성하는 함수
    private fun generateRandomVideoData(): VideoItem {
        val randomId = Random.nextInt(1, 100)
        val randomTitle = "Item $randomId"
        val randomUrl = "https://example.com/item/$randomId"
        val randomDatetime = getCurrentDateTime()
        val randomThumbnail = "https://example.com/thumbnail/$randomId.jpg"
        val randomIsFavorite = Random.nextBoolean()

        return VideoItem(
            "",
            randomTitle,
            randomUrl,
            randomDatetime,
            randomThumbnail,
            randomIsFavorite
        )
    }

    private fun generateRandomChannelData(): ChannelItem {
        val randomId = Random.nextInt(1, 100)
        val randomTitle = "Item $randomId"
        val randomUrl = "https://example.com/item/$randomId"
        val randomDatetime = getCurrentDateTime()
        val randomThumbnail = "https://example.com/thumbnail/$randomId.jpg"
        val randomIsFavorite = Random.nextBoolean()

        return ChannelItem(
            "",
            randomTitle,
            randomUrl,
            randomDatetime,
            randomThumbnail,
            randomIsFavorite
        )
    }
}
