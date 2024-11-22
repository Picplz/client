package com.hm.picplz.ui.screen.search_photographer

import android.content.Context
import com.kakao.vectormap.LatLng

sealed class SearchPhotographerIntent {
    data class SetAddress(val address: String) : SearchPhotographerIntent()
    data class GetAddress(val Coords: LatLng) : SearchPhotographerIntent()
    data class SetCenterCoords(val centerCoords: LatLng) : SearchPhotographerIntent()
    data class SetCurrentLocation(val location: LatLng) : SearchPhotographerIntent()
    data class GetCurrentLocation(val context: Context) : SearchPhotographerIntent()
}