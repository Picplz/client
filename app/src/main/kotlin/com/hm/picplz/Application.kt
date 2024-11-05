package com.hm.picplz

import android.app.Application
import android.util.Log
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            KakaoMapSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        } catch (e: Exception) {
            Log.e("KakaoMapSdk", "카카오맵 SDK init 실패", e)
        }
    }
}