package com.example.flo_mainpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo_mainpage.databinding.FragmentLockerSavedalbumBinding

class SavedAlbumFragment : Fragment() {
    private lateinit var binding: FragmentLockerSavedalbumBinding
    private lateinit var albumDB: SongDatabase
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
        initRecyclerView()
        loadLikedAlbums()
    }

    private fun initRecyclerView() {
        binding.lockerSavedSongRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        albumRVAdapter = AlbumLockerRVAdapter()

        albumRVAdapter.setMyItemClickListener(object : AlbumLockerRVAdapter.MyItemClickListener {
            override fun onRemoveSong(songId: Int) {
                Thread {
                    albumDB.albumDao().deleteAlbumById(songId)

                    // 삭제 후 메인 스레드에서 UI 갱신
                    requireActivity().runOnUiThread {
                        loadLikedAlbums()
                    }
                }.start()
            }
        })

        binding.lockerSavedSongRecyclerView.adapter = albumRVAdapter
    }

    private fun loadLikedAlbums() {
        Thread {
            val likedAlbums = albumDB.albumDao().getLikedAlbums(getJwt(requireContext()))
            requireActivity().runOnUiThread {
                albumRVAdapter.setAlbums(ArrayList(likedAlbums))
            }
        }.start()
    }

    fun refreshAlbums() {
        loadLikedAlbums()
    }
}
