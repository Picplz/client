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
import com.hm.picplz.data.repository.PhotographerRepository
import com.hm.picplz.ui.model.FilteredPhotographers
import com.hm.picplz.ui.screen.search_photographer.SearchPhotographerSideEffect
import com.hm.picplz.utils.LocationUtil.calcurateScreenDistance
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
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
        handleIntent(SearchPhotographerIntent.FetchNearbyPhotographers)
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
                _state.update { it.copy(nearbyPhotographers = intent.nearbyPhotographers)}
            }
            is SearchPhotographerIntent.FetchNearbyPhotographers,
            is SearchPhotographerIntent.RefetchNearbyPhotographers -> {
                viewModelScope.launch {
//                    val userLocation = state.value.userLocation ?: return@launch
                    val dummyUserLocation = LatLng.from(37.402960, 127.115587)
//                    val userAddress = state.value.address ?: return@launch
                    val dummyUserAddress = "종로구 무악동"

                    handleIntent(SearchPhotographerIntent.SetIsSearchingPhotographer(true))
                    photographerRepository.getNearbyPhotographers(
                        userLocation = dummyUserLocation,
                        distanceLimit = 2,
                        countLimit = 5,
                        userAddress = dummyUserAddress,
                    )
                        .onSuccess { nearbyPhotographers ->
                            handleIntent(SearchPhotographerIntent.SetIsSearchingPhotographer(false))
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

    class OffsetGenerationFailedException : Exception("전체 위치 생성 최종 실패")

    private fun generateNonOverlappingOffsets(photographers: FilteredPhotographers): Map<Int, Pair<Float, Float>> {
        val maxAttempts = 1000

        for (attempt in 1..maxAttempts) {
            try {
                return tryGenerateOffsets(photographers)
            } catch (e: OffsetGenerationException) {
                continue
            }
        }

        throw OffsetGenerationFailedException()
    }

    private class OffsetGenerationException : Exception("개별 위치 생성 실패")


    private fun tryGenerateOffsets(photographers: FilteredPhotographers): Map<Int, Pair<Float, Float>> {
        val offsets = mutableMapOf<Int, Pair<Float, Float>>()
        val minDistance = 110f

        val innerAreaRatio = 0.75f
        val outerAreaRatio = 0.93f

        val maxSingleAttempts = 100

        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels / displayMetrics.density
        val padding = 40f

        val maxOffsetX = (screenWidth - padding * 2) / 2
        val innerCircleMaxOffsetX = maxOffsetX * innerAreaRatio
        val outerCircleMinOffsetX = maxOffsetX * outerAreaRatio
        Log.d("안뇽", innerCircleMaxOffsetX.toString())

        val center = Pair(0f, 0f)

        if (photographers.inactive.isEmpty()) {
            photographers.active.forEachIndexed{ index, photographer ->
                var newOffset: Pair<Float, Float>
                var attempts = 0

                do {
                    attempts++
                    newOffset = if (index < 3) {
                        Pair(
                            (Random.nextFloat() * 2 - 1) * maxOffsetX,
                            (Random.nextFloat() * 2 - 1) * maxOffsetX
                        )
                    } else {
                        Pair(
                            (Random.nextFloat() * 2 - 1) * maxOffsetX,
                            (Random.nextFloat() * 2 - 1) * maxOffsetX
                        )
                    }
                    if (attempts >= maxSingleAttempts) {
                        throw OffsetGenerationException()
                    }
                } while (
                    offsets.values.any { existingOffset ->
                        calcurateScreenDistance(existingOffset, newOffset) < minDistance
                    } ||
                    (index < 3 && calcurateScreenDistance(center, newOffset) < minDistance) ||
                    (index < 3 && calcurateScreenDistance(center, newOffset) > innerCircleMaxOffsetX) ||
                    (index >= 3 && calcurateScreenDistance(center, newOffset) < outerCircleMinOffsetX)
                )
                offsets[photographer.id] = newOffset
            }
        } else {
            photographers.active.forEachIndexed { index, photographer ->
                var attempts = 0
                var newOffset: Pair<Float, Float>

                do {
                    attempts++
                    newOffset = if (index < 3) {
                        Pair(
                            (Random.nextFloat() * 2 - 1) * maxOffsetX,
                            (Random.nextFloat() * 2 - 1) * maxOffsetX
                        )
                    } else {
                        Pair(
                            (Random.nextFloat() * 2 - 1) * maxOffsetX,
                            (Random.nextFloat() * 2 - 1) * maxOffsetX
                        )
                    }
                    if (attempts >= maxSingleAttempts) {
                        throw OffsetGenerationException()
                    }
                } while (
                    offsets.values.any { existingOffset ->
                        calcurateScreenDistance(existingOffset, newOffset) < minDistance
                    } ||
                    (index < 3 && calcurateScreenDistance(center, newOffset) < minDistance) ||
                    (index < 3 && calcurateScreenDistance(center, newOffset) > innerCircleMaxOffsetX) ||
                    (index >= 3 && calcurateScreenDistance(center, newOffset) < outerCircleMinOffsetX)
                )
                offsets[photographer.id] = newOffset
            }
            photographers.inactive.forEach { photographer ->
                var newOffset: Pair<Float, Float>
                var attempts = 0
                do {
                    attempts++
                    newOffset = Pair(
                        (Random.nextFloat() * 2 - 1) * maxOffsetX,
                        (Random.nextFloat() * 2 - 1) * maxOffsetX
                    )
                    if (attempts >= maxSingleAttempts) {
                        throw OffsetGenerationException()
                    }
                } while (
                    offsets.values.any { existingOffset ->
                        calcurateScreenDistance(existingOffset, newOffset) < minDistance
                    } ||
                    calcurateScreenDistance(center, newOffset) < outerCircleMinOffsetX
                )
                offsets[photographer.id] = newOffset
            }
        }
        return offsets
    }
}