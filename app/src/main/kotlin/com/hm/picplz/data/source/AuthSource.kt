package com.hm.picplz.data.source

import com.hm.picplz.data.service.AuthService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AuthSource {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://3.36.183.87:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authService: AuthService = retrofit.create(AuthService::class.java)

//    suspend fun requestKakaoLogin(): Result<String> =
//        runCatching {
//            val response = authService.requestKakaoLogin()
//            if (response.isSuccessful) {
//                response.body() ?: throw Exception("Response body is null")
//            } else {
//                throw Exception("Request failed with code: ${response.code()}")
//            }
//        }
}