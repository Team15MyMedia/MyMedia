package com.example.mymedia.data

import java.text.SimpleDateFormat
import java.util.Date
import kotlin.random.Random

object Data {
    private val searchData: MutableList<Item> = mutableListOf()
    private val bookmarkData: MutableList<Item> = mutableListOf()

    init {
        searchData.apply {
            repeat(10) {
                val randomItem = generateRandomData()
                add(randomItem)
            }
        }
    }

    fun getSearchData(): MutableList<Item> {
        return searchData
    }

    fun removeSearchItem(item: Item) {
        searchData.remove(item)
    }

    fun addSearchItem(item: Item) {
        searchData.add(item)
    }

    fun getBookmarkData(): MutableList<Item> {
        return bookmarkData
    }

    fun removeBookmarkItem(item: Item) {
        bookmarkData.remove(item)
    }

    fun addBookmarkItem(item: Item) {
        bookmarkData.add(item)
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
    fun generateRandomData(): Item {
        val randomId = Random.nextInt(1, 100)
        val randomTitle = "Item $randomId"
        val randomUrl = "https://example.com/item/$randomId"
        val randomDatetime = getCurrentDateTime()
        val randomThumbnail = "https://example.com/thumbnail/$randomId.jpg"
        val randomIsFavorite = Random.nextBoolean()

        return Item(randomId, randomTitle, randomUrl, randomDatetime, randomThumbnail, randomIsFavorite)
    }
}
