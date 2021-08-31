package com.example.moviesearchapplication.di

import com.example.moviesearchapplication.domain.FavoriteFilmInteractor
import com.example.moviesearchapplication.domain.FilmInteractor
import com.example.moviesearchapplication.frameworks.apiServices.FilmApiService
import com.example.moviesearchapplication.frameworks.database.FavoriteFilmDAO
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [NetModule::class, RoomModule::class])
class InteractorModule {
    @Singleton
    @Provides
    fun providesInteractor(apiService: FilmApiService): FilmInteractor {
        return FilmInteractor(apiService)
    }

    @Singleton
    @Provides
    fun providesFavoriteFilmInteractor(dao: FavoriteFilmDAO): FavoriteFilmInteractor {
        return FavoriteFilmInteractor(dao)
    }
}