package com.example.flo_mainpage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo_mainpage.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private lateinit var song: Song
    private lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // findviewbyId 은 null exception을 고려 안함
        // binding 써

        song =
            Song(binding.miniSongTitle.text.toString(), binding.miniArtistName.text.toString(), 0, 60, false)

        binding.miniPlayer.setOnClickListener {
            // startActivity(Intent(this, SongActivity::class.java))
            // Intent로 전달하기
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            intent.putExtra("second", song.second)
            intent.putExtra("playTime", song.playTime)
            intent.putExtra("isPlaying", song.isPlaying)
            startActivity(intent)
        }
        initBottomNavigation()

        // data 가 잘 저장되었는지 확인하기 (log 사용)
        Log.d("Song", song.title + song.singer)

        binding.miniPlayButton.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.miniPreviousButton.setOnClickListener {
            setPlayerStatus(true)
            restartTimer()
        }

        startTimer()

    }

    override fun onDestroy() {
        super.onDestroy()
        if (::timer.isInitialized) {
            timer.interrupt()
        }
    }

    private fun setPlayerStatus(isPlaying: Boolean) {
            song.isPlaying = isPlaying

            if (::timer.isInitialized) {
                timer.isPlaying = isPlaying
            }

            if (isPlaying) {
                binding.miniPlayButton.setImageResource(R.drawable.btn_miniplay_pause) // 필요한 이미지 리소스로 변경
            } else {
                binding.miniPlayButton.setImageResource(R.drawable.btn_miniplay_mvplay) // 필요한 이미지 리소스로 변경
            }
        }

        private fun startTimer() {
            timer = Timer(song.playTime, song.isPlaying)
            timer.start()
        }

        private fun restartTimer() {
            if (::timer.isInitialized) {
                timer.interrupt()
            }
            startTimer()
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
                                binding.songProgressMain.progress = ((mills / (playTime * 1000)) * 100000).toInt()
                            }

                            if (mills % 1000 == 0f) {
                                second++
                                // TextView가 없는 경우 주석 처리
                                // runOnUiThread {
                                //    binding.songStartTime.text = String.format("%02d:%02d", second / 60, second % 60)
                                // }
                            }
                        }
                    }
                } catch (e: InterruptedException) {
                    Log.d("Song", "Thread is dead ${e.message}")
                }
            }
        }

    private fun initBottomNavigation() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment()).commitAllowingStateLoss()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                else -> return@setOnItemSelectedListener false
            }
        }
    }
}