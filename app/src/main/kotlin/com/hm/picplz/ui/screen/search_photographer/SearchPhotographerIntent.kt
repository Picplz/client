package com.hm.picplz.ui.screen.search_photographer

import com.kakao.vectormap.LatLng

sealed class SearchPhotographerIntent {
    data class SetAddress(val address: String) : SearchPhotographerIntent()
    data class GetAddress(val Coords: LatLng) : SearchPhotographerIntent()
    data class SetCenterCoords(val centerCoords: LatLng) : SearchPhotographerIntent()
}