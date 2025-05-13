package com.example.flo_mainpage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo_mainpage.databinding.FragmentLockerSavedalbumBinding

class SavedAlbumFragment : Fragment() {
    lateinit var binding: FragmentLockerSavedalbumBinding
    lateinit var albumDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerSavedalbumBinding.inflate(inflater, container, false)
        albumDB = SongDatabase.getInstance(requireContext())!!
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview() {
        binding.lockerSavedSongRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val albumRVAdapter = AlbumLockerRVAdapter()

        // 어댑터에 클릭 리스너 등록
        albumRVAdapter.setMyItemClickListener(object : AlbumLockerRVAdapter.MyItemClickListener {
            override fun onRemoveSong(songId: Int) {
                albumDB.albumDao().disLikeAlbum(getJwt(), songId)
                initRecyclerview() // 삭제 후 목록 갱신
            }
        })

        binding.lockerSavedSongRecyclerView.adapter = albumRVAdapter

        // DB에서 좋아요 누른 앨범 가져오기
        val likedAlbums = albumDB.albumDao().getLikedAlbums(getJwt()) as ArrayList
        Log.d("SAVED_ALBUM/LIKED", likedAlbums.toString())

        albumRVAdapter.addAlbums(likedAlbums)
    }

    private fun getJwt(): Int {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val jwt = spf?.getInt("jwt", 0) ?: 0
        Log.d("MAIN_ACT/GET_JWT", "jwt_token: $jwt")
        return jwt
    }
}
