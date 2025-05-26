package com.example.flo_mainpage.Album

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo_mainpage.DetailFragment
import com.example.flo_mainpage.Song.SongFragment
import com.example.flo_mainpage.VideoFragment

class AlbumVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    // 수록곡 영상 상세정보
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        // when = switch
        return when(position) {
            0 -> SongFragment()
            1 -> DetailFragment()
            else -> VideoFragment()
        }
    }
}
