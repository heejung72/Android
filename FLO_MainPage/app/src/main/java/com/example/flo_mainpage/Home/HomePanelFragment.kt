package com.example.flo_mainpage.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo_mainpage.databinding.FragmentHomepanelBinding


class HomePanelFragment(val imgRes : Int) : Fragment() {
    lateinit var binding: FragmentHomepanelBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomepanelBinding.inflate(inflater, container, false)

        binding.homePanelImgVp.setImageResource(imgRes)


        return binding.root
    }
}