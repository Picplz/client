package com.hm.picplz.ui.screen.search_photographer

import com.hm.picplz.ui.model.FilteredPhotographers
import com.kakao.vectormap.LatLng

data class SearchPhotographerState (
    val address: String? = null,
    val centerCoords: LatLng = LatLng.from(37.406960, 127.115587),
    val userLocation: LatLng? = null,
    val isFetchingGPS: Boolean = false,
    val isSearchingPhotographer: Boolean = false,
    val nearbyPhotographers: FilteredPhotographers = FilteredPhotographers(),
    val randomOffsets: Map<Int, Pair<Float, Float>> = emptyMap(),
) {
    companion object {
        fun idle(): SearchPhotographerState {
            return SearchPhotographerState()
        }
    }
}