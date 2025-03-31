package com.example.flo_mainpage

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private var isPlaying = false // 음악 재생 상태 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupMiniPlayer() // 미니 플레이어 설정

        val navView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.navigation_home, R.id.navigation_favorites, R.id.navigation_search, R.id.navigation_library -> return@setOnItemSelectedListener true
                else -> return@setOnItemSelectedListener false
            }
        }

        // 노래 제목 및 아티스트 이름 클릭 시 AlbumFragment 열기
        findViewById<View>(R.id.mini_song_title).setOnClickListener { openAlbumFragment() }
        findViewById<View>(R.id.mini_artist_name).setOnClickListener { openAlbumFragment() }

        // 앨범 이미지 클릭 시 SongActivity 실행
        val albumImage = findViewById<ImageView>(R.id.imageView40)
        albumImage.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupMiniPlayer() {
        val playButton = findViewById<ImageView>(R.id.mini_play_button)

        playButton.setOnClickListener {
            if (isPlaying) {
                playButton.setImageResource(R.drawable.btn_miniplay_mvplay)
                // TODO: 음악 일시정지 기능 추가
                isPlaying = false
            } else {
                playButton.setImageResource(R.drawable.btn_miniplay_pause)
                // TODO: 음악 재생 기능 추가
                isPlaying = true
            }
        }
    }

    private fun openAlbumFragment() {
        val albumFragment: Fragment = AlbumFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, albumFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
