package com.example.flo_mainpage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // 특정 화면으로 이동하거나 처리를 할 수 있습니다.
                    true
                }
                R.id.navigation_favorites -> {
                    // 즐겨찾기 화면 처리
                    true
                }
                R.id.navigation_search -> {
                    // 검색 화면 처리
                    true
                }
                R.id.navigation_library -> {
                    // 보관함 화면 처리
                    true
                }
                else -> false
            }
        }
    }
}
