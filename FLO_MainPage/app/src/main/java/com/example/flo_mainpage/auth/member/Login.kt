package com.example.flo_mainpage.auth.member

data class Login(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: LoginResult
)

data class LoginResult(
    val memberId: Int,
    val accessToken: String
)

