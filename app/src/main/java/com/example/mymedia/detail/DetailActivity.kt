package com.example.mymedia.detail

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.mymedia.R
import com.example.mymedia.data.VideoItem
import com.bumptech.glide.Glide
import com.example.mymedia.home.getHighQualityThumbnailUrl

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val rootLayout = findViewById<ConstraintLayout>(R.id.detailRootLayout)
        val thumbnailImageView: ImageView = findViewById(R.id.thumbnailImageView)
        val titleTextView: TextView = findViewById(R.id.videoTitleTextView)
        val descriptionTextView: TextView = findViewById(R.id.videoDescriptionTextView)
        val backButton: ImageView = findViewById(R.id.imageButton2)

        val enterAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_up)
        rootLayout.startAnimation(enterAnimation)

        backButton.setOnClickListener {
            val exitAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_down)
            rootLayout.startAnimation(exitAnimation)
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
