package com.example.moviesearchapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModelFactory(private val filmId: Int) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilmDetailViewModel::class.java)) {
            return FilmDetailViewModel(filmId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}