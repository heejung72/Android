package com.example.flo_mainpage

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // 카카오 SDK 초기화
        KakaoSdk.init(this, "b57134c4643e326fc88994dbbf43cb58")
    }
}