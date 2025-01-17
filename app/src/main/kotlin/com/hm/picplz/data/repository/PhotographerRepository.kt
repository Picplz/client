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
        countLimit: Int = 5,
        userAddress: String,
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
        countLimit: Int,
        userAddress: String
    ): Result<FilteredPhotographers> {
        return photographerSource.getPhotographers().map { response ->
            val photographers = response.toUiModel()

            val activeNearbyPhotographers = photographers
                .asSequence()
                .filter { it.isActive }
                .mapNotNull {
                    it.location?.let { location ->
                        it to getDistance(userLocation, location)
                    }
                }
                .filter { (_, distance) -> distance <= distanceLimit }
                .sortedBy { (_, distance) -> distance }
                .take(countLimit)
                .map { (photographer, _) -> photographer }
                .toList()

            if (activeNearbyPhotographers.size < countLimit) {
                val inactiveSameAreaPhotographers = photographers
                    .filter { !it.isActive && it.workingArea == userAddress }
                    .take(countLimit - activeNearbyPhotographers.size)

                FilteredPhotographers(
                    active = activeNearbyPhotographers,
                    inactive = inactiveSameAreaPhotographers
                )
            } else {
                FilteredPhotographers(
                    active = activeNearbyPhotographers,
                )
            }
        }
    }
}