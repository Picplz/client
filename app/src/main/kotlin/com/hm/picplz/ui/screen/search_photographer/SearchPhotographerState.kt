package com.hm.picplz.ui.screen.search_photographer

import com.hm.picplz.data.model.Photographer
import com.kakao.vectormap.LatLng

data class SearchPhotographerState (
    val address: String? = null,
    val centerCoords: LatLng = LatLng.from(37.406960, 127.115587),
    val userLocation: LatLng? = null,
    val isFetchingGPS: Boolean = true,
    val isSearchingPhotographer: Boolean = true,
    val nearbyPhotographers: List<Photographer> = emptyList()
) {
    companion object {
        fun idle(): SearchPhotographerState {
            return SearchPhotographerState()
        }
    }
}