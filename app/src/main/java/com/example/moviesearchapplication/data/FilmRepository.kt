package com.example.moviesearchapplication.data

import androidx.lifecycle.MutableLiveData
import com.example.moviesearchapplication.App
import com.example.moviesearchapplication.data.DTO.DataMapper.FilmMapper
import com.example.moviesearchapplication.data.DTO.NetworkFilm
import com.example.moviesearchapplication.data.model.entities.FavoriteFilm
import com.example.moviesearchapplication.data.model.entities.Film
import com.example.moviesearchapplication.domain.FavoriteFilmInteractor
import com.example.moviesearchapplication.domain.FilmInteractor
import com.example.moviesearchapplication.frameworks.database.FavoriteFilmDAO
import com.example.moviesearchapplication.frameworks.database.FilmDao
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

const val TAG = "LOG_TAG"

class FilmRepository @Inject constructor(
    private val filmDAO: FilmDao,
    private val favoriteFilmDAO: FavoriteFilmDAO
) {

    private val filmInteractor: FilmInteractor = App.instance.applicationComponent.filmInteractor()
    private val favoriteFilmInteractor: FavoriteFilmInteractor =
        App.instance.applicationComponent.favoriteFilmInteractor()

    val allFilms: Flowable<List<Film>> = filmDAO.getAll()
    val favoriteFilms: Flowable<List<FavoriteFilm>> = favoriteFilmDAO.getAll()

    val pageCount = MutableLiveData<Int>()
    private val filmMapper = FilmMapper()

    fun getFilmById(id: Int): Single<Film> = filmDAO.getFilmById(id)

    fun getFilmList(loadedPage: Int): Flowable<List<Film>> {

        val zipper =
            BiFunction<List<NetworkFilm>, List<FavoriteFilm>, List<Film>> { network, favorite ->
                val map = favorite.associateBy { it.id }
                val filmCollection = network.map {
                    filmMapper.map(it)
                }
                filmCollection.forEach {
                    if (map.containsKey(it.id))
                        it.isFavorite = true
                }
                filmCollection
            }

        return requestNetworkFilms(loadedPage)
            .subscribeOn(Schedulers.io())
            .zipWith(favoriteFilmInteractor.getFavoriteFilms(), zipper)
            .flatMap { filmList ->
                addToCache(filmList)
                    .andThen(getCachedFilms())
            }
    }

    private fun requestNetworkFilms(page: Int): Flowable<List<NetworkFilm>> =
        filmInteractor.getFilms(page)
            .flatMap { it ->
                pageCount.postValue(it.pagesCount)
                Flowable.just(it.films)
            }

    private fun addToCache(collection: List<Film>): Completable {
        return filmDAO.insertList(collection)
    }

    private fun getCachedFilms(): Flowable<List<Film>> = filmDAO.getAll()

    fun update(film: Film): Completable = filmDAO.update(film)

    fun insertFavorite(favoriteFilm: FavoriteFilm): Completable =
        favoriteFilmDAO.insert(favoriteFilm)

    fun deleteFavorite(favoriteFilm: FavoriteFilm): Completable =
        favoriteFilmDAO.delete(favoriteFilm)

}