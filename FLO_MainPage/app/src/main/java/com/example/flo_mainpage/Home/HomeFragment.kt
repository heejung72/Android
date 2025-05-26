package com.example.flo_mainpage.Home

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
import com.example.flo_mainpage.Album.Album
import com.example.flo_mainpage.Album.AlbumFragment
import com.example.flo_mainpage.Album.AlbumRVAdapter
import com.example.flo_mainpage.Banner.BannerFragment
import com.example.flo_mainpage.Banner.BannerVPAdapter
import com.example.flo_mainpage.MainActivity
import com.example.flo_mainpage.R
import com.example.flo_mainpage.Song.SongDatabase
import com.google.gson.Gson
import java.util.ArrayList

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

        // Initialize database and fetch albums
        songDB = SongDatabase.Companion.getInstance(requireContext())!!

        // Debug log before fetching albums
        Log.d("HomeFragment", "Fetching albums from database")
        albumDatas.clear()
        albumDatas.addAll(songDB.albumDao().getAlbums())
        Log.d("HomeFragment", "Albums fetched: ${albumDatas.size}")

        // More detailed logging to see what albums we have
        for (album in albumDatas) {
            Log.d("HomeFragment", "Album: ${album.id} - ${album.title} by ${album.singer}")
        }

        // Set up RecyclerView with albums
        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        binding.homeTodayMusicAlbumRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener {
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }

            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeItem(position)
            }
        })

        // ViewPager2 implementation (banner)
        val bannerAdapater = BannerVPAdapter(this)
        bannerAdapater.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapater.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        bannerAdapater.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapater.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        bannerAdapater.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapater.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))

        binding.homeBannerVp.adapter = bannerAdapater
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // Home panel ViewPager2 implementation
        val homePanelAdapter = HomePanelVPAdaptor(this)
        homePanelAdapter.addFragment(HomePanelFragment(R.drawable.img_first_album_default))
        homePanelAdapter.addFragment(HomePanelFragment(R.drawable.img_album_exp3))
        homePanelAdapter.addFragment(HomePanelFragment(R.drawable.img_album_exp4))
        homePanelAdapter.addFragment(HomePanelFragment(R.drawable.img_album_exp5))

        binding.homePanelVp.adapter = homePanelAdapter
        binding.homePanelVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        setupAutoSlide(homePanelAdapter.itemCount)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        // Refresh album data when returning to this fragment
        if (::songDB.isInitialized) {
            albumDatas.clear()
            albumDatas.addAll(songDB.albumDao().getAlbums())
            binding.homeTodayMusicAlbumRv.adapter?.notifyDataSetChanged()
        }

        // Setup slider
        if (::binding.isInitialized && binding.homePanelVp.adapter != null) {
            setupAutoSlide((binding.homePanelVp.adapter as HomePanelVPAdaptor).itemCount)
        }
    }

    private fun changeAlbumFragment(album: Album) {
        Log.d("HomeFragment", "Navigating to album: ${album.title}")

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

    // Auto slide setup
    private fun setupAutoSlide(itemCount: Int) {
        binding.homePanelVp.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPosition = position
            }
        })

        if (::timer.isInitialized) {
            timer.cancel()
        }

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
        }, 3000, 3000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::timer.isInitialized) {
            timer.cancel()
        }
    }

    override fun onPause() {
        super.onPause()
        if (::timer.isInitialized) {
            timer.cancel()
        }
    }
}