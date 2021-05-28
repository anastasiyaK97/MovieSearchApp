package com.example.moviesearchapplication.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesearchapplication.App
import com.example.moviesearchapplication.data.FilmRepository
import com.example.moviesearchapplication.data.model.entities.Film

class FilmDetailViewModel: ViewModel() {

    private val repository:FilmRepository = FilmRepository(App.instance.db.getFilmDao())
    var film = MutableLiveData<Film>()

    fun fillFilmViewModel(id: Int) {
        film.postValue(repository.getFilmById(id))
    }

}