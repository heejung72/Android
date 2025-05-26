package com.example.flo_mainpage.Album

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo_mainpage.Home.HomeFragment
import com.example.flo_mainpage.Like
import com.example.flo_mainpage.MainActivity
import com.example.flo_mainpage.R
import com.example.flo_mainpage.Song.SongDatabase
import com.example.flo_mainpage.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlbumFragment : Fragment() {
    private lateinit var binding: FragmentAlbumBinding

    private val information = arrayListOf("수록곡", "상세정보", "영상")

    private fun isLikedAlbum(albumId: Int): Boolean {
        val songDB = SongDatabase.Companion.getInstance(requireContext())!!
        val userId = getJwt() // 현재 사용자의 userId 가져오기

        // userId와 albumId를 기반으로 like 여부 확인
        val likeId: Int? = songDB.albumDao().isLikedAlbum(userId, albumId)

        // likeId가 null이 아니면 이미 좋아요를 누른 상태
        return likeId != null
    }

    private var isLiked: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        val albumData = arguments?.getString("album")
        if (albumData != null) {
            val gson = Gson()
            val album = gson.fromJson(albumData, Album::class.java)
            isLiked = isLikedAlbum(album.id)
            setViews(album)
            initViewPager()
            setClickListeners(album)
        } else {
            Log.e("AlbumFragment", "Album data is null")
            // album 데이터가 없을 경우에 대한 처리 추가
        }


        return binding.root
    }

    private fun setViews(album: Album) {
        binding.tvSongTitle.text = album.title.toString()
        binding.tvArtistname.text = album.singer.toString()
        binding.imageView1.setImageResource(album.coverImg!!)

        if(isLiked) {
            binding.btnLike.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.btnLike.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun setClickListeners(album: Album) {
        val userId: Int = getJwt()

        // FIX: Remove the duplicate like button click listener
        binding.btnLike.setOnClickListener {
            if(isLiked) {
                binding.btnLike.setImageResource(R.drawable.ic_my_like_off)
                disLikeAlbum(userId, album.id)
            } else {
                binding.btnLike.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId, album.id)
            }

            isLiked = !isLiked
        }

        // Back button implementation
        binding.btnBack.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commitAllowingStateLoss()
        }
    }

    private fun initViewPager() {
        //init viewpager
        val albumAdapter = AlbumVPAdapter(this)

        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) { tab, position ->
            tab.text = information[position]
        }.attach()
    }

    // 예시: Coroutine을 사용한 비동기 처리
    private fun disLikeAlbum(userId: Int, albumId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val songDB = SongDatabase.Companion.getInstance(requireContext())!!
            songDB.albumDao().disLikeAlbum(userId, albumId)
        }
    }

    private fun likeAlbum(userId: Int, albumId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val songDB = SongDatabase.Companion.getInstance(requireContext())!!
            val like = Like(userId, albumId)
            songDB.albumDao().likeAlbum(like)
        }
    }


    private fun getJwt(): Int {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val jwt = spf?.getInt("jwt", 0)
        if (jwt == 0) {
            Log.d("AlbumFragment", "JWT token is not available or is 0")
        }
        return jwt ?: 0 // 기본값 0 반환
    }

}