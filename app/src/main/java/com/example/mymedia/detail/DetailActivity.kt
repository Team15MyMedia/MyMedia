package com.example.mymedia.detail
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.mymedia.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class DetailActivity : AppCompatActivity() {

    private var videoId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val rootLayout = findViewById<ConstraintLayout>(R.id.detailRootLayout)
        val youtubePlayerView: YouTubePlayerView = findViewById(R.id.thumbnailImageView)
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

        videoId = extractVideoIdFromThumbnailUrl(imageUrl)

        lifecycle.addObserver(youtubePlayerView)
        youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {

                videoId?.let { youTubePlayer.loadVideo(it, 0f) }
            }
        })

        val shareButton: ImageView = findViewById(R.id.imageButton4)
        shareButton.setOnClickListener {
            shareVideo(videoId)
        }
    }

    private fun shareVideo(videoId: String?) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"

        val videoUrl = "https://www.youtube.com/watch?v=$videoId"
        intent.putExtra(Intent.EXTRA_TEXT, videoUrl)

        val shareIntent = Intent.createChooser(intent, "비디오 공유")
        startActivity(shareIntent)
    }

    private fun extractVideoIdFromThumbnailUrl(thumbnailUrl: String?): String {
        val pattern = "https://i.ytimg.com/vi/(.*?)/maxresdefault.jpg".toRegex()
        val matchResult = pattern.find(thumbnailUrl ?: "")
        return matchResult?.groupValues?.get(1) ?: ""
    }
}
