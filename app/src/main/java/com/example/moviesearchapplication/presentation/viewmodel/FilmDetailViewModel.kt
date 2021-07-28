package com.example.moviesearchapplication.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesearchapplication.App
import com.example.moviesearchapplication.data.FilmRepository
import com.example.moviesearchapplication.data.model.entities.Film
import java.util.concurrent.Executors

class FilmDetailViewModel(private val filmId: Int): ViewModel() {

    private val repository:FilmRepository = FilmRepository(App.instance.db.getFilmDao(), App.instance.db.getFavoriteFilmDao())

    private val _film = MutableLiveData<Film>()
    val film: LiveData<Film> = _film

    init {
        Executors.newSingleThreadScheduledExecutor().execute {
            fillFilmViewModel(filmId)
        }
    }

    private fun fillFilmViewModel(id: Int) {
        _film.postValue(repository.getFilmById(id))
    }

}