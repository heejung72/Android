package com.example.flo_mainpage

import android.content.Context

// JWT 가져오기
fun getJwt(context: Context): Int {
    val spf = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    return spf.getInt("jwt", -1) // 로그인하지 않았을 경우 -1 반환
}

// JWT 저장하기 (필요 시)
fun saveJwt(context: Context, jwt: Int) {
    val spf = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    val editor = spf.edit()
    editor.putInt("jwt", jwt)
    editor.apply()
}
