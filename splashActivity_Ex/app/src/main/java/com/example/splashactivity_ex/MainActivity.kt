package com.example.splashactivityex

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.splashactivity_ex.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Splash 화면 로직 (로고나 배경 색 등)
        setContentView(R.layout.activity_splash)

        // 일정 시간 후 앱 종료 (Splash 화면을 2초 뒤에 바로 종료)
        Handler(Looper.getMainLooper()).postDelayed({
            finish() // Splash 화면 종료하고 앱 종료
        }, 2000) // 2초 후
    }
}
