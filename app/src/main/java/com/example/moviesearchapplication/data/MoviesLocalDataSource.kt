package com.example.moviesearchapplication.data

import com.example.moviesearchapplication.data.db.FavoriteFilmDAO
import com.example.moviesearchapplication.data.db.FilmDao
import com.example.moviesearchapplication.data.model.entities.FavoriteFilm
import com.example.moviesearchapplication.data.model.entities.Film
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class MoviesLocalDataSource @Inject constructor(
    private val filmDAO: FilmDao,
    private val favoriteFilmDAO: FavoriteFilmDAO
) {

    fun getAllMovies(): Flowable<List<Film>> = filmDAO.getAll()
    fun getAllFavoriteMovies(): Flowable<List<FavoriteFilm>> = favoriteFilmDAO.getAll()

    fun insertList(collection: List<Film>): Completable {
        return filmDAO.insertList(collection)
    }

    fun getFilmById(id: Int): Single<Film> = filmDAO.getFilmById(id)
    fun update(film: Film): Completable = filmDAO.update(film)

    fun insertFavorite(favoriteFilm: FavoriteFilm): Completable =
        favoriteFilmDAO.insert(favoriteFilm)

    fun deleteFavorite(favoriteFilm: FavoriteFilm): Completable =
        favoriteFilmDAO.delete(favoriteFilm)

}