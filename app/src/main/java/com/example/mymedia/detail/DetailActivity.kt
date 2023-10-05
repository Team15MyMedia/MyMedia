package com.example.mymedia.detail

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.mymedia.R
import com.example.mymedia.data.VideoItem
import com.example.mymedia.databinding.ActivityDetailBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.text.SimpleDateFormat
import java.util.Date

class DetailActivity : AppCompatActivity() {

    private var videoItem: VideoItem? = null
    private var videoId: String? = null
    private var isFavorite: Boolean = false

    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val rootLayout = findViewById<ConstraintLayout>(R.id.detailRootLayout)
        val youtubePlayerView: YouTubePlayerView = findViewById(R.id.thumbnailImageView)
        val titleTextView: TextView = findViewById(R.id.videoTitleTextView)
        val descriptionTextView: TextView = findViewById(R.id.videoDescriptionTextView)
        val backButton: ImageView = findViewById(R.id.imageButton2)
        val enterAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_up)

        rootLayout.startAnimation(enterAnimation)

        val intent = intent

        val imageUrl = intent.getStringExtra("videoThumbnail")
        val title = intent.getStringExtra("videoTitle")
        val description = intent.getStringExtra("videoDescription")
        val dateString = intent.getStringExtra("videoDatetime") ?: ""
        val dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        val date = stringToDate(dateString, dateFormat) ?: Date()
        val nextPage = intent.getStringExtra("videoNextPage") ?: ""
        val channelId = intent.getStringExtra("videoChannelId") ?: ""

        titleTextView.text = title
        descriptionTextView.text = description

        videoId = extractVideoIdFromThumbnailUrl(imageUrl)
        isFavorite = intent.getBooleanExtra("isFavorite", false)

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

        // 좋아요 버튼 표시
        if (isFavorite) {
            binding.imageButton3.setImageResource(R.drawable.ic_sel_like_bt)
        }

        binding.imageButton3.setOnClickListener {
            isFavorite = !isFavorite

            if (isFavorite) {
                binding.imageButton3.setImageResource(R.drawable.ic_sel_like_bt)
            } else {
                binding.imageButton3.setImageResource(R.drawable.ic_unsel_like_bt)
            }

            videoItem = VideoItem(
                videoId!!,
                title!!,
                description!!,
                date,
                imageUrl!!,
                isFavorite,
                nextPage,
                channelId
            )
        }

        videoItem = VideoItem(
            videoId!!,
            title!!,
            description!!,
            date,
            imageUrl!!,
            isFavorite,
            nextPage,
            channelId
        )

        backButton.setOnClickListener {
            val exitAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_down)
            rootLayout.startAnimation(exitAnimation)

            videoItem = VideoItem(
                videoId!!,
                title,
                description,
                date,
                imageUrl,
                isFavorite,
                nextPage,
                channelId
            )

            onBackPressed()
            finish()
        }
    }

    override fun onBackPressed() {
        val resultIntent = Intent()

        resultIntent.putExtra("id", videoItem!!.id)
        resultIntent.putExtra("title", videoItem!!.title)
        resultIntent.putExtra("description", videoItem!!.description)
        resultIntent.putExtra("datetime", videoItem!!.datetime)
        resultIntent.putExtra("thumbnail", videoItem!!.thumbnail)
        resultIntent.putExtra("isFavorite", videoItem!!.isFavorite)
        resultIntent.putExtra("nextPage", videoItem!!.nextPage)
        resultIntent.putExtra("channelId", videoItem!!.channelId)

        setResult(RESULT_OK, resultIntent)
        finish() // 현재 액티비티 종료
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

    private fun stringToDate(dateString: String, dateFormat: String): Date? {
        return try {
            val sdf = SimpleDateFormat(dateFormat)
            sdf.parse(dateString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
