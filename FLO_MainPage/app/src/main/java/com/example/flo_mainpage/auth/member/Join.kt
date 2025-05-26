package com.example.flo_mainpage.auth.member

data class Join(
    val name: String,
    val email: String,
    val password: String
)

data class JoinResult(
    val isSuccess : Boolean,
    val code : String,
    val message : String,
    val result : Time
)

data class Time(
    val memberId : Int,
    val createdAt : String,
    val updatedAt: String
)
