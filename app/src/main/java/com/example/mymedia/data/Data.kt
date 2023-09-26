package com.example.mymedia.data

import java.text.SimpleDateFormat
import java.util.Date
import kotlin.random.Random

object Data {
    private val searchData: MutableList<VideoItem> = mutableListOf()
    private val bookmarkData: MutableList<VideoItem> = mutableListOf()

    init {
        searchData.apply {
            repeat(10) {
                val randomItem = generateRandomData()
                add(randomItem)
            }
        }
    }

    fun getSearchData(): MutableList<VideoItem> {
        return searchData
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
    fun getCurrentDateTime(): Date {
        return Date()
    }

    // 날짜 포맷을 지정하는 함수
    fun formatDate(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return format.format(date)
    }

    // 임의의 데이터를 생성하는 함수
    fun generateRandomData(): VideoItem {
        val randomId = Random.nextInt(1, 100)
        val randomTitle = "Item $randomId"
        val randomUrl = "https://example.com/item/$randomId"
        val randomDatetime = getCurrentDateTime()
        val randomThumbnail = "https://example.com/thumbnail/$randomId.jpg"
        val randomIsFavorite = Random.nextBoolean()

        return VideoItem("", randomTitle, randomUrl, randomDatetime, randomThumbnail, randomIsFavorite)
    }
}
