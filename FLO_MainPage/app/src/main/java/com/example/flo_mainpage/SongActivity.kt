package com.example.flo_mainpage

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_mainpage.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    lateinit var binding: ActivitySongBinding
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null

    private lateinit var songDB: SongDatabase
    private var songs = arrayListOf<Song>()
    private var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())

        initSong()
        initClickListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onPause() {
        super.onPause()
        songs[nowPos].second = ((binding.greystring.progress * songs[nowPos].playTime) / 100) / 1000
        songs[nowPos].isPlaying = false
        setPlayerStatus(false)

        getSharedPreferences("song", MODE_PRIVATE).edit()
            .putInt("songId", songs[nowPos].id)
            .apply()
    }

    private fun initSong() {
        val songId = getSharedPreferences("song", MODE_PRIVATE).getInt("songId", 0)
        nowPos = getPlayingSongPosition(songId)

        startTimer()
        setPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId: Int): Int {
        for (i in songs.indices) {
            if (songs[i].id == songId) return i
        }
        return 0
    }

    private fun initClickListener() {
        binding.btnDown.setOnClickListener {
            finish()
        }

        binding.btnPlay.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.btnPlayPause.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.btnReplay.setOnClickListener {
            setPlayerStatus(true)
            restartTimer()
        }

        binding.btnPrev.setOnClickListener {
            moveSong(-1)
        }

        binding.btnNext.setOnClickListener {
            moveSong(+1)
        }

        binding.btnLike.setOnClickListener {
            toggleLike()
        }
    }

    private fun moveSong(direction: Int) {
        val newPos = nowPos + direction

        if (newPos !in songs.indices) {
            Toast.makeText(this, "더 이상 곡이 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null

        nowPos = newPos
        startTimer()
        setPlayer(songs[nowPos])
    }

    private fun toggleLike() {
        val song = songs[nowPos]
        song.isLike = !song.isLike

        if (song.isLike) {
            binding.btnLike.setImageResource(R.drawable.ic_my_like_on)
            Toast.makeText(this, "좋아요 추가", Toast.LENGTH_SHORT).show()
        } else {
            binding.btnLike.setImageResource(R.drawable.ic_my_like_off)
            Toast.makeText(this, "좋아요 제거", Toast.LENGTH_SHORT).show()
        }

        songDB.songDao().update(song)
    }

    private fun setPlayer(song: Song) {
        binding.tvSongTitle.text = song.title
        binding.tvArtist.text = song.singer
        binding.songStartTime.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTime.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.greystring.progress = (song.second * 1000 / song.playTime)

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)
        setPlayerStatus(song.isPlaying)

        // 좋아요 UI 초기 상태 반영
        binding.btnLike.setImageResource(
            if (song.isLike) R.drawable.ic_my_like_on
            else R.drawable.ic_my_like_off
        )
    }

    private fun setPlayerStatus(isPlaying: Boolean) {
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if (isPlaying) {
            mediaPlayer?.start()
            binding.btnPlay.visibility = View.GONE
            binding.btnPlayPause.visibility = View.VISIBLE
        } else {
            mediaPlayer?.pause()
            binding.btnPlay.visibility = View.VISIBLE
            binding.btnPlayPause.visibility = View.GONE
        }
    }

    private fun startTimer() {
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.start()
    }

    private fun restartTimer() {
        if (::timer.isInitialized) {
            timer.interrupt()
        }
        startTimer()
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true) : Thread() {
        private var second = songs[nowPos].second
        private var mills: Float = (second * 1000).toFloat()

        override fun run() {
            try {
                while (second < playTime) {
                    if (isPlaying) {
                        sleep(50)
                        mills += 50
                        runOnUiThread {
                            binding.greystring.progress = ((mills / playTime / 1000) * 100).toInt()
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
            } catch (e: InterruptedException) {
                Log.d("Song", "Thread 종료됨: ${e.message}")
            }
        }
    }
}
