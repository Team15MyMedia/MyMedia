package com.example.mymedia.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mymedia.data.Data
import com.example.mymedia.data.Item

class HomeViewModel() : ViewModel() {

    private val _video = MutableLiveData<MutableList<Item>>()

    val search: LiveData<MutableList<Item>>
        get() = _video

    init {
        _video.value = Data.getSearchData()
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