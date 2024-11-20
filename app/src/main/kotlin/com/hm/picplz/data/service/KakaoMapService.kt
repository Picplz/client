package com.hm.picplz.data.service

import com.hm.picplz.data.model.KaKaoAddressResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoMapService {
    @GET("v2/local/geo/coord2address.json")
    suspend fun getAddressFromCoordsService(
        @Header("Authorization") authorization: String,
        @Query("x") longitude: Double,
        @Query("y") latitude: Double
    ): KaKaoAddressResponse
}