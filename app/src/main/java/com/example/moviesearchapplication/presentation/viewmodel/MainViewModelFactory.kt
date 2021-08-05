package com.example.moviesearchapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviesearchapplication.data.FilmRepository

class MainViewModelFactory(val repository: FilmRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilmDetailViewModel::class.java)) {
            return FilmDetailViewModel(repository) as T
        } else
        if (modelClass.isAssignableFrom(SetUpWatchLaterViewModel::class.java)) {
            return SetUpWatchLaterViewModel(repository) as T
        } else
        if (modelClass.isAssignableFrom(FilmListViewModel::class.java)) {
            return FilmListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}