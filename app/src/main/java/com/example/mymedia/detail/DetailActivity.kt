package com.example.mymedia.detail
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.mymedia.R
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

        val shareButton: ImageView = findViewById(R.id.imageButton4)
        shareButton.setOnClickListener {
            shareVideo(imageUrl)
        }
    }
    private fun shareVideo(thumbnailUrl: String?) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        val videoId = extractVideoIdFromThumbnailUrl(thumbnailUrl)
        val videoUrl = "https://www.youtube.com/watch?v=$videoId"
        intent.putExtra(Intent.EXTRA_TEXT, videoUrl)
        val shareIntent = Intent.createChooser(intent, "비디오 공유")
        startActivity(shareIntent)
    }
    private fun extractVideoIdFromThumbnailUrl(thumbnailUrl: String?): String {
        val pattern = "https://i.ytimg.com/vi/(.*?)/default.jpg".toRegex()
        val matchResult = pattern.find(thumbnailUrl ?: "")
        return matchResult?.groupValues?.get(1) ?: ""
    }
}