package com.hm.picplz.ui.screen.search_photographer

import android.util.Log
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.kakao.vectormap.*
import com.kakao.vectormap.camera.CameraPosition


@Composable
fun KakaoMapView(
    modifier: Modifier = Modifier,
    onMapDestroy: () -> Unit = {},
    onMapReady: (KakaoMap) -> Unit = {},
    onMapError: (Exception) -> Unit = {},
    initialPosition: LatLng = LatLng.from(37.406960, 127.115587),
    initialZoomLevel: Int = 17,
    onResume: () -> Unit = {},
    onPause: () -> Unit = {},
    onCameraMoveEnd: (KakaoMap, CameraPosition, GestureType) -> Unit = { _: KakaoMap, _: CameraPosition, _: GestureType -> },
    isZoomEnable: Boolean = true,
) {
    var mapView by remember { mutableStateOf<MapView?>(null) }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            MapView(context).also { mapView = it }.apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                start(
                    object : MapLifeCycleCallback() {
                        override fun onMapDestroy() {
                            onMapDestroy()
                        }

                        override fun onMapError(error: Exception) {
                            onMapError(error)
                            Log.e("KakaoMapView", "Map Error: ${error.message}")
                            error.printStackTrace()
                            if (error is MapAuthException) {
                                when (error.errorCode) {
                                    -1 -> Log.e("KakaoMapView", "인증 과정 중 원인을 알 수 없는 에러가 발생한 상태")
                                    -2 -> Log.e("KakaoMapView", "통신 연결 시도 중 발생하는 에러")
                                    -3 -> Log.e(
                                        "KakaoMapView",
                                        "통신 연결 중 SocketTimeoutException 에러가 발생한 경우"
                                    )

                                    -4 -> Log.e(
                                        "KakaoMapView",
                                        "통신 시도 중 ConnectTimeoutException 에러가 발생한 경우"
                                    )

                                    400 -> Log.e(
                                        "KakaoMapView",
                                        "일반적인 오류. 주로 API에 필요한 필수 파라미터와 관련하여 서버가 클라이언트 오류를 감지해 요청을 처리하지 못한 상태입니다."
                                    )

                                    401 -> Log.e(
                                        "KakaoMapView",
                                        "인증 오류. 해당 리소스에 유효한 인증 자격 증명이 없어 요청에 실패한 상태"
                                    )

                                    403 -> Log.e(
                                        "KakaoMapView",
                                        "권한 오류. 서버에 요청이 전달되었지만, 권한 때문에 거절된 상태"
                                    )

                                    429 -> Log.e(
                                        "KakaoMapView",
                                        "쿼터 초과. 정해진 사용량이나 초당 요청 한도를 초과한 경우"
                                    )

                                    499 -> Log.e(
                                        "KakaoMapView",
                                        "통신 실패 오류. 인터넷 연결 상태 확인이 필요한 경우"
                                    )
                                }
                            }
                        }
                    },
                    object : KakaoMapReadyCallback() {
                        override fun onMapReady(kakaoMap: KakaoMap) {
                            onMapReady(kakaoMap)
                            kakaoMap.setOnCameraMoveEndListener { cameraListenerKakaoMap, cameraPosition, gestureType ->
                                onCameraMoveEnd(
                                    cameraListenerKakaoMap,
                                    cameraPosition,
                                    gestureType
                                )
                            }
                            kakaoMap.setGestureEnable(GestureType.Zoom, isZoomEnable)
                        }
                        override fun getPosition(): LatLng {
                            return initialPosition
                        }

                        override fun getZoomLevel(): Int {
                            return initialZoomLevel
                        }
                    }
                )
            }
        }
    )

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                mapView?.resume()
                onResume()
            }

            override fun onPause(owner: LifecycleOwner) {
                mapView?.pause()
                onPause()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}