package com.example.mymedia.data

import java.util.Date

data class VideoItem(
    val id: String,
    val title: String,
    val description : String,
    val datetime: Date,
    val thumbnail: String,
    var isFavorite: Boolean,
)

data class ChannelItem(
    val id: String,
    val title: String,
    val description : String,
    val datetime: Date,
    val thumbnail: String,
    var isFavorite: Boolean,
)


//id : String
//videoId : String
//channelId : String

//snippet : String
////publishedAt : datetime
////channelId : String
////title : String
////description : String
//// thumbnails : String
//////url : String