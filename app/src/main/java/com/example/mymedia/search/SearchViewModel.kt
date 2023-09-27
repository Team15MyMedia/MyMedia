package com.example.mymedia.search

import android.util.Log
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
import com.example.mymedia.home.HomeViewModel
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: ItemRepository,
) : ViewModel() {

    private val _searchvideo = MutableLiveData<MutableList<VideoItem>>()
    val searchvideo: LiveData<MutableList<VideoItem>>
        get() = _searchvideo

    private val _most = MutableLiveData<MutableList<VideoItem>>()
    val most: LiveData<MutableList<VideoItem>>
        get() = _most

    private val _categoryChannel = MutableLiveData<MutableList<ChannelItem>>()
    val categoryChannel: LiveData<MutableList<ChannelItem>>
        get() = _categoryChannel

//    init {
//        _searchvideo.value = Data.getSearchData()
//    }

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

    fun searchVideo(text: String) {
        viewModelScope.launch {
            val list = mutableListOf<MediaItem>()
            // Video
            val responseVideo = repository.searchVideo(text)
            if (responseVideo.isSuccessful) {
                val itemList = responseVideo.body() ?: mutableListOf()
                list.addAll(itemList)
            } else {
                // null일 시 공백 리스트 생성
                _searchvideo.value = mutableListOf()
            }
            _searchvideo.value = list.filterIsInstance<VideoItem>().toMutableList()
            Log.d("searchvideo", _searchvideo.value.toString())
        }
    }

    fun searchByCategory(text: String) {
        viewModelScope.launch {
            val list = mutableListOf<MediaItem>()
            // Video
            val responseVideo = repository.searchChannel(text)
            if (responseVideo.isSuccessful) {
                val itemList = responseVideo.body() ?: mutableListOf()
                list.addAll(itemList)
            } else {
                _categoryChannel.value = mutableListOf()
            }
            //수정
            _categoryChannel.value = list.filterIsInstance<ChannelItem>().toMutableList()
        }
    }

}

class SearchViewModelFactory2(
    private val repository: ItemRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}