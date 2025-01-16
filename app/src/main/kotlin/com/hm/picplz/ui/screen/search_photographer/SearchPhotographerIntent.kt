package com.hm.picplz.ui.screen.search_photographer

import android.content.Context
import com.hm.picplz.data.model.Photographer
import com.kakao.vectormap.LatLng

sealed class SearchPhotographerIntent {
    data object NavigateToPrev : SearchPhotographerIntent()
    data class SetAddress(val address: String) : SearchPhotographerIntent()
    data class GetAddress(val Coords: LatLng) : SearchPhotographerIntent()
    data class SetCenterCoords(val centerCoords: LatLng) : SearchPhotographerIntent()
    data class SetCurrentLocation(val location: LatLng) : SearchPhotographerIntent()
    data class GetCurrentLocation(val context: Context) : SearchPhotographerIntent()
    data class SetIsSearchingPhotographer(val isSearchingPhotographer: Boolean) : SearchPhotographerIntent()
    data class SetNearbyPhotographers(val photographers : List<Photographer>) : SearchPhotographerIntent()
    data object FetchPhotographers : SearchPhotographerIntent()
    data object RefreshPhotographers : SearchPhotographerIntent()
    data class DistributeRandomOffsets(val photographers: List<Photographer>) : SearchPhotographerIntent()
}