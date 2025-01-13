package com.hm.picplz.data.repository

import com.hm.picplz.data.model.Photographer
import com.hm.picplz.data.source.PhotographerService
import com.hm.picplz.data.source.PhotographerSource
import javax.inject.Inject

interface PhotographerRepository {
    suspend fun getPhotographers(): Result<List<Photographer>>
}

class PhotographerRepositoryImpl @Inject constructor(
    private val photographerSource: PhotographerSource
) : PhotographerRepository {
    override suspend fun getPhotographers(): Result<List<Photographer>> {
        return photographerSource.getPhotographers()
    }
}