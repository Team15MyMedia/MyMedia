package com.example.mymedia.data

object Data {
    private val searchData: MutableList<Item> = mutableListOf()
    private val bookmarkData: MutableList<Item> = mutableListOf()

    init {

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
}
