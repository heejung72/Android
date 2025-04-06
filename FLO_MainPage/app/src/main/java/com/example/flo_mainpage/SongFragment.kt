package com.example.flo_mainpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo_mainpage.databinding.FragmentDetailBinding
import com.example.flo_mainpage.databinding.FragmentSongBinding

class SongFragment : Fragment() {
    lateinit var binding: FragmentSongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)


        binding.buttonMix.tag = false
        binding.buttonMix.setImageResource(R.drawable.btn_toggle_off)

        binding.buttonMix.setOnClickListener {
            val isOn = binding.buttonMix.tag as Boolean  // 현재 상태 가져오기
            binding.buttonMix.setImageResource(if (isOn) R.drawable.btn_toggle_off else R.drawable.btn_toggle_on)
            binding.buttonMix.tag = !isOn  // 상태 변경
        }

        return binding.root
    }
}