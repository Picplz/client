package com.hm.picplz.data.source

import com.hm.picplz.data.model.Photographer
import com.hm.picplz.data.model.dummyPhotographers
import javax.inject.Inject

interface PhotographerService {
    suspend fun getPhotographers(): List<Photographer>
}

class PhotographerServiceImpl @Inject constructor() : PhotographerService {
    override suspend fun getPhotographers(): List<Photographer> {
        // TODO: API 작동
        // return apiClient.getPhotographers()

        return dummyPhotographers
    }
}