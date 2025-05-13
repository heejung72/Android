package com.example.flo_mainpage

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.flo_mainpage.databinding.FragmentHomeBinding
import java.util.Timer
import java.util.TimerTask
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import java.util.ArrayList
import kotlin.concurrent.timer

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private var albumDatas = ArrayList<Album>()

    private lateinit var timer: Timer
    private val handler = Handler(Looper.getMainLooper())
    private var currentPosition = 0


    private lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        songDB = SongDatabase.getInstance(requireContext())!!

        albumDatas.addAll(songDB.albumDao().getAlbums())

        Log.d("albumList", albumDatas.toString())

            // 더미데이터랑 Adapter 연결
            val albumRVAdapter = AlbumRVAdapter(albumDatas)
            // 리사이클러뷰에 어댑터를 연결
            binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter

            albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener {

                override fun onItemClick(album: Album) {
                    changeAlbumFragment(album)
                }

                override fun onRemoveAlbum(position: Int) {
                    albumRVAdapter.removeItem(position)
                }
            })
            // 레이아웃 매니저 설정
            binding.homeTodayMusicAlbumRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


            // viewpager2 구현 (광고)
            val bannerAdapater = BannerVPAdapter(this)
            bannerAdapater.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
            bannerAdapater.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
            bannerAdapater.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
            bannerAdapater.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
            bannerAdapater.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
            bannerAdapater.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))

            binding.homeBannerVp.adapter = bannerAdapater
            binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 가로 실행

            // homepanel viewpager2 구현
            val homePanelAdapter = HomePanelVPAdaptor(this)
            homePanelAdapter.addFragment(HomePanelFragment(R.drawable.img_first_album_default))
            homePanelAdapter.addFragment(HomePanelFragment(R.drawable.img_album_exp3))
            homePanelAdapter.addFragment(HomePanelFragment(R.drawable.img_album_exp4))
            homePanelAdapter.addFragment(HomePanelFragment(R.drawable.img_album_exp5))

            binding.homePanelVp.adapter = homePanelAdapter
            binding.homePanelVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 가로 실행

            setupAutoSlide(homePanelAdapter.itemCount)

            return binding.root
        }

        private fun changeAlbumFragment(album: Album) {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AlbumFragment().apply {
                    arguments = Bundle().apply {
                        val gson = Gson()
                        val albumJson = gson.toJson(album)
                        putString("album", albumJson)
                    }
                })
                .commitAllowingStateLoss()
        }

        // 자동 슬라이드 설정 함수
        private fun setupAutoSlide(itemCount: Int) {
            // 페이지 변경 감지 리스너
            binding.homePanelVp.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    currentPosition = position
                }
            })

            // 타이머 설정
            timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    handler.post {
                        if (currentPosition == itemCount - 1) {
                            currentPosition = 0
                        } else {
                            currentPosition++
                        }
                        binding.homePanelVp.setCurrentItem(currentPosition, true)
                    }
                }
            }, 3000, 3000) // 최초 3초 후에 시작하여 3초마다 페이지 전환 (원하는 시간으로 조정 가능)
        }

        // Fragment 생명주기에 맞춰 타이머 해제
        override fun onDestroyView() {
            super.onDestroyView()
            if (::timer.isInitialized) {
                timer.cancel()
            }
        }

        // 화면이 보이지 않을 때 타이머 중지
        override fun onPause() {
            super.onPause()
            if (::timer.isInitialized) {
                timer.cancel()
            }
        }

        // 화면이 다시 보일 때 타이머 재시작
        override fun onResume() {
            super.onResume()
            if (::timer.isInitialized) {
                timer.cancel()
            }
            if (::binding.isInitialized && binding.homePanelVp.adapter != null) {
                setupAutoSlide((binding.homePanelVp.adapter as HomePanelVPAdaptor).itemCount)
            }
        }

    }


