package com.example.flo_mainpage

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo_mainpage.databinding.ItemAlbumBinding
import com.example.flo_mainpage.databinding.ItemLockerAlbumBinding

class AlbumLockerRVAdapter : RecyclerView.Adapter<AlbumLockerRVAdapter.ViewHolder>() {
    private var albumList: ArrayList<Album> = arrayListOf()

    interface MyItemClickListener {
        fun onRemoveSong(songId: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(listener: MyItemClickListener) {
        mItemClickListener = listener
    }

    fun setAlbums(albums: ArrayList<Album>) {
        this.albumList = albums
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val binding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = albumList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albumList[position])
    }

    inner class ViewHolder(private val binding: ItemAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album) {
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg!!)

            // 예: 삭제 버튼 클릭 시
            binding.itemAlbumTitleTv.setOnClickListener {
                mItemClickListener.onRemoveSong(album.id)
            }
        }
    }
}
