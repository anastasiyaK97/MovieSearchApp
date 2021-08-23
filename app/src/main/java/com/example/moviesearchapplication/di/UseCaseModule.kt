package com.example.moviesearchapplication.di

import com.example.moviesearchapplication.data.FilmRepository
import com.example.moviesearchapplication.domain.usecase.FilmUseCases
import com.example.moviesearchapplication.domain.usecase.GetFavoriteFilmListUseCase
import com.example.moviesearchapplication.domain.usecase.GetFilmListUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UseCaseModule {

    @Provides
    @Singleton
    fun providesFilmUseCase(repository: FilmRepository): FilmUseCases {
        return FilmUseCases(repository)
    }

    @Provides
    @Singleton
    fun providesGetFilmListUseCase(repository: FilmRepository): GetFilmListUseCase {
        return GetFilmListUseCase(repository)
    }

    @Provides
    @Singleton
    fun providesGetFavoriteFilmListUseCase(repository: FilmRepository): GetFavoriteFilmListUseCase {
        return GetFavoriteFilmListUseCase(repository)
    }
}