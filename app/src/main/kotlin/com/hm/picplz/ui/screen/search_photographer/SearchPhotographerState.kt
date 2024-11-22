package com.hm.picplz.ui.screen.search_photographer

import com.kakao.vectormap.LatLng

data class SearchPhotographerState (
    val address: String? = null,
    val centerCoords: LatLng = LatLng.from(37.406960, 127.115587),
    val userLocation: LatLng? = null
) {
    companion object {
        fun idle(): SearchPhotographerState {
            return SearchPhotographerState(
                address = null,
                centerCoords = LatLng.from(37.406960, 127.115587),
                userLocation = null
            )
        }
    }
}