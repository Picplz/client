package com.hm.picplz.ui.screen.search_photographer

import com.kakao.vectormap.LatLng

data class SearchPhotographerState (
    val address: String? = null,
    val centerCoords: LatLng = LatLng.from(37.406960, 127.115587),
    val userLocation: LatLng? = null,
    val isFetchingGPS: Boolean = true,
    val isSearchingPhotographer: Boolean = true,
) {
    companion object {
        fun idle(): SearchPhotographerState {
            return SearchPhotographerState()
        }
    }
}