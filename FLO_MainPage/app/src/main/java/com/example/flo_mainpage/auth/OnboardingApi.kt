package com.example.flo_mainpage.auth

import com.example.flo_mainpage.auth.health.Health
import com.example.flo_mainpage.auth.member.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface OnboardingApi {
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/join")
    suspend fun join(@Body request: Join): Response<JoinResult>

    @GET("/test")
    suspend fun test(): Response<Test>

    @GET("/health")
    suspend fun health(): Response<Health>
}
