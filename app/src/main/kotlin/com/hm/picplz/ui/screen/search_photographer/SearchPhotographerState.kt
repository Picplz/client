package com.hm.picplz.ui.screen.search_photographer

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
    val selectedPhotographerId: Int? = null,
    val sheetMaxHeight: Dp = 750.dp,
    val sheetPeekHeight: Dp? = 30.dp
) {
    companion object {
        fun idle(): SearchPhotographerState {
            return SearchPhotographerState()
        }
    }
}