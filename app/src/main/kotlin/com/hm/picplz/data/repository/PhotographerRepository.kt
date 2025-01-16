package com.hm.picplz.data.repository

import com.hm.picplz.data.source.PhotographerSource
import com.hm.picplz.ui.model.FilteredPhotographers
import com.hm.picplz.ui.model.Photographer
import com.hm.picplz.ui.model.toUiModel
import com.hm.picplz.utils.LocationUtil.getDistance
import com.kakao.vectormap.LatLng
import javax.inject.Inject

interface PhotographerRepository {
    suspend fun getPhotographers(): Result<List<Photographer>>
    suspend fun getNearbyPhotographers(
        userLocation: LatLng,
        distanceLimit: Int = 2,
        countLimit: Int = 5
    ): Result<FilteredPhotographers>}

class PhotographerRepositoryImpl @Inject constructor(
    private val photographerSource: PhotographerSource
) : PhotographerRepository {
    override suspend fun getPhotographers(): Result<List<Photographer>> {
        return photographerSource.getPhotographers().map { response ->
            response.map { it.toUiModel() }
        }
    }
    override suspend fun getNearbyPhotographers(
        userLocation: LatLng,
        distanceLimit: Int,
        countLimit: Int
    ): Result<FilteredPhotographers> {
        return photographerSource.getPhotographers().map { photographers ->
            val filteredPhotographers = photographers
                .map { it to getDistance(userLocation, it.location) }
                .filter { (_, distance) -> distance <= distanceLimit }
                .sortedBy { (_, distance) -> distance }
                .map { (photographer, _) -> photographer.toUiModel() }

            val activePhotographers = filteredPhotographers
                .filter { it.isActive }
                .take(countLimit)

            if (activePhotographers.size < countLimit) {
                val inactivePhotographers = filteredPhotographers
                    .filter { !it.isActive }
                    .take(countLimit - activePhotographers.size)

                FilteredPhotographers(
                    active = activePhotographers,
                    inactive = inactivePhotographers
                )
            } else {
                FilteredPhotographers(
                    active = activePhotographers,
                )
            }
        }
    }
}