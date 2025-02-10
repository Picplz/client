package com.hm.picplz.ui.model

import com.hm.picplz.data.model.PhotographerResponse
import com.kakao.vectormap.LatLng

data class Photographer (
    val id: Int,
    val name: String,
    val location: LatLng? = null,
    val profileImageUri: String,
    val isActive: Boolean,
    val workingArea: String,
)

fun PhotographerResponse.toUiModel(): Photographer {
    return Photographer(
        id = id,
        name = name,
        location = location,
        profileImageUri = profileImageUri,
        isActive = isActive,
        workingArea = workingArea,
    )
}

fun List<PhotographerResponse>.toUiModel(): List<Photographer> {
    return map { it.toUiModel() }
}

data class FilteredPhotographers(
    val active: List<Photographer> = emptyList(),
    val inactive: List<Photographer> = emptyList()
)