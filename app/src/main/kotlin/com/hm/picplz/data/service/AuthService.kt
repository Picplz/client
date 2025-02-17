package com.hm.picplz.data.service

import retrofit2.http.GET
import retrofit2.Response

interface AuthService {
    @GET("api/v1/oauth2/authorization/kakao")
    suspend fun requestKakaoLogin(): Response<String>
}