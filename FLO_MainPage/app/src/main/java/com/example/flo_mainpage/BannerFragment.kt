package com.example.flo_mainpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.flo_mainpage.databinding.FragmentBannerBinding
import me.relex.circleindicator.CircleIndicator
import me.relex.circleindicator.CircleIndicator3

class BannerFragment(val imgRes : Int) : Fragment() {
    lateinit var binding: FragmentBannerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentBannerBinding.inflate(inflater, container, false)

        binding.bannerImageIv.setImageResource(imgRes)


        return binding.root
    }
}