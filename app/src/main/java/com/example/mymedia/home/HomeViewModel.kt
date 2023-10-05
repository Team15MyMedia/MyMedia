package com.example.mymedia.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mymedia.data.Category
import com.example.mymedia.data.ChannelItem
import com.example.mymedia.data.ItemRepository
import com.example.mymedia.data.MediaItem
import com.example.mymedia.data.VideoItem
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: ItemRepository,
) : ViewModel() {

    private val apiSavingMode = false

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

    private val _mostLive = MutableLiveData<MutableList<VideoItem>>()
    val mostLive: LiveData<MutableList<VideoItem>>
        get() = _mostLive

    val curCategory: LiveData<Int>
        get() = _curCategory

    init {
        _curCategory.value = 0

        if (!apiSavingMode) {
            getCategoryList()
            searchMostVideo()

        }
        searchMostLiveVideo()
    }

    fun showByYoutube(videoItem: VideoItem, context: Context) {
        val videoUrl = "https://www.youtube.com/watch?v=${videoItem.id}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
        intent.setPackage("com.google.android.youtube")
        context.startActivity(intent)
    }

    private fun searchMostVideo() {
        if (!apiSavingMode) {
            viewModelScope.launch {
                val list = mutableListOf<MediaItem>()
                // Video
                val responseVideo = repository.findMostVideo()
                if (responseVideo.isSuccessful) {
                    val itemList = responseVideo.body() ?: mutableListOf()
                    list.addAll(itemList)
                } else {
                    // null일 시 공백 리스트 생성입니다.
                    _most.value = mutableListOf()
                }
                _most.value = list.filterIsInstance<VideoItem>().toMutableList()
            }
        }
    }

    private fun searchMostLiveVideo() {
//        if (!apiSavingMode) {
        viewModelScope.launch {
            val list = mutableListOf<MediaItem>()
            // Video
            val responseVideo = repository.findMostLiveVideo()
            if (responseVideo.isSuccessful) {
                val itemList = responseVideo.body() ?: mutableListOf()
                list.addAll(itemList)
            } else {
                // null일 시 공백 리스트 생성
                _mostLive.value = mutableListOf()
            }
            _mostLive.value = list.filterIsInstance<VideoItem>().toMutableList()
            Log.d("init", "${_mostLive.value}")
        }
//        }
    }

    fun searchByCategory(id: String) {
        if (!apiSavingMode) {
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
    }

    private fun searchChannelByID() {
        if (!apiSavingMode) {
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
    }

    private fun getCategoryList() {
        if (!apiSavingMode) {
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
    }

    fun setCurCategory(position: Int) {
        _curCategory.value = position
    }

    fun reorganizeOrder(type: String) {
        val currentData = when (type) {
            "most" -> {
                _most.value?.toMutableList() ?: mutableListOf()
            }

            "channel" -> {
                _categoryChannel.value?.toMutableList() ?: mutableListOf()
            }

            "category" -> {
                _categoryVideo.value?.toMutableList() ?: mutableListOf()
            }

            else -> return
        }

        if (currentData.size > 1) {
            val firstHalf = currentData.subList(0, currentData.size / 2)
            val secondHalf = currentData.subList(currentData.size / 2, currentData.size)

            val newData = mutableListOf<MediaItem>()
            newData.addAll(secondHalf)
            newData.addAll(firstHalf)

            when (type) {
                "most" -> {
                    _most.value = newData.filterIsInstance<VideoItem>().toMutableList()
                }

                "channel" -> {
                    _categoryChannel.value = newData.filterIsInstance<ChannelItem>().toMutableList()
                }

                "category" -> {
                    _categoryVideo.value = newData.filterIsInstance<VideoItem>().toMutableList()
                }

                else -> return
            }
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