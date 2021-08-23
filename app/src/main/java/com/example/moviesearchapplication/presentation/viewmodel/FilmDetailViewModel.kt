package com.example.moviesearchapplication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesearchapplication.data.model.entities.Film
import com.example.moviesearchapplication.domain.usecase.FilmUseCases
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FilmDetailViewModel @Inject constructor(private val filmUseCases: FilmUseCases) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    var film = MutableLiveData<Film>()
        private set

    fun setFilm(filmId: Int) {
        val d = filmUseCases.getFilmById(filmId)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .subscribe(
                {film.postValue(it)},
                {e -> Log.d("Error", e.message?:e.stackTraceToString())}
            )
        compositeDisposable.add(d)
    }
}