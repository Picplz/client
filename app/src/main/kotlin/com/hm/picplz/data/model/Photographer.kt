package com.hm.picplz.data.model

import com.hm.picplz.ui.model.Photographer
import com.kakao.vectormap.LatLng

data class PhotographerEntity(
    val id: Int,
    val name: String,
    val location: LatLng?,
    val isActive: Boolean,
    val profileImageUri: String,
    val workingArea: String,
    val distance: Number,
    val followers: List<Number>,
    val socialAccount: String?,
)

typealias PhotographerResponse = PhotographerEntity

typealias PhotographerListResponse = List<PhotographerEntity>
