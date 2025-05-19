package com.example.flo_mainpage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo_mainpage.databinding.FragmentLockerSavedalbumBinding

class SavedAlbumFragment : Fragment() {
    lateinit var binding: FragmentLockerSavedalbumBinding
    lateinit var albumDB: SongDatabase
    private lateinit var albumRVAdapter: AlbumLockerRVAdapter

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
        loadAlbums()
    }

    private fun initRecyclerview() {
        binding.lockerSavedSongRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        albumRVAdapter = AlbumLockerRVAdapter()

        albumRVAdapter.setMyItemClickListener(object : AlbumLockerRVAdapter.MyItemClickListener {
            override fun onRemoveSong(songId: Int) {
                Thread {
                    // DB에서 삭제
                    albumDB.albumDao().deleteAlbumById(songId)

                }.start()
            }
        })


        binding.lockerSavedSongRecyclerView.adapter = albumRVAdapter
    }

    private fun loadAlbums() {
        Thread {
            val allAlbums = albumDB.albumDao().getAlbums() as ArrayList
            requireActivity().runOnUiThread {
                albumRVAdapter.addAlbums(allAlbums)
            }
        }.start()
    }

    // 바깥에서 호출하는 UI 갱신 함수
    fun refreshAlbums() {
        loadAlbums()
    }

}
