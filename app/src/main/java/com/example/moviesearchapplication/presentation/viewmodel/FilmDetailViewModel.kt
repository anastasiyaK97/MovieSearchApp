package com.example.moviesearchapplication.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesearchapplication.App
import com.example.moviesearchapplication.data.FilmRepository
import com.example.moviesearchapplication.data.model.entities.Film
import java.util.concurrent.Executors

class FilmDetailViewModel(private val filmId: Int): ViewModel() {

    private val repository:FilmRepository = FilmRepository(App.instance.db.getFilmDao(), App.instance.db.getFavoriteFilmDao())

    var film = MutableLiveData<Film>()
    init {
        Executors.newSingleThreadScheduledExecutor().execute {
            fillFilmViewModel(filmId)
        }
    }

    private fun fillFilmViewModel(id: Int) {
        film.postValue(repository.getFilmById(id))
    }

}