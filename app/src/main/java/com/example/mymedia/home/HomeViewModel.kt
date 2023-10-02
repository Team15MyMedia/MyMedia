package com.example.mymedia.home

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mymedia.data.Category
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

    private val _categoryList = MutableLiveData<MutableList<Category>>()
    val categoryList: LiveData<MutableList<Category>>
        get() = _categoryList

    private val _curCategory = MutableLiveData<Int>()
    val curCategory: LiveData<Int>
        get() = _curCategory

    init {
        _categoryVideo.value = Data.getMediaData().filterIsInstance<VideoItem>().toMutableList()
        _categoryChannel.value = Data.getMediaData().filterIsInstance<ChannelItem>().toMutableList()

        getCategoryList()
        _curCategory.value = 0

        // API 절약을 위한 주석
//        searchMostVideo()
    }

    fun showDetail(videoItem: VideoItem, context: Context) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra("videoThumbnail", videoItem.thumbnail)
        intent.putExtra("videoTitle", videoItem.title)
        intent.putExtra("videoDescription", videoItem.description)
        context.startActivity(intent)
    }

    private fun searchMostVideo() {
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

    fun searchByCategory(id: String) {
        viewModelScope.launch {
            val list = mutableListOf<MediaItem>()
            // Video
            val responseVideo = repository.findItemByCategory(id)
            if (responseVideo.isSuccessful) {
                val itemList = responseVideo.body() ?: mutableListOf()
                list.addAll(itemList)
            } else {
                // null일 시 공백 리스트 생성
                _categoryVideo.value = mutableListOf()
            }
            _categoryVideo.value = list.filterIsInstance<VideoItem>().toMutableList()
            searchChannelByID()
        }
    }

    private fun searchChannelByID() {
        viewModelScope.launch {
            val list = mutableListOf<MediaItem>()
            val channelIdList = _categoryVideo.value?.map {
                it.channelId
            } ?: mutableListOf()
            // Channel
            for (id in channelIdList) {
                val responseChannel = repository.findChannelByID(id)

                if (responseChannel.isSuccessful) {
                    val item = responseChannel.body() ?: continue
                    list.add(item)
                }
            }
            _categoryChannel.value = list.filterIsInstance<ChannelItem>().toMutableList()
        }
    }

    private fun getCategoryList() {
        viewModelScope.launch {
            val list = mutableListOf<Category>()
            // Video
            val responseCategoryList = repository.findCategoryList()
            if (responseCategoryList.isSuccessful) {
                val categoryList = responseCategoryList.body() ?: mutableListOf()
                list.addAll(categoryList)
            } else {
                // null일 시 공백 리스트 생성
                _categoryList.value = mutableListOf()
            }
            _categoryList.value = list
        }
    }

    fun setCurCategory(position: Int) {
        _curCategory.value = position
    }

    fun reorganizeOrder() {
        val currentData = _categoryVideo.value

        if (currentData != null && currentData.size > 1) {
            val firstItem = currentData[0]
            val newData = currentData.subList(1, currentData.size).toMutableList()
            newData.add(firstItem)
            _categoryVideo.value = newData
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