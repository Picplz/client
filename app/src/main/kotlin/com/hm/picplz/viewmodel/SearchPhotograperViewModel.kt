package com.hm.picplz.viewmodel

import android.content.Context
import android.location.LocationManager
import android.util.Log
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.R
import com.hm.picplz.data.model.KaKaoAddressRequest
import com.hm.picplz.data.source.KakaoMapSource
import com.hm.picplz.ui.screen.search_photographer.SearchPhotographerIntent
import com.hm.picplz.ui.screen.search_photographer.SearchPhotographerState
import com.hm.picplz.ui.theme.MainThemeColor
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationListener
import com.hm.picplz.ui.screen.search_photographer.SearchPhotographerSideEffect
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class SearchPhotographerViewModel: ViewModel() {
    private val _state = MutableStateFlow(SearchPhotographerState.idle())
    val state : StateFlow<SearchPhotographerState> get() = _state

    private val _sideEffect = MutableSharedFlow<SearchPhotographerSideEffect>()
    val sideEffect: SharedFlow<SearchPhotographerSideEffect> get() = _sideEffect

    private val kakaoSource = KakaoMapSource()

    private var locationManager: LocationManager? = null
    private val locationListeners = mutableListOf<LocationListener>()

    fun handleIntent(intent: SearchPhotographerIntent) {
        when (intent) {
            is SearchPhotographerIntent.NavigateToPrev -> {
                viewModelScope.launch {
                    _sideEffect.emit(SearchPhotographerSideEffect.NavigateToPrev)
                }
            }
            is SearchPhotographerIntent.SetAddress -> {
                _state.update { it.copy(address = intent.address) }
            }
            is SearchPhotographerIntent.GetAddress -> {
                viewModelScope.launch {
                    kakaoSource.getAddressFromCoords(KaKaoAddressRequest(intent.Coords.longitude.toString(), intent.Coords.latitude.toString()))
                        .onSuccess { response ->
                            val twoDepthRegion = response.documents.firstOrNull()?.address?.region_2depth_name ?: ""
                            val threeDepthRegion = response.documents.firstOrNull()?.address?.region_3depth_name ?: ""
                            val address = "$twoDepthRegion $threeDepthRegion"
                            handleIntent(SearchPhotographerIntent.SetAddress(address))
                        }
                        .onFailure { error ->
                            Log.e("kakaoMapAddressSearch", "좌표 검색 실패 : ", error)
                        }
                }
            }
            is SearchPhotographerIntent.SetCenterCoords -> {
                _state.update { it.copy(centerCoords = intent.centerCoords) }
                handleIntent(SearchPhotographerIntent.GetAddress(intent.centerCoords))
            }
            is SearchPhotographerIntent.SetCurrentLocation -> {
                _state.update { it.copy(
                    userLocation = intent.location,
                    isFetchingGPS = false
                ) }
            }
            is SearchPhotographerIntent.GetCurrentLocation -> {
                if (locationManager == null) {
                    locationManager = intent.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                }

                if (ActivityCompat.checkSelfPermission(
                    intent.context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                        intent.context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) return

                val gpsProvider = LocationManager.GPS_PROVIDER
                val networkProvider = LocationManager.NETWORK_PROVIDER

                if (locationManager!!.isProviderEnabled(gpsProvider)) {
                    locationManager!!.requestLocationUpdates(
                        gpsProvider,
                        1000L,
                        1.0f,
                    ) { location ->
                        handleIntent(SearchPhotographerIntent.SetCurrentLocation(
                            LatLng.from(location.latitude, location.longitude)
                        ))
                    }
                    if (state.value.userLocation == null) {
                        val lastGpsLocation = locationManager?.getLastKnownLocation(gpsProvider)
                        if (lastGpsLocation != null) {
                            handleIntent(SearchPhotographerIntent.SetCurrentLocation(
                                LatLng.from(lastGpsLocation.latitude, lastGpsLocation.longitude)
                            ))
                        }
                    }
                } else if (locationManager!!.isProviderEnabled(networkProvider)) {
                    if (state.value.userLocation == null) {
                        locationManager!!.requestLocationUpdates(
                            networkProvider,
                            1000L,
                            1.0f,
                        ) { location ->
                            handleIntent(
                                SearchPhotographerIntent.SetCurrentLocation(
                                    LatLng.from(location.latitude, location.longitude)
                                )
                            )
                        }
                    }
                    if (state.value.userLocation == null) {
                        val lastNetworkLocation = locationManager?.getLastKnownLocation(networkProvider)
                        if (lastNetworkLocation != null) {
                            handleIntent(SearchPhotographerIntent.SetCurrentLocation(
                                LatLng.from(lastNetworkLocation.latitude, lastNetworkLocation.longitude)
                            ))
                        }
                    }
                }
            }
        }
    }

    fun displayLabelsOnMap(kakaoMap: KakaoMap) {
        viewModelScope.launch {
            val labelManager = kakaoMap.labelManager

            val labelStyles = LabelStyles.from(
                "photographerLabel",
                LabelStyle.from(R.drawable.phone_marker)
                    .setZoomLevel(8)
                    .setApplyDpScale(true)
                    .setAnchorPoint(0.5f, 1.0f),
                LabelStyle.from(R.drawable.phone_marker)
                    .setZoomLevel(11)
                    .setApplyDpScale(true),
                LabelStyle.from(R.drawable.phone_marker)
                    .setZoomLevel(15)
                    .setApplyDpScale(true)
                    .setTextStyles(24, MainThemeColor.Black.toArgb())
            )

            val currentLocation = _state.value.userLocation ?: return@launch

            /**
             * 더미 데이터
             * Todo : db에 있는 작가 데이터에서 근처 위치에 있는 데이터 정보만 필터링 해서 호출
             * **/
            val photographerLocations = listOf(
                LatLng.from(37.406960, 127.115587),
                LatLng.from(37.408960, 127.117587),
                LatLng.from(37.384921, 127.125171),
                LatLng.from(37.339832, 127.109160),
                LatLng.from(37.340521, 127.108872),
                LatLng.from(37.339245, 127.109876),
            )
            val distanceLimit =3
            val nearbyPhotographers = photographerLocations.filter { photographerLocation ->
                val distance = getDistance(currentLocation, photographerLocation )
                distance <= distanceLimit
            }

            kakaoMap.labelManager?.layer?.removeAll()

            val styles = labelManager?.addLabelStyles(labelStyles)
            val texts = LabelTextBuilder().setTexts("작가")

            nearbyPhotographers.forEach { location ->
                val options = LabelOptions.from(location)
                    .setStyles(styles)
                    .setTexts(texts)
                val layer = labelManager?.layer
                layer?.addLabel(options)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        locationListeners.forEach { listener ->
            locationManager?.removeUpdates(listener)
        }
        locationListeners.clear()
    }

    private fun getDistance(location1: LatLng, location2: LatLng): Double {
        val earthRadius = 6371
        val deltaLat = Math.toRadians(location2.latitude - location1.latitude)
        val deltaLng = Math.toRadians(location2.longitude - location1.longitude)

        val haversine = sin(deltaLat/2).pow(2) +
                cos(Math.toRadians(location1.latitude)) *
                cos(Math.toRadians(location2.latitude)) *
                sin(deltaLng/2).pow(2)

        val angularDistance = 2 * atan2(sqrt(haversine), sqrt(1-haversine))
        return earthRadius * angularDistance
    }
}