package com.example.mymedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.mymedia.main.MainActivity

class SplashActivity : AppCompatActivity() {
    private val SPLASH_DURATION: Long = 3000 // 3초 동안 스플래시 화면 표시

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        // 일정 시간 후에 메인 화면으로 전환
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_DURATION)
    }
}