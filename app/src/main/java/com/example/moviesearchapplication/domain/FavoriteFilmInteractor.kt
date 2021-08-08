package com.example.moviesearchapplication.domain

import com.example.moviesearchapplication.data.model.entities.FavoriteFilm
import com.example.moviesearchapplication.frameworks.database.FavoriteFilmDAO
import io.reactivex.Flowable
import javax.inject.Inject

class FavoriteFilmInteractor @Inject constructor(private val dao: FavoriteFilmDAO) {

    fun getFavoriteFilms(): Flowable<List<FavoriteFilm>> {
        return dao.getAll()
    }

}