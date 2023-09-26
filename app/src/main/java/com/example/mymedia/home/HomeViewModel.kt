package com.example.mymedia.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mymedia.data.Data
import com.example.mymedia.data.Item

class HomeViewModel(

) : ViewModel() {

    private val _video = MutableLiveData<MutableList<Item>>()

    val video: LiveData<MutableList<Item>>
        get() = _video

    private val _channel = MutableLiveData<MutableList<Item>>()

    val channel: LiveData<MutableList<Item>>
        get() = _channel

    init {
        _video.value = Data.getSearchData()
        _channel.value = Data.getSearchData()
    }

    fun showDetail(item: Item) {
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