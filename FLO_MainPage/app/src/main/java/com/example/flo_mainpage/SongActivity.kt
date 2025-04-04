package com.example.flo_mainpage

import android.os.Bundle
import android.provider.CalendarContract.Instances
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_mainpage.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    // extends 대신 : (상속)

    // 변수 선언 방법 var : 변수, val : 상수
    lateinit var binding: ActivitySongBinding

    // activity 실행시 필수로 oncreate
    override fun onCreate(savedInstancesState: Bundle?) {
        super.onCreate(savedInstancesState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDown.setOnClickListener {
            finish()
        }
        binding.btnPlay.setOnClickListener {
            setPlayerStatus(false)
        }
        binding.btnPlayPause.setOnClickListener{
            setPlayerStatus(true)
        }

        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
            binding.tvSongTitle.text= intent.getStringExtra("title")
            binding.tvArtist.text= intent.getStringExtra("singer")
        }
    }

    fun setPlayerStatus(isPlaying: Boolean) {
        if (isPlaying) {
            binding.btnPlay.visibility = View.VISIBLE
            binding.btnPlayPause.visibility = View.GONE
        } else {
            binding.btnPlay.visibility = View.GONE
            binding.btnPlayPause.visibility = View.VISIBLE
        }
    }
}
