package com.example.flo_mainpage.Locker

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo_mainpage.MusicFileFragment
import com.example.flo_mainpage.SavedAlbumFragment
import com.example.flo_mainpage.SavedSongFragment

class LockerVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> SavedSongFragment()
            1 -> MusicFileFragment()
            else -> SavedAlbumFragment()
        }
    }
}