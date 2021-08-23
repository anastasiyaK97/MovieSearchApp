package com.example.moviesearchapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviesearchapplication.domain.usecase.FilmUseCases
import com.example.moviesearchapplication.domain.usecase.GetFavoriteFilmListUseCase
import com.example.moviesearchapplication.domain.usecase.GetFilmListUseCase

class MainViewModelFactory(val filmUseCases: FilmUseCases,
                           val filmListUseCase: GetFilmListUseCase,
                           val favoriteFilmListUseCase: GetFavoriteFilmListUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilmDetailViewModel::class.java)) {
            return FilmDetailViewModel(filmUseCases) as T
        } else
        if (modelClass.isAssignableFrom(SetUpWatchLaterViewModel::class.java)) {
            return SetUpWatchLaterViewModel(filmUseCases) as T
        } else
        if (modelClass.isAssignableFrom(FilmListViewModel::class.java)) {
            return FilmListViewModel(filmUseCases, filmListUseCase, favoriteFilmListUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}