package com.example.moviesearchapplication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesearchapplication.data.model.entities.Film
import com.example.moviesearchapplication.domain.usecase.FilmUseCases
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SetUpWatchLaterViewModel(private val filmUseCases: FilmUseCases) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    var film = MutableLiveData<Film>()
        private set

    fun setFilm(id: Int) {
        val d = filmUseCases.getFilmById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .subscribe(
                {film.postValue(it)},
                {e -> Log.d("Error", e.message?:e.stackTraceToString())}
            )
        compositeDisposable.add(d)
    }

    fun updateNotificationSettings() {
        if (film.value != null && !film.value?.isWatchingLater!!) {
            film.value?.isWatchingLater = true
            val d = filmUseCases.updateFilm(film.value!!)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(
                    {},
                    {e -> Log.d("Error", e.message?:e.stackTraceToString())})
            compositeDisposable.add(d)
        }
    }
}