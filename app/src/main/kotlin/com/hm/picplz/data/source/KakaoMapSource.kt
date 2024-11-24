package com.hm.picplz.data.source

import com.hm.picplz.BuildConfig
import com.hm.picplz.data.model.KaKaoAddressRequest
import com.hm.picplz.data.model.KaKaoAddressResponse
import com.hm.picplz.data.service.KakaoMapService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class KakaoMapSource {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://dapi.kakao.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val kakaoMapService: KakaoMapService = retrofit.create(KakaoMapService::class.java)

    suspend fun getAddressFromCoords(request: KaKaoAddressRequest): Result<KaKaoAddressResponse> =
        runCatching {
            kakaoMapService.getAddressFromCoordsService(
                authorization = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}",
                x = request.x,
                y = request.y
            )
        }
}