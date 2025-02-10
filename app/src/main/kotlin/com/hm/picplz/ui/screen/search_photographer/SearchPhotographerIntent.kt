package com.hm.picplz.ui.screen.search_photographer

import android.content.Context
import androidx.compose.ui.unit.Dp
import com.hm.picplz.ui.model.FilteredPhotographers
import com.hm.picplz.ui.model.Photographer
import com.kakao.vectormap.LatLng

sealed class SearchPhotographerIntent {
    data object NavigateToPrev : SearchPhotographerIntent()
    data class SetAddress(val address: String) : SearchPhotographerIntent()
    data class GetAddress(val Coords: LatLng) : SearchPhotographerIntent()
    data class SetCenterCoords(val centerCoords: LatLng) : SearchPhotographerIntent()
    data class SetCurrentLocation(val location: LatLng) : SearchPhotographerIntent()
    data class GetCurrentLocation(val context: Context) : SearchPhotographerIntent()
    data class SetIsSearchingPhotographer(val isSearchingPhotographer: Boolean) : SearchPhotographerIntent()
    data class SetNearbyPhotographers(val nearbyPhotographers : FilteredPhotographers) : SearchPhotographerIntent()
    data object FetchNearbyPhotographers : SearchPhotographerIntent()
    data object RefetchNearbyPhotographers : SearchPhotographerIntent()
    data class DistributeRandomOffsets(val photographers: FilteredPhotographers) : SearchPhotographerIntent()
    data class SetSelectedPhotographerId(val photographerId: Int?) : SearchPhotographerIntent()
    data class SetSheetMaxHeight(val maxHeight: Dp) : SearchPhotographerIntent()
    data class SetSheetPeekHeight(val peekHeight: Dp?) : SearchPhotographerIntent()
}