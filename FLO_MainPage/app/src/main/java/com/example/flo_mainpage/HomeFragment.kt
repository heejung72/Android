package com.example.flo_mainpage

import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.flo_mainpage.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.imageView40.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AlbumFragment())
                .commitAllowingStateLoss()
        }

        // viewpager2 구현 (광고)
        val bannerAdapater = BannerVPAdapter(this)
        bannerAdapater.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapater.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        bannerAdapater.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapater.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        bannerAdapater.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapater.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))

        binding.homeBannerVp.adapter=bannerAdapater
        binding.homeBannerVp.orientation= ViewPager2.ORIENTATION_HORIZONTAL // 가로 실행

        return binding.root
    }
}