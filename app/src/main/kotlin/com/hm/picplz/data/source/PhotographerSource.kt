package com.hm.picplz.data.source

import com.hm.picplz.data.model.Photographer
import javax.inject.Inject

interface PhotographerSource {
    suspend fun getPhotographers(): Result<List<Photographer>>
}

class PhotographerSourceImpl @Inject constructor(
    private val service: PhotographerService
) : PhotographerSource {
    override suspend fun getPhotographers(): Result<List<Photographer>> =
        runCatching { service.getPhotographers() }
}