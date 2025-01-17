package com.hm.picplz.data.source

import com.hm.picplz.data.model.PhotographerListResponse
import com.hm.picplz.data.service.PhotographerService
import javax.inject.Inject

interface PhotographerSource {
    suspend fun getPhotographers(): Result<PhotographerListResponse>
}

class PhotographerSourceImpl @Inject constructor(
    private val service: PhotographerService
) : PhotographerSource {
    override suspend fun getPhotographers(): Result<PhotographerListResponse> =
        runCatching { service.getPhotographers() }
}