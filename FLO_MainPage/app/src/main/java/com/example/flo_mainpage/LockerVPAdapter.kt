package com.example.flo_mainpage

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo_mainpage.databinding.FragmentLockerBinding

class LockerVPAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        // when = switch
        return when (position) {
            0 -> SavedSongFragment()
            else -> SongFileFragment()
        }
    }
}

