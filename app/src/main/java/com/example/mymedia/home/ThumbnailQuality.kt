package com.example.mymedia.home

fun getHighQualityThumbnailUrl(thumbnailUrl: String?): String? {
    return thumbnailUrl?.replace("/default.jpg", "/maxresdefault.jpg")
}