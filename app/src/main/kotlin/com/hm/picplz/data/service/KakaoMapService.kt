package com.hm.picplz.data.service

import com.hm.picplz.data.model.KaKaoAddressResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoMapService {
    @GET("v2/local/geo/coord2address.json")
    suspend fun getAddressFromCoordsService(
        @Header("Authorization") authorization: String,
        @Query("x") x: String,
        @Query("y") y: String
    ): KaKaoAddressResponse
}