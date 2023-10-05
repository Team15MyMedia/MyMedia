package com.example.mymedia.mypage

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mymedia.data.ItemRepository
import com.example.mymedia.data.VideoItem
import com.example.mymedia.detail.DetailActivity
import com.example.mymedia.main.ContextProvider
import com.example.mymedia.main.MainSharedViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class MyPageViewModel(
    private val repository: ItemRepository,
    private val contextProvider: ContextProvider
) : ViewModel() {

    private val _favoriteVideo = MutableLiveData<MutableList<VideoItem>>()
    val favoriteVideo: LiveData<MutableList<VideoItem>>
        get() = _favoriteVideo

    private val _mainEvent = MutableLiveData<EventForMain>()
    val mainEvent: LiveData<EventForMain>
        get() = _mainEvent

    init {
        _favoriteVideo.value = loadDeviceData()
        Log.d("loadData", loadDeviceData().toString())
    }

    fun saveDeviceData(list: MutableList<VideoItem>) {
        val gson = Gson()
        val listAsJson = gson.toJson(list)

        Log.d("savelist", listAsJson)

        val sharedPreferences = contextProvider.getSharedPreferences()
        val editor = sharedPreferences.edit()
        editor.putString("favorite", listAsJson)
        editor.apply()
    }

    fun loadDeviceData(): MutableList<VideoItem> {
        val sharedPreferences = contextProvider.getSharedPreferences()
        val list = sharedPreferences.getString("favorite", "") ?: ""

        val gson = Gson()
        val itemType = object : TypeToken<MutableList<VideoItem>>() {}.type

        return gson.fromJson(list, itemType) ?: mutableListOf()
    }

    fun checkIsFavorite(item: VideoItem) {
        val curList = _favoriteVideo.value ?: return
        val curItem = curList.find { it.id == item.id }

        _mainEvent.value = EventForMain.CheckedItem(curItem)
    }

    fun updateItem(item: VideoItem) {
        val curList = _favoriteVideo.value ?: return
        val curItem = curList.find { it.id == item.id }

        if (item.isFavorite) {
            if (curItem == null) {
                curList.add(item)
            } else {
                return
            }
        } else {
            if (curItem == null) {
                return
            } else {
                curList.remove(curList.find { it.id == item.id })
            }
        }

        _favoriteVideo.value = curList
        saveDeviceData(curList)
    }
}

sealed interface EventForMain {
    data class CheckedItem(
        val item: VideoItem?
    ) : EventForMain
}

class MyPageViewModelFactory(
    private val repository: ItemRepository,
    private val contextProvider: ContextProvider,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyPageViewModel::class.java)) {
            return MyPageViewModel(repository, contextProvider) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}