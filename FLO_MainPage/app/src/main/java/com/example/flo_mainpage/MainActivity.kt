package com.example.flo_mainpage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_mainpage.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var songs: ArrayList<Song>
    private var nowPos = 0
    private lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the database and add dummy data
        initializeDB()

        // Set up UI and listeners
        initBottomNavigation()

        binding.miniPlayer.setOnClickListener {
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", songs[nowPos].id)
            editor.apply()

            // 현재 재생 중인 노래의 앨범 정보 가져오기
            val songDB = SongDatabase.getInstance(this)!!
            val allAlbums = songDB.albumDao().getAlbums()

            // 현재 노래의 앨범 찾기 (임시로 첫 번째 앨범 사용)
            val currentAlbum = allAlbums[0]  // 실제로는 song.albumIdx 등을 사용해 적절한 앨범을 찾아야 함

            // AlbumFragment로 전환
            val albumFragment = AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(currentAlbum)
                    putString("album", albumJson)
                }
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, albumFragment)
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        binding.miniPlayButton.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.miniPreviousButton.setOnClickListener {
            moveSong(-1)
        }

        binding.miniNextButton.setOnClickListener {
            moveSong(1)
        }
    }

    private fun initializeDB() {
        val songDB = SongDatabase.getInstance(this)!!

        // Add dummy songs if needed
        inputDummySongs()

        // Log the number of albums in the database
        val albums = songDB.albumDao().getAlbums()
        Log.d("MainActivity", "Albums in database: ${albums.size}")
        for (album in albums) {
            Log.d("MainActivity", "Album found: ${album.id} - ${album.title}")
        }
    }

    override fun onStart() {
        super.onStart()

        val songDB = SongDatabase.getInstance(this)!!
        songs = ArrayList(songDB.songDao().getSongs())

        Log.d("DB", "songs.size after load: ${songs.size}")

        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 1)

        nowPos = getPlayingSongPosition(songId)

        setMiniPlayer(songs[nowPos])
        startTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::timer.isInitialized) timer.interrupt()
    }

    private fun getPlayingSongPosition(songId: Int): Int {
        for (i in songs.indices) {
            if (songs[i].id == songId) {
                Log.d("DB", "Found song at position $i with id: ${songs[i].id}")
                return i
            }
        }
        Log.d("DB", "Song not found, returning 0")
        return 0
    }

    private fun moveSong(direction: Int) {
        val newPos = nowPos + direction
        if (newPos < 0 || newPos >= songs.size) return

        nowPos = newPos
        timer.interrupt()
        startTimer()
        setMiniPlayer(songs[nowPos])

        val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
        editor.putInt("songId", songs[nowPos].id)
        editor.apply()
    }

    private fun setMiniPlayer(song: Song) {
        Log.d("DB", "Setting mini player for song: ${song.title}")
        runOnUiThread {
            binding.miniSongTitle.text = song.title
            binding.miniArtistName.text = song.singer
            binding.songProgressMain.progress = (song.second * 100000) / song.playTime
            setPlayerStatus(song.isPlaying)
        }
    }

    private fun setPlayerStatus(isPlaying: Boolean) {
        songs[nowPos].isPlaying = isPlaying
        if (::timer.isInitialized) {
            timer.isPlaying = isPlaying
        }

        if (isPlaying) {
            binding.miniPlayButton.setImageResource(R.drawable.btn_miniplay_pause)
        } else {
            binding.miniPlayButton.setImageResource(R.drawable.btn_miniplay_mvplay)
        }
    }

    private fun startTimer() {
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.start()
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
                    true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, LookFragment())
                        .commitAllowingStateLoss()
                    true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, SearchFragment())
                        .commitAllowingStateLoss()
                    true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, LockerFragment())
                        .commitAllowingStateLoss()
                    true
                }

                else -> false
            }
        }
    }

    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()
        if (songs.isNotEmpty()) return

        songDB.songDao().insert(
            Song("Lilac", "아이유 (IU)", 0, 200, false, "music_lilac", R.drawable.img_album_exp2, false)
        )
        songDB.songDao().insert(
            Song("Flu", "아이유 (IU)", 0, 200, false, "music_flu", R.drawable.img_album_exp2, false)
        )
        songDB.songDao().insert(
            Song("Butter", "방탄소년단 (BTS)", 0, 190, false, "music_butter", R.drawable.img_album_exp, false)
        )
        songDB.songDao().insert(
            Song("Next Level", "에스파 (AESPA)", 0, 210, false, "music_next", R.drawable.img_album_exp3, false)
        )
        songDB.songDao().insert(
            Song("Boy with Luv", "방탄소년단 (BTS)", 0, 230, false, "music_lilac", R.drawable.img_album_exp4, false)
        )
        songDB.songDao().insert(
            Song("BBoom BBoom", "모모랜드 (MOMOLAND)", 0, 240, false, "music_bboom", R.drawable.img_album_exp5, false)
        )

        Log.d("DB", "Dummy songs inserted.")
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true) : Thread() {
        private var second: Int = 0
        private var mills: Float = 0f

        override fun run() {
            try {
                while (true) {
                    if (second >= playTime) break
                    if (isPlaying) {
                        sleep(50)
                        mills += 50
                        runOnUiThread {
                            binding.songProgressMain.progress =
                                ((mills / (playTime * 1000)) * 100000).toInt()
                        }
                        if (mills % 1000 == 0f) second++
                    }
                }
            } catch (e: InterruptedException) {
                Log.d("Song", "Thread is dead ${e.message}")
            }
        }
    }
}