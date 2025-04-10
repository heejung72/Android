package com.example.flo_mainpage

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LockerVPAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        // when = switch
        return when (position) {
            0 -> SaveSongFragment()
            else -> SongFileFragment()
        }
    }
}

