package com.example.mymedia.mypage

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mymedia.data.ItemRepository
import com.example.mymedia.data.VideoItem
import com.example.mymedia.detail.DetailActivity
import java.util.Date

class MyPageViewModel(
    private val repository: ItemRepository,
) : ViewModel() {

    private val _favoriteVideo = MutableLiveData<MutableList<VideoItem>>()
    val favoriteVideo: LiveData<MutableList<VideoItem>>
        get() = _favoriteVideo

    private val _mainEvent = MutableLiveData<EventForMain>()
    val mainEvent: LiveData<EventForMain>
        get() = _mainEvent

    init {
        // test
        _favoriteVideo.value = mutableListOf(
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

    fun showDetail(videoItem: VideoItem, context: Context) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(
            "videoThumbnail",
            videoItem.thumbnail.replace("/default.jpg", "/maxresdefault.jpg")
        )
        intent.putExtra("videoTitle", videoItem.title)
        intent.putExtra("videoDescription", videoItem.description)
        context.startActivity(intent)
    }

    fun checkIsFavorite(item: VideoItem) {
        val curList = _favoriteVideo.value ?: return
        val curItem = curList.find { it.id == item.id }

        _mainEvent.value = EventForMain.CheckedItem(curItem)
    }

    fun updateItem(item: VideoItem) {
        val curList = _favoriteVideo.value ?: return
        val curItem = curList.find { it.id == item.id }

        if (item.isFavorite) {
            if (curItem == null) {
                curList.add(item)
            } else {
                return
            }
        } else {
            if (curItem == null) {
                return
            } else {
                curList.remove(curList.find { it.id == item.id })
            }
        }

        _favoriteVideo.value = curList
    }
}

sealed interface EventForMain {
    data class CheckedItem(
        val item: VideoItem?
    ) : EventForMain
}

class MyPageViewModelFactory(
    private val repository: ItemRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyPageViewModel::class.java)) {
            return MyPageViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}