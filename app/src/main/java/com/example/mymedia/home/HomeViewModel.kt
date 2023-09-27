package com.example.mymedia.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mymedia.data.Data
import com.example.mymedia.data.ItemRepository
import com.example.mymedia.data.VideoItem
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: ItemRepository,
) : ViewModel() {

    private val _video = MutableLiveData<MutableList<VideoItem>>()

    val video: LiveData<MutableList<VideoItem>>
        get() = _video

    private val _channel = MutableLiveData<MutableList<VideoItem>>()

    val channel: LiveData<MutableList<VideoItem>>
        get() = _channel

    private val _most = MutableLiveData<MutableList<VideoItem>>()
    val most: LiveData<MutableList<VideoItem>>
        get() = _most

    init {
        _video.value = Data.getSearchData()
        _channel.value = Data.getSearchData()
        _most.value = Data.getSearchData()
    }

    fun showDetail(videoItem: VideoItem) {
//        val intent = Intent( , YourActivity::class.java)
//        context.startActivity(intent)
    }

    fun searchMostVideo() {
        viewModelScope.launch {
            val list = mutableListOf<VideoItem>()
            // Video
            val responseVideo = repository.findMostVideo()
            if (responseVideo.isSuccessful) {
                val itemList = responseVideo.body() ?: mutableListOf()
                list.addAll(itemList)
            } else {
                // null일 시 공백 리스트 생성
                _most.value = mutableListOf()
            }
            _most.value = list
        }
    }

    fun searchCategoryVideo() {
        viewModelScope.launch {
            val list = mutableListOf<VideoItem>()
            // Video
            val responseVideo = repository.findVideoByCategory()
            if (responseVideo.isSuccessful) {
                val itemList = responseVideo.body() ?: mutableListOf()
                list.addAll(itemList)
            } else {
                // null일 시 공백 리스트 생성
                _video.value = mutableListOf()
            }
            _video.value = list
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