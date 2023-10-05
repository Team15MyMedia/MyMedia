package com.example.mymedia.search

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mymedia.data.ChannelItem
import com.example.mymedia.data.ItemRepository
import com.example.mymedia.data.MediaItem
import com.example.mymedia.data.VideoItem
import com.example.mymedia.detail.DetailActivity
import com.example.mymedia.main.MainActivity
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: ItemRepository,
) : ViewModel() {

    private val apiSavingMode = false

    private val _searchvideo = MutableLiveData<MutableList<VideoItem>>()
    val searchvideo: LiveData<MutableList<VideoItem>>
        get() = _searchvideo

    private val _most = MutableLiveData<MutableList<VideoItem>>()
    val most: LiveData<MutableList<VideoItem>>
        get() = _most

    private val _searchChannel = MutableLiveData<MutableList<ChannelItem>>()
    val searchChannel: LiveData<MutableList<ChannelItem>>
        get() = _searchChannel

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    val list = mutableListOf<MediaItem>()
    val channelList = mutableListOf<MediaItem>()

    var curPageCnt: String = ""
    var searchText: String = ""
    var isVideoSearchFinished = false
    var isChannelSearchFinished = false

    init {
        _most.value = mutableListOf()
    }

    fun searchMostVideo() {
        if (true) {
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

    fun searchVideo(text: String, page:String) {
        if (!apiSavingMode) {
            viewModelScope.launch {

                // Video
                val responseVideo = repository.searchVideo(text, page)
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
                    curPageCnt = itemList[itemList.size -1].nextPage
                    list.addAll(itemList)
                } else {
                    // null일 시 공백 리스트 생성
                    val emptyList: MutableList<MediaItem> = mutableListOf()
                    list.addAll(emptyList)
                }
                isVideoSearchFinished = true
                if (isVideoSearchFinished) {
                    _searchvideo.value = list.filterIsInstance<VideoItem>().toMutableList()
                    _isLoading.value = false
                }
            }
        }
    }

    fun searchChannel(text: String, page:String) {
        if (!apiSavingMode) {
            viewModelScope.launch {

                // Channel
                val responseVideo = repository.searchChannel(text, page)
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
                    curPageCnt = itemList[itemList.size -1].nextPage
                    channelList.addAll(itemList)
                } else {
                    val emptyList: MutableList<MediaItem> = mutableListOf()
                    channelList.addAll(emptyList)
                }
                isChannelSearchFinished = true
                if (isChannelSearchFinished) {
                    _searchChannel.value = channelList.filterIsInstance<ChannelItem>().toMutableList()
                    _isLoading.value = false
                }
            }
        }
    }


    fun showDetail(videoItem: VideoItem, context: Context) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra("videoThumbnail", videoItem.thumbnail.replace("/default.jpg", "/maxresdefault.jpg"))
        intent.putExtra("videoTitle", videoItem.title)
        intent.putExtra("videoDescription", videoItem.description)
        context.startActivity(intent)
    }

    fun doSearch(text: String) {

        _isLoading.value = true
        isVideoSearchFinished = false

        searchVideo(text, curPageCnt)
    }

    fun doChannelSearch(text: String) {

        _isLoading.value = true
        isChannelSearchFinished = false

        searchChannel(text, curPageCnt)
    }

}

class SearchViewModelFactory(
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
