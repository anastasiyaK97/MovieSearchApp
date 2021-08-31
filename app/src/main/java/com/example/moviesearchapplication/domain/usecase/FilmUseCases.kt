package com.example.moviesearchapplication.domain.usecase

import com.example.moviesearchapplication.data.FilmRepository
import com.example.moviesearchapplication.data.model.entities.FavoriteFilm
import com.example.moviesearchapplication.data.model.entities.Film
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class FilmUseCases @Inject constructor(private val repository: FilmRepository) {

    fun getFilmById(id: Int): Single<Film> = repository.getFilmById(id)

    fun updateFilm(film: Film): Completable = repository.update(film)
    fun addFilmToFavorite(film: Film): Completable {
        return repository.update(film)
            .andThen(repository.insertFavorite(FavoriteFilm(film)))
    }
    fun removeFilmFromFavorite(film: Film): Completable {
        return repository.update(film)
            .andThen(repository.deleteFavorite(FavoriteFilm(film)))
    }

    fun resetWatchLaterFilmState(id: Int): Completable = getFilmById(id)
        .flatMapCompletable { film ->
            film.isWatchingLater = false
            updateFilm(film)
        }
}