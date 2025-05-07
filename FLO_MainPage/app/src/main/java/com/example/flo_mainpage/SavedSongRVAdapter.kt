package com.example.flo_mainpage
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo_mainpage.databinding.ItemSongBinding

class SavedSongRVAdapter() :
    RecyclerView.Adapter<SavedSongRVAdapter.ViewHolder>() {
    private val songs = ArrayList<Song>()
    interface MyItemClickListener{
        fun onRemoveSong(songId: Int)
    }
    private lateinit var mItemClickListener : MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SavedSongRVAdapter.ViewHolder {
        val binding: ItemSongBinding = ItemSongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedSongRVAdapter.ViewHolder, position: Int) {
        holder.bind(songs[position])

        // 스위치 상태에 따라 이미지 변경
        val song = songs[position]
        if (song.isSwitchOn) {
            holder.binding.itemSongSwitchOn.visibility = View.VISIBLE
            holder.binding.itemSongSwitchOff.visibility = View.GONE
        } else {
            holder.binding.itemSongSwitchOn.visibility = View.GONE
            holder.binding.itemSongSwitchOff.visibility = View.VISIBLE
        }

        // 스위치 클릭 리스너 설정
        holder.binding.itemSongSwitchOff.setOnClickListener {
            song.isSwitchOn = true
            notifyItemChanged(position)
        }

        holder.binding.itemSongSwitchOn.setOnClickListener {
            song.isSwitchOn = false
            notifyItemChanged(position)
        }

        // 더보기 버튼 클릭 리스너
        holder.binding.itemSongMoreIv.setOnClickListener {
            removeSong(position)
        }
    }

    override fun getItemCount(): Int = songs.size

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)

        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun removeSong(position: Int){
        songs.removeAt(position)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(song: Song){
            binding.itemSongImgIv.setImageResource(song.coverImg!!)
            binding.itemSongTitleTv.text = song.title
            binding.itemSongSingerTv.text = song.singer
        }
    }
}