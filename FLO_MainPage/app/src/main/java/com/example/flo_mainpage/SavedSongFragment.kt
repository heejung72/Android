package com.example.flo_mainpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo_mainpage.databinding.FragmentLockerSavedsongBinding

class SavedSongFragment : Fragment() {
    private lateinit var binding: FragmentLockerSavedsongBinding
    private lateinit var songDB: SongDatabase
    private lateinit var likedSongs: List<Song>
    private lateinit var savedSongRVAdapter: SavedSongRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerSavedsongBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initDB()
        initRecyclerView()
    }

    private fun initDB() {
        songDB = SongDatabase.getInstance(requireContext())!!
        likedSongs = songDB.songDao().getLikedSongs(isLike = true) // 좋아요한 곡만 조회
    }

    private fun initRecyclerView() {
        savedSongRVAdapter = SavedSongRVAdapter()
        binding.lockerSavedSongRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.lockerSavedSongRecyclerView.adapter = savedSongRVAdapter

        savedSongRVAdapter.addSongs(likedSongs)

        savedSongRVAdapter.setMyItemClickListener(object : SavedSongRVAdapter.MyItemClickListener {
            override fun onRemoveSong(songId: Int) {
                // Optional: 좋아요 취소 기능 추가하려면 여기서 처리
            }
        })
    }
}
