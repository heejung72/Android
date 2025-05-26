package com.example.flo_mainpage.auth

import com.example.flo_mainpage.auth.health.Health
import com.example.flo_mainpage.auth.member.Join
import com.example.flo_mainpage.auth.member.JoinResult
import com.example.flo_mainpage.auth.member.Login
import com.example.flo_mainpage.auth.member.LoginResult
import com.example.flo_mainpage.auth.member.Test
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface OnboardingApi {
    @POST("/login")
    fun login(@Body request: Login): retrofit2.Call<LoginResult>

    @POST("/join")
    fun join(@Body request: Join): retrofit2.Call<JoinResult>

    @GET("/test")
    fun test(): retrofit2.Call<Test>

    @GET("/health")
    fun health(): retrofit2.Call<Health>
}
