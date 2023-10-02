package com.example.mymedia.detail

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mymedia.R
import com.example.mymedia.data.VideoItem
import com.bumptech.glide.Glide
import com.example.mymedia.home.getHighQualityThumbnailUrl

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val thumbnailImageView: ImageView = findViewById(R.id.thumbnailImageView)
        val titleTextView: TextView = findViewById(R.id.videoTitleTextView)
        val descriptionTextView: TextView = findViewById(R.id.videoDescriptionTextView)
        val backButton: ImageView = findViewById(R.id.imageButton2)

        backButton.setOnClickListener {
            onBackPressed()
        }

        val intent = intent
        val imageUrl = intent.getStringExtra("videoThumbnail")
        val title = intent.getStringExtra("videoTitle")
        val description = intent.getStringExtra("videoDescription")

        titleTextView.text = title
        descriptionTextView.text = description

        Glide.with(this)
            .load(getHighQualityThumbnailUrl(imageUrl))
            .into(thumbnailImageView)
    }
}
