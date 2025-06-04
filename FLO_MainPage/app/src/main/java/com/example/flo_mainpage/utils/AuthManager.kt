package com.example.flo_mainpage.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.kakao.sdk.user.UserApiClient

class AuthManager(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("is_logged_in", false)
    }

    fun getLoginType(): String {
        return prefs.getString("login_type", "none") ?: "none"
    }

    fun getUserInfo(): Map<String, String> {
        return mapOf(
            "kakao_id" to (prefs.getString("kakao_id", "") ?: ""),
            "kakao_email" to (prefs.getString("kakao_email", "") ?: ""),
            "kakao_nickname" to (prefs.getString("kakao_nickname", "") ?: ""),
            "kakao_profile_image" to (prefs.getString("kakao_profile_image", "") ?: ""),
            "access_token" to (prefs.getString("access_token", "") ?: "")
        )
    }

    fun logout(callback: (Boolean) -> Unit) {
        val loginType = getLoginType()

        when (loginType) {
            "kakao" -> {
                // 카카오 로그아웃
                UserApiClient.instance.logout { error ->
                    if (error != null) {
                        Log.e("AuthManager", "카카오 로그아웃 실패. SDK에서 토큰 삭제됨", error)
                    } else {
                        Log.i("AuthManager", "카카오 로그아웃 성공. SDK에서 토큰 삭제됨")
                    }

                    // 로컬 저장된 정보 삭제
                    clearUserData()
                    callback(true)
                }
            }
            "normal" -> {
                // 일반 로그인 로그아웃
                clearUserData()
                callback(true)
            }
            else -> {
                clearUserData()
                callback(true)
            }
        }
    }

    fun unlink(callback: (Boolean) -> Unit) {
        val loginType = getLoginType()

        if (loginType == "kakao") {
            // 카카오 연결 끊기 (회원탈퇴)
            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    Log.e("AuthManager", "카카오 연결 끊기 실패", error)
                    callback(false)
                } else {
                    Log.i("AuthManager", "카카오 연결 끊기 성공. SDK에서 토큰 삭제됨")
                    clearUserData()
                    callback(true)
                }
            }
        } else {
            // 일반 회원 탈퇴 로직 구현
            clearUserData()
            callback(true)
        }
    }

    private fun clearUserData() {
        prefs.edit().clear().apply()
    }
}