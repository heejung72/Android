package com.example.flo_mainpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
                removeLikedSong(songId)
            }
        })
    }

    // 노래 좋아요 취소 기능
    private fun removeLikedSong(songId: Int) {
        // Room DB 업데이트 - 노래 좋아요 상태 변경
        val song = songDB.songDao().getSong(songId)
        song.isLike = false

        // 사용자에게 피드백 제공
        Toast.makeText(requireContext(), "${song.title} 노래가 좋아요 목록에서 삭제되었습니다", Toast.LENGTH_SHORT).show()

        // UI 갱신을 위해 좋아요 목록 다시 로드
        likedSongs = songDB.songDao().getLikedSongs(isLike = true)
        savedSongRVAdapter.addSongs(likedSongs)
    }
}


