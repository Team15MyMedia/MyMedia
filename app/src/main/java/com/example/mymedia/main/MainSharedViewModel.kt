package com.example.mymedia.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymedia.data.VideoItem

class MainSharedViewModel() : ViewModel() {

    private val _myPageEvent = MutableLiveData<MainEventForMyPage>()
    val myPageEvent: LiveData<MainEventForMyPage>
        get() = _myPageEvent

    private val _detailEvent = MutableLiveData<MainEventForDetail>()
    val detailEvent: LiveData<MainEventForDetail>
        get() = _detailEvent

    var selItem: VideoItem? = null

    // myPage에 event 전달
    fun isFavorite(videoItem: VideoItem) {
        _myPageEvent.value = MainEventForMyPage.CheckMyPageItem(videoItem)
    }

    // mypage에서 온 이벤트
    fun checkVideo(item: VideoItem?) {
        selItem = item
    }

    fun updateData(item: VideoItem) {
        _myPageEvent.value = MainEventForMyPage.UpdateMyPageItem(item)
    }
}

sealed interface MainEventForMyPage {
    data class AddMyPageItem(
        val item: VideoItem
    ) : MainEventForMyPage

    data class RemoveMyPageItem(
        val item: VideoItem
    ) : MainEventForMyPage

    data class UpdateMyPageItem(
        val item: VideoItem
    ) : MainEventForMyPage

    data class CheckMyPageItem(
        val item: VideoItem
    ) : MainEventForMyPage
}

sealed interface MainEventForDetail {
    data class CheckDetailItem(
        val items: MutableList<VideoItem>
    ) : MainEventForMyPage
}

