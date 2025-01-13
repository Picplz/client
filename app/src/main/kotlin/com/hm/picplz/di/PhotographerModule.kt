package com.hm.picplz.di

import com.hm.picplz.data.repository.PhotographerRepository
import com.hm.picplz.data.repository.PhotographerRepositoryImpl
import com.hm.picplz.data.source.PhotographerService
import com.hm.picplz.data.source.PhotographerServiceImpl
import com.hm.picplz.data.source.PhotographerSource
import com.hm.picplz.data.source.PhotographerSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PhotographerModule {
    @Binds
    @Singleton
    abstract fun bindPhotographerRepository(
        photographerRepositoryImpl: PhotographerRepositoryImpl
    ): PhotographerRepository

    @Binds
    @Singleton
    abstract fun bindPhotographerSource(
        photographerSourceImpl: PhotographerSourceImpl
    ): PhotographerSource

    @Binds
    @Singleton
    abstract fun bindPhotographerService(
        photographerServiceImpl: PhotographerServiceImpl
    ): PhotographerService
}