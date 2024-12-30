package com.hm.picplz.viewmodel

import android.content.Context
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.data.model.KaKaoAddressRequest
import com.hm.picplz.data.source.KakaoMapSource
import com.hm.picplz.ui.screen.search_photographer.SearchPhotographerIntent
import com.hm.picplz.ui.screen.search_photographer.SearchPhotographerState
import com.kakao.vectormap.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationListener
import com.hm.picplz.data.model.Photographer
import com.hm.picplz.data.repository.PhotographerRepository
import com.hm.picplz.data.repository.PhotographerRepositoryImpl
import com.hm.picplz.data.source.PhotographerServiceImpl
import com.hm.picplz.data.source.PhotographerSourceImpl
import com.hm.picplz.ui.screen.search_photographer.SearchPhotographerSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@HiltViewModel
class SearchPhotographerViewModel @Inject constructor(
    private val photographerRepository: PhotographerRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SearchPhotographerState.idle())
    val state : StateFlow<SearchPhotographerState> get() = _state

    private val _sideEffect = MutableSharedFlow<SearchPhotographerSideEffect>()
    val sideEffect: SharedFlow<SearchPhotographerSideEffect> get() = _sideEffect

    init {
        handleIntent(SearchPhotographerIntent.FetchPhotographers)
    }

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
                            val twoDepthRegion = response.documents.firstOrNull()?.address?.region_2depth_name
                                ?.split(" ")
                                ?.lastOrNull() ?: ""
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
                handleIntent(SearchPhotographerIntent.GetAddress(intent.location))
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
            is SearchPhotographerIntent.SetIsSearchingPhotographer -> {
                _state.update { it.copy(isSearchingPhotographer = intent.isSearchingPhotographer) }
            }
            is SearchPhotographerIntent.SetNearbyPhotographers -> {
                _state.update { it.copy(nearbyPhotographers = intent.photographers)}
            }
            is SearchPhotographerIntent.FetchPhotographers,
            is SearchPhotographerIntent.RefreshPhotographers -> {
                viewModelScope.launch {
                    photographerRepository.getPhotographers()
                        .onSuccess { photographers ->
                            val nearbyPhotographers = filteredPhotographers(photographers)
                            Log.d("FetchPhotographers","작가 목록 로딩 성공 $nearbyPhotographers")
                            _state.update { it.copy(
                                nearbyPhotographers = nearbyPhotographers,
                                isSearchingPhotographer = false
                            )}
                        }
                        .onFailure { error ->
                            Log.e("FetchPhotographers", "작가 목록 로딩 실패", error)
                        }
                }
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

    private fun filteredPhotographers(photographers: List<Photographer>): List<Photographer> {
        val centerCoords = _state.value.centerCoords
        val distanceLimit = 2

        return photographers.filter { (_, location, _ ) ->
            val distance = getDistance(centerCoords, location)
            distance <= distanceLimit
        }
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