package com.example.flo_mainpage

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo_mainpage.databinding.ItemSongBinding

class SavedSongRVAdapter :
    RecyclerView.Adapter<SavedSongRVAdapter.ViewHolder>() {

    private val songs = ArrayList<Song>()

    interface MyItemClickListener {
        fun onRemoveSong(songId: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songs[position]
        holder.bind(song)

        // 좋아요 스위치 UI
        holder.binding.itemSongSwitchOn.visibility = if (song.isSwitchOn) View.VISIBLE else View.GONE
        holder.binding.itemSongSwitchOff.visibility = if (song.isSwitchOn) View.GONE else View.VISIBLE

        holder.binding.itemSongSwitchOff.setOnClickListener {
            song.isSwitchOn = true
            notifyItemChanged(position)
        }

        holder.binding.itemSongSwitchOn.setOnClickListener {
            song.isSwitchOn = false
            notifyItemChanged(position)
        }

        // 더보기 클릭 → 좋아요 취소 처리 요청
        holder.binding.itemSongMoreIv.setOnClickListener {
            mItemClickListener.onRemoveSong(song.id) // 외부에서 DB 업데이트 가능
            removeSong(position)
        }
    }

    override fun getItemCount(): Int = songs.size

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(newSongs: List<Song>) {
        songs.clear()
        songs.addAll(newSongs)
        notifyDataSetChanged()
    }

    private fun removeSong(position: Int) {
        songs.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class ViewHolder(val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            binding.itemSongImgIv.setImageResource(song.coverImg ?: R.drawable.img_album_exp2)
            binding.itemSongTitleTv.text = song.title
            binding.itemSongSingerTv.text = song.singer
        }
    }
}
