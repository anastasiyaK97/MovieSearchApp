package com.example.moviesearchapplication.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesearchapplication.App
import com.example.moviesearchapplication.data.FilmRepository
import com.example.moviesearchapplication.data.model.entities.Film
import java.util.concurrent.Executors

class SetUpWatchLaterViewModel(private val id: Int) : ViewModel() {

    private val repository: FilmRepository = FilmRepository(App.instance.db.getFilmDao(), App.instance.db.getFavoriteFilmDao())

    var film = MutableLiveData<Film>()
        private set

    init {
        Executors.newSingleThreadScheduledExecutor().execute {
            fillFilmViewModel(id)
        }
    }

    private fun fillFilmViewModel(id: Int) {
        film.postValue(repository.getFilmById(id))
    }

    fun updateNotificationSettings() {
        if (film.value != null && !film.value?.isWatchingLater!!) {
            film.value?.isWatchingLater = true
            repository.update(film.value!!)
        }
    }
}