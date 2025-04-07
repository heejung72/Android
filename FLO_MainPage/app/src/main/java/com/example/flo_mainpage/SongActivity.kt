package com.example.flo_mainpage

import android.os.Bundle
import android.provider.CalendarContract.Instances
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_mainpage.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    // extends 대신 : (상속)

    // 변수 선언 방법 var : 변수, val : 상수
    lateinit var binding: ActivitySongBinding
    lateinit var song: Song
    lateinit var timer: Timer

    // activity 실행시 필수로 oncreate
    override fun onCreate(savedInstancesState: Bundle?) {
        super.onCreate(savedInstancesState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()

        setPlayer(song)

        binding.btnDown.setOnClickListener {
            finish()
        }
        binding.btnPlay.setOnClickListener {
            setPlayerStatus(false)
        }
        binding.btnPlayPause.setOnClickListener {
            setPlayerStatus(true)
        }

//        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
//            binding.tvSongTitle.text= intent.getStringExtra("title")
//            binding.tvArtist.text= intent.getStringExtra("singer")
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
    }

    private fun initSong() {
        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying", false)
            )
        }
        startTimer()
    }

    private fun setPlayer(song: Song) {
        binding.tvSongTitle.text = intent.getStringExtra("title")!!
        binding.tvArtist.text = intent.getStringExtra("singer")!!
        binding.songStartTime.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTime.text =
            String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.greystring.progress = (song.second * 1000 / song.playTime)

        setPlayerStatus(song.isPlaying)
    }

    private fun setPlayerStatus(isPlaying: Boolean) {
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if (isPlaying) {
            binding.btnPlay.visibility = View.VISIBLE
            binding.btnPlayPause.visibility = View.GONE
        } else {
            binding.btnPlay.visibility = View.GONE
            binding.btnPlayPause.visibility = View.VISIBLE
        }
    }

    private fun startTimer() {
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()
    }

    // Thread (timer class)
    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true) : Thread() {
        private var second: Int = 0
        private var mills: Float = 0f

        override fun run() {
            super.run()

            try {

                // timer 계속 진행
                while (true) {
                    if (second >= playTime) {
                        break
                    }
                    if (isPlaying) {
                        sleep(50)
                        mills += 50
                        runOnUiThread {
                            binding.greystring.progress = ((mills / playTime) * 100).toInt()
                        }

                        if (mills % 1000 == 0f) {
                            runOnUiThread {
                                binding.songStartTime.text =
                                    String.format("%02d:%02d", second / 60, second % 60)
                            }
                            second++
                        }
                    }
                }
            } catch (e: InterruptedException){
                Log.d("Song" , "Thread is dead ${e.message}")
            }

        }
    }

}