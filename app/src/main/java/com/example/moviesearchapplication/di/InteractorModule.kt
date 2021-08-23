package com.example.moviesearchapplication.di

import com.example.moviesearchapplication.data.MoviesLocalDataSource
import com.example.moviesearchapplication.data.MoviesRemoteDataSource
import com.example.moviesearchapplication.frameworks.apiServices.FilmApiService
import com.example.moviesearchapplication.frameworks.database.FavoriteFilmDAO
import com.example.moviesearchapplication.frameworks.database.FilmDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [NetModule::class, RoomModule::class])
class InteractorModule {
    @Singleton
    @Provides
    fun providesMoviesLocalDataSource(dao: FilmDao, favoriteDao: FavoriteFilmDAO): MoviesLocalDataSource {
        return MoviesLocalDataSource(dao, favoriteDao)
    }

    @Singleton
    @Provides
    fun providesMoviesRemoteDataSource(apiService: FilmApiService): MoviesRemoteDataSource {
        return MoviesRemoteDataSource(apiService)
    }
}