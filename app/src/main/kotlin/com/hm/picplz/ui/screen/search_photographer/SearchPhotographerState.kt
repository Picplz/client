package com.hm.picplz.ui.screen.search_photographer

data class SearchPhotographerState (
    val address: String? = null,
) {
    companion object {
        fun idle(): SearchPhotographerState {
            return SearchPhotographerState(
                address = null,
            )
        }
    }
}