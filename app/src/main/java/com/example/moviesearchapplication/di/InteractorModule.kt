package com.example.moviesearchapplication.di

import com.example.moviesearchapplication.domain.FilmInteractor
import com.example.moviesearchapplication.frameworks.apiServices.FilmApiService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [NetModule::class])
class InteractorModule {
    @Singleton
    @Provides
    fun providesInteractor(apiService: FilmApiService): FilmInteractor {
        return FilmInteractor(apiService)
    }
}