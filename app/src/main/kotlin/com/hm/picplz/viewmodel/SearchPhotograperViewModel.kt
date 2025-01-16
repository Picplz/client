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
import com.hm.picplz.data.model.dummyPhotographers
import com.hm.picplz.data.repository.PhotographerRepository
import com.hm.picplz.data.repository.PhotographerRepositoryImpl
import com.hm.picplz.data.source.PhotographerServiceImpl
import com.hm.picplz.data.source.PhotographerSourceImpl
import com.hm.picplz.ui.screen.search_photographer.SearchPhotographerSideEffect
import com.hm.picplz.utils.LocationUtil.calcurateScreenDistance
import com.hm.picplz.utils.LocationUtil.getDistance
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

@HiltViewModel
class SearchPhotographerViewModel @Inject constructor(
    private val photographerRepository: PhotographerRepository,
    @ApplicationContext private val context: Context
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
                    handleIntent(SearchPhotographerIntent.SetIsSearchingPhotographer(true))
                    photographerRepository.getPhotographers()
                        .onSuccess { photographers ->
                            handleIntent(SearchPhotographerIntent.SetIsSearchingPhotographer(false))
                            val nearbyPhotographers = filteredPhotographers(photographers)
                            handleIntent(SearchPhotographerIntent.SetNearbyPhotographers(nearbyPhotographers))
                            handleIntent(SearchPhotographerIntent.DistributeRandomOffsets(nearbyPhotographers))
                        }
                        .onFailure { error ->
                            handleIntent(SearchPhotographerIntent.SetIsSearchingPhotographer(false))
                            Log.e("FetchPhotographers", "작가 목록 로딩 실패", error)
                        }
                }
            }
            is SearchPhotographerIntent.DistributeRandomOffsets -> {
                val randomOffsets = generateNonOverlappingOffsets(intent.photographers)
                _state.update { it.copy(randomOffsets = randomOffsets) }
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
//        val userLocation = _state.value.userLocation ?: return emptyList()
        val dummyUserLocation = LatLng.from(37.402960, 127.115587)
        val distanceLimit = 2

        return photographers.filter { (_, _, location, _ ) ->
            val distance = getDistance(dummyUserLocation, location)
            distance <= distanceLimit
        }
    }

    private fun generateNonOverlappingOffsets(photographers: List<Photographer>): Map<Int, Pair<Float, Float>> {
        val offsets = mutableMapOf<Int, Pair<Float, Float>>()
        val minDistance = 110f

        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels / displayMetrics.density
        val padding = 40f

        val maxOffsetX = (screenWidth - padding * 2) / 2

        Log.d("랜덤 위치", "screenWidth: $screenWidth, padding: $padding")
        val center = Pair(0f, 0f)
        photographers.forEach { photographer ->
            var newOffset: Pair<Float, Float>
            do {
                newOffset = Pair(
                    (Random.nextFloat() * 2 - 1) * maxOffsetX,
                    (Random.nextFloat() * 2 - 1) * maxOffsetX
                )
            } while (
                offsets.values.any { existingOffset ->
                    calcurateScreenDistance(existingOffset, newOffset) < minDistance
                } ||
                calcurateScreenDistance(center, newOffset) < minDistance
            )
            offsets[photographer.id] = newOffset
        }
        Log.d("랜덤 위치", "positions: $offsets")

        return offsets
    }
}