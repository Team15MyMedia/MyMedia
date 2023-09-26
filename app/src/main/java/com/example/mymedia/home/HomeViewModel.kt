package com.example.mymedia.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mymedia.data.Data
import com.example.mymedia.data.VideoItem

class HomeViewModel(

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
}

class SearchViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel() as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}