package com.example.flo_mainpage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo_mainpage.databinding.FragmentLockerBinding
import com.example.umc_flo.BottomSheetFragment
import com.google.android.material.tabs.TabLayoutMediator

class LockerFragment : Fragment() {
    lateinit var binding: FragmentLockerBinding
    private val information = arrayListOf("저장한 곡", "음악파일", "저장앨범")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        val lockerAdapter = LockerVPAdapter(this)
        binding.lockerContentVp.adapter = lockerAdapter

        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp) { tab, position ->
            tab.text = information[position]
        }.attach()

        binding.lockerSelectAllTv.setOnClickListener {
            val bottomSheet = BottomSheetFragment()

            bottomSheet.setOnDeleteListener {
                // ViewPager2 Fragment 태그 규칙 참고: "f" + position
                val savedAlbumFragment = childFragmentManager.findFragmentByTag("f0")
                if (savedAlbumFragment is SavedAlbumFragment) {
                    savedAlbumFragment.refreshAlbums()
                }
            }

            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }

        return binding.root
    }
}
