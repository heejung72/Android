package com.example.flo_mainpage.Song

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flo_mainpage.Album.Album
import com.example.flo_mainpage.Album.AlbumDao
import com.example.flo_mainpage.Like
import com.example.flo_mainpage.R
import com.example.flo_mainpage.User
import com.example.flo_mainpage.UserDao

@Database(entities = [Song::class, Album::class, Like::class, User::class ], version = 5)
abstract class SongDatabase : RoomDatabase() {

    abstract fun songDao(): SongDao
    abstract fun albumDao(): AlbumDao
     abstract fun userDao(): UserDao

    companion object {
        private var instance: SongDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SongDatabase? {
            if (instance == null) {
                synchronized(SongDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "song-database"
                    ).fallbackToDestructiveMigration().allowMainThreadQueries().build()

                    // Check if albums exist and initialize if needed
                    instance?.let {
                        if (it.albumDao().getAlbums().isEmpty()) {
                            Log.d("DB", "Initializing albums")
                            initializeAlbums(it)
                        }
                    }
                }
            }

            return instance
        }

        // Initialize albums with dummy data
        private fun initializeAlbums(db: SongDatabase) {
            val albums = arrayListOf(
                Album(1, "Lilac", "아이유 (IU)", R.drawable.img_album_exp2),
                Album(2, "Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp),
                Album(3, "Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3),
                Album(4, "Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp4),
                Album(5, "BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5)
            )


            for (album in albums) {
                db.albumDao().insert(album)
            }
            Log.d("DB", "Albums initialized: ${albums.size} albums added")
        }
    }
}