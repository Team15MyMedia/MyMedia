package com.example.mymedia.data

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("kind")
    val kind: String?,

    @SerializedName("items")
    val items: List<T>?,

    @SerializedName("nextPageToken")
    val nextPageToken: String?,

    @SerializedName("regionCode")
    val regionCode: String?,
)

data class SearchItem(
    @SerializedName("kind")
    val kind: String?,

    @SerializedName("id")
    val id: Id?,

    @SerializedName("snippet")
    val snippet: Snippet?
)

data class NoneSearchItem(
    @SerializedName("kind")
    val kind: String?,

    @SerializedName("id")
    val id: String?,

    @SerializedName("snippet")
    val snippet: Snippet?,

    @SerializedName("contentDetails")
    val contentDetails: ContentDetails?
)

data class ContentDetails(
    @SerializedName("duration")
    val duration: String?,
)

data class Id(
    @SerializedName("kind")
    val kind: String?,

    @SerializedName("videoId")
    val videoId: String?
)

data class Snippet(
    @SerializedName("publishedAt")
    val publishedAt: String?,

    @SerializedName("channelId")
    val channelId: String?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("thumbnails")
    val thumbnails: Thumbnails?,

    @SerializedName("channelTitle")
    val channelTitle: String?,

    @SerializedName("liveBroadcastContent")
    val liveBroadcastContent: String?
)

data class Thumbnails(
    @SerializedName("default")
    val default: Thumbnail?,

    @SerializedName("medium")
    val medium: Thumbnail?,

    @SerializedName("high")
    val high: Thumbnail?
)

data class Thumbnail(
    @SerializedName("url")
    val url: String?,

    @SerializedName("width")
    val width: Int?,

    @SerializedName("height")
    val height: Int?
)
