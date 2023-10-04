package com.example.mymedia.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymedia.data.VideoItem
import java.util.Date

class MainSharedViewModel() : ViewModel() {

    private val _favoriteList = MutableLiveData<MutableList<VideoItem>>()
    val favoriteList: LiveData<MutableList<VideoItem>>
        get() = _favoriteList

    init {
//        sharedPreference에서 가져오기...
        _favoriteList.value = mutableListOf(
            VideoItem(
                id = "HLnTVgdP_g0",
                title = "이집트 홍해 한달살기가 최고인 이유 【이집트4】",
                description = "이유는 저도 모름ㅋ",
                datetime = Date(), // 임의의 날짜 값
                thumbnail = "https://example.com/thumbnail.jpg", // 임의의 이미지 URL
                isFavorite = true,
                nextPage = "https://example.com/nextpage", // 임의의 다음 페이지 URL
                channelId = "UC1234567890" // 임의의 채널 ID
            )
        )
    }

    fun getIsFavorite(videoItem: VideoItem): Boolean {
        val curList = _favoriteList.value ?: return false
        Log.d("favorite", curList.toString())
        return curList.find { it.id == videoItem.id } != null
    }
}