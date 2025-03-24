package com.example.bottomnav_ex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // BottomNavigationView 설정
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // 홈 화면 처리
                    true
                }
                R.id.nav_pen -> {
                    // 작성 화면 처리
                    true
                }
                R.id.nav_calendar -> {
                    // 달력 화면 처리
                    true
                }
                R.id.nav_settings -> {
                    // 설정 화면 처리
                    true
                }
                else -> false
            }
        }
    }
}
