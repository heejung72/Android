package com.example.flo_mainpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo_mainpage.Album.Album
import com.example.flo_mainpage.Album.AlbumLockerRVAdapter
import com.example.flo_mainpage.Song.SongDatabase
import com.example.flo_mainpage.databinding.FragmentLockerSavedalbumBinding

class SavedAlbumFragment : Fragment() {
    private lateinit var binding: FragmentLockerSavedalbumBinding
    private lateinit var albumDB: SongDatabase
    private lateinit var albumRVAdapter: AlbumLockerRVAdapter
    private lateinit var likedAlbums: List<Album>  // Album 데이터 클래스 가정

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
            override fun onRemoveSong(albumId: Int) {
                Thread {
                    val album = albumDB.albumDao().getAlbum(albumId)
                    if (album != null) {
                        album.isLike = false
                        albumDB.albumDao().updateAlbum(album)
                        requireActivity().runOnUiThread {
                            Toast.makeText(requireContext(), "${album.title} 앨범이 좋아요 목록에서 삭제되었습니다", Toast.LENGTH_SHORT).show()
                            loadLikedAlbums()
                        }
                    }
                }.start()
            }
        })

        binding.lockerSavedSongRecyclerView.adapter = albumRVAdapter
    }

    private fun loadLikedAlbums() {
        Thread {
            likedAlbums = albumDB.albumDao().getLikedAlbums(true)
            requireActivity().runOnUiThread {
                albumRVAdapter.setAlbums(ArrayList(likedAlbums))
            }
        }.start()
    }

    fun refreshAlbums() {
        loadLikedAlbums()
    }
}
