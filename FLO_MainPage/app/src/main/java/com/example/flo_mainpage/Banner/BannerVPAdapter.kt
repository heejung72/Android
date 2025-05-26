package com.example.flo_mainpage.Banner

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerVPAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {
    // 하나의 리스트에 여러개의 프래그먼트 담기 (광고 여러개니까)
    // private이라고 안쓰면 home에서 사용 가능함
    private val fragementList : ArrayList<Fragment> = ArrayList() // 초기화까지

    // override fun getItemCount(): Int = fragmentList.size 라고 작성해도 된다
    override fun getItemCount(): Int {
        // 몇개를 전달할지
        return fragementList.size
    }

    override fun createFragment(position: Int): Fragment = fragementList[position]

    fun addFragment(fragment: Fragment){
        fragementList.add(fragment)
        notifyItemInserted(fragementList.size - 1)
    }
}