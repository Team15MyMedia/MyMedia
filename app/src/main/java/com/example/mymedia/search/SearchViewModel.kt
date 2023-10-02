package com.example.mymedia.search

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
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
import com.example.mymedia.home.HomeViewModel
import com.example.mymedia.main.MainActivity
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: ItemRepository,
) : ViewModel() {

    private val apiSavingMode = Data.apiSavingMode

    private val _searchvideo = MutableLiveData<MutableList<VideoItem>>()
    val searchvideo: LiveData<MutableList<VideoItem>>
        get() = _searchvideo

    private val _most = MutableLiveData<MutableList<VideoItem>>()
    val most: LiveData<MutableList<VideoItem>>
        get() = _most

    private val _searchChannel = MutableLiveData<MutableList<ChannelItem>>()
    val searchChannel: LiveData<MutableList<ChannelItem>>
        get() = _searchChannel

    init {
        _most.value = Data.getSearchData()
    }

    fun searchMostVideo() {
        if (!apiSavingMode) {
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
    }

    fun searchVideo(text: String) {
        if (!apiSavingMode) {
            viewModelScope.launch {
                val list = mutableListOf<MediaItem>()
                // Video
                val responseVideo = repository.searchVideo(text)
                if (responseVideo.isSuccessful) {
                    var itemList = responseVideo.body() ?: mutableListOf()

                    if (itemList.isNullOrEmpty()) {
                        Toast.makeText(
                            MainActivity.getContext(),
                            "비디오 검색 결과가 없습니다!",
                            Toast.LENGTH_SHORT
                        ).show()
                        itemList = mutableListOf()
                    }

                    list.addAll(itemList)

                    Log.d("videolist", list.toString())
                } else {
                    // null일 시 공백 리스트 생성
                    _searchvideo.value = mutableListOf()
                }
                _searchvideo.value = list.filterIsInstance<VideoItem>().toMutableList()
            }
        }
    }

    fun searchChannel(text: String) {
        if (!apiSavingMode) {
            viewModelScope.launch {
                val list = mutableListOf<MediaItem>()
                // Channel
                val responseVideo = repository.searchChannel(text)
                if (responseVideo.isSuccessful) {
                    var itemList = responseVideo.body()

                    if (itemList.isNullOrEmpty()) {
                        Toast.makeText(
                            MainActivity.getContext(),
                            "채널 검색 결과가 없습니다!!",
                            Toast.LENGTH_SHORT
                        ).show()
                        itemList = mutableListOf()
                    }

                    list.addAll(itemList)
                } else {
                    _searchChannel.value = mutableListOf()
                }
                _searchChannel.value = list.filterIsInstance<ChannelItem>().toMutableList()
            }
        }
    }

    fun showDetail(videoItem: VideoItem, context: Context) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra("videoThumbnail", videoItem.thumbnail)
        intent.putExtra("videoTitle", videoItem.title)
        intent.putExtra("videoDescription", videoItem.description)
        context.startActivity(intent)
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
