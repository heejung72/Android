package com.example.flo_mainpage

data class Song (
    // data class
    val title : String = " ",
    val singer : String = " ",
    var second : Int = 0,
    var playTime : Int = 0,
    var isPlaying : Boolean = false
)