package com.hm.picplz.ui.screen.search_photographer

sealed class SearchPhotographerIntent {
    data class SetAddress(val address: String) : SearchPhotographerIntent()
    data class GetAddress(val latitude: Double, val longitude: Double) : SearchPhotographerIntent()
}