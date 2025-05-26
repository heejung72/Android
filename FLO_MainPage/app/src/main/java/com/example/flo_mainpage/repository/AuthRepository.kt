package com.example.flo_mainpage.repository

import com.example.flo_mainpage.auth.member.*
import com.example.flo_mainpage.network.NetworkModule
import retrofit2.Response

class AuthRepository {
    private val api = NetworkModule.onboardingApi

    suspend fun login(email: String, password: String): Response<LoginResponse> {
        return try {
            api.login(LoginRequest(email, password))
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun join(name: String, email: String, password: String): Response<JoinResult> {
        return try {
            api.join(Join(name, email, password))
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun test(): Response<Test> {
        return try {
            api.test()
        } catch (e: Exception) {
            throw e
        }
    }
}
