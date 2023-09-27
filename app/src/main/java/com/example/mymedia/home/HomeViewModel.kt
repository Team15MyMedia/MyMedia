package com.example.mymedia.home

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mymedia.data.ChannelItem
import com.example.mymedia.data.Data
import com.example.mymedia.data.ItemRepository
import com.example.mymedia.data.MediaItem
import com.example.mymedia.data.VideoItem
import com.example.mymedia.detail.DetailActivity
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: ItemRepository,
) : ViewModel() {

    private val _categoryVideo = MutableLiveData<MutableList<VideoItem>>()

    val categoryVideo: LiveData<MutableList<VideoItem>>
        get() = _categoryVideo

    private val _categoryChannel = MutableLiveData<MutableList<ChannelItem>>()

    val categoryChannel: LiveData<MutableList<ChannelItem>>
        get() = _categoryChannel

    private val _most = MutableLiveData<MutableList<VideoItem>>()
    val most: LiveData<MutableList<VideoItem>>
        get() = _most

    init {
        _categoryVideo.value = Data.getMediaData().filterIsInstance<VideoItem>().toMutableList()
        _categoryChannel.value = Data.getMediaData().filterIsInstance<ChannelItem>().toMutableList()
        _most.value = Data.getSearchData()
    }

    fun showDetail(videoItem: VideoItem, context: Context) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra("videoThumbnail", videoItem.thumbnail)
        intent.putExtra("videoTitle", videoItem.title)
        intent.putExtra("videoDescription", videoItem.description)

        context.startActivity(intent)
    }

    fun searchMostVideo() {
        viewModelScope.launch {
            val list = mutableListOf<MediaItem>()
            // Video
            val responseVideo = repository.findMostVideo()
            if (responseVideo.isSuccessful) {
                val itemList = responseVideo.body() ?: mutableListOf()
                list.addAll(itemList)
            } else {
                // null일 시 공백 리스트 생성
                _most.value = mutableListOf()
            }
            _most.value = list.filterIsInstance<VideoItem>().toMutableList()
        }
    }

    fun searchByCategory() {
        viewModelScope.launch {
            val list = mutableListOf<MediaItem>()
            // Video
            val responseVideo = repository.findItemByCategory()
            if (responseVideo.isSuccessful) {
                val itemList = responseVideo.body() ?: mutableListOf()
                list.addAll(itemList)
            } else {
                // null일 시 공백 리스트 생성
                _categoryVideo.value = mutableListOf()
                _categoryChannel.value = mutableListOf()
            }
            _categoryVideo.value = list.filterIsInstance<VideoItem>().toMutableList()
            _categoryChannel.value = list.filterIsInstance<ChannelItem>().toMutableList()
        }
    }
}

class SearchViewModelFactory(
    private val repository: ItemRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}