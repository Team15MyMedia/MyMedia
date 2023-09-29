package com.example.mymedia.mypage

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mymedia.data.Data
import com.example.mymedia.data.ItemRepository
import com.example.mymedia.data.VideoItem
import com.example.mymedia.detail.DetailActivity

class MyPageViewModel(
    private val repository: ItemRepository,
) : ViewModel() {
    private val _favoriteVideo = MutableLiveData<MutableList<VideoItem>>()

    val favoriteVideo: LiveData<MutableList<VideoItem>>
        get() = _favoriteVideo

    init {
        // test
        _favoriteVideo.value = Data.getFavoriteData()
    }

    fun showDetail(videoItem: VideoItem, context: Context) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra("videoThumbnail", videoItem.thumbnail)
        intent.putExtra("videoTitle", videoItem.title)
        intent.putExtra("videoDescription", videoItem.description)
        context.startActivity(intent)
    }
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