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
    private val songList = ArrayList<Song>()

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
        initRecyclerView()
    }

    private fun initRecyclerView() {
        // 더미 데이터 추가
        songList.apply {
            add(Song("Butter", "방탄소년단", 0, 60, false, "music_butter", R.drawable.img_album_exp))
            add(Song("Lilac", "아이유", 0, 70, false, "music_lilac", R.drawable.img_album_exp2))
            add(Song("Next Level", "에스파", 0, 90, false, "music_next", R.drawable.img_album_exp3))
            add(Song("Boy with Luv", "방탄소년단", 0, 80, false, "music_boy", R.drawable.img_album_exp4))
            add(Song("BBoom BBoom", "모모랜드", 0, 75, false, "music_bboom", R.drawable.img_album_exp5))
            add(Song("Weekend", "태연", 0, 85, false, "music_weekend", R.drawable.img_album_exp6))
        }

        val songRVAdapter = SavedSongRVAdapter()
        binding.lockerSavedSongRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.lockerSavedSongRecyclerView.adapter = songRVAdapter

        // 데이터 적용
        songRVAdapter.addSongs(songList)

        // 클릭 리스너 연결
        songRVAdapter.setMyItemClickListener(object : SavedSongRVAdapter.MyItemClickListener {
            override fun onRemoveSong(songId: Int) {
                // 삭제 기능은 비워둬도 됩니다 (DB 없이 구현 중이므로)
            }
        })
    }
}
