package com.example.mymedia.data

import java.util.Date

data class Item(
    val id: Int,
    val title: String,
    val url: String,
    val datetime: Date,
    val thumbnail: String,
    var isFavorite: Boolean,
)
