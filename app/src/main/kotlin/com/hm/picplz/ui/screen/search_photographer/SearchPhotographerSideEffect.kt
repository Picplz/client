package com.hm.picplz.ui.screen.search_photographer

sealed class SearchPhotographerSideEffect {
    data object NavigateToPrev : SearchPhotographerSideEffect()
}