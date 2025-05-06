package com.example.flo_mainpage

import android.content.Intent // 화면 전환
import android.os.Bundle // 화면 간 데이터 저장 및 복원
import android.view.LayoutInflater // XML 레이아웃을 View 객체로 변환
import android.view.View // 뷰 객체
import android.view.ViewGroup // 뷰 그룹 객체
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo_mainpage.databinding.FragmentAlbumBinding // 뷰 바인딩 클래스
import com.example.flo_mainpage.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator // 탭 레이아웃과 뷰페이저2를 연결
import com.google.gson.Gson

class AlbumFragment : Fragment(){
    lateinit var binding: FragmentAlbumBinding
    private var gson: Gson = Gson()

    private val information = arrayListOf("수록곡", "상세정보", "영상")

    // fragment에서는 onCreateView activity는 onCreate
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        // Home 에서 넘어온 데이터 받아오기
        val albumJson = arguments?.getString("album")
        val album = gson.fromJson(albumJson, Album::class.java)
        // Home에서 넘어온 데이터를 반영
        setInit(album)

        binding.btnBack.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container,HomeFragment()).commitAllowingStateLoss()
        }


        // lilac 팝업이 발생함
        binding.tvSongTitle.setOnClickListener{
            Toast.makeText(activity,"LILAC", Toast.LENGTH_SHORT).show()
        }

        val albumVPAdapter = AlbumVPAdapter(this)
        binding.albumContentVp.adapter = albumVPAdapter
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp){
            tab, position ->
            tab.text = information[position]
        }.attach()

        return binding.root}

        fun setInit(album: Album) {
            binding.imageView1.setImageResource(album.coverImg!!)
            binding.tvSongTitle.text = album.title.toString()
            binding.tvArtistname.text = album.singer.toString()}
    }
