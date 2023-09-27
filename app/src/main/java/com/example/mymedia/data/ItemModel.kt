package com.example.mymedia.data

import java.util.Date

sealed class MediaItem {
    abstract val id: String
    abstract val title: String
    abstract val description: String
    abstract val datetime: Date
    abstract val thumbnail: String
    abstract var isFavorite: Boolean
}

data class VideoItem(
    override val id: String,
    override val title: String,
    override val description: String,
    override val datetime: Date,
    override val thumbnail: String,
    override var isFavorite: Boolean
) : MediaItem()

data class ChannelItem(
    override val id: String,
    override val title: String,
    override val description: String,
    override val datetime: Date,
    override val thumbnail: String,
    override var isFavorite: Boolean
) : MediaItem()

data class Category(
    val id: String,
    val title: String,
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