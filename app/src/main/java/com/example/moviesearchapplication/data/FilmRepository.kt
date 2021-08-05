package com.example.moviesearchapplication.data

import androidx.lifecycle.MutableLiveData
import com.example.moviesearchapplication.App
import com.example.moviesearchapplication.data.DTO.DataMapper.FilmMapper
import com.example.moviesearchapplication.data.DTO.NetworkFilm
import com.example.moviesearchapplication.data.model.entities.FavoriteFilm
import com.example.moviesearchapplication.data.model.entities.Film
import com.example.moviesearchapplication.domain.FilmInteractor
import com.example.moviesearchapplication.frameworks.database.FavoriteFilmDAO
import com.example.moviesearchapplication.frameworks.database.FilmDao
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

const val TAG = "LOG_TAG"

class FilmRepository @Inject constructor(private val filmDAO: FilmDao, private val favoriteFilmDAO: FavoriteFilmDAO) {

    val filmInteractor: FilmInteractor = App.instance.applicationComponent.filmInteractor()

    val allFilms: Flowable<List<Film>> = filmDAO.getAll()
    val favoriteFilms: Flowable<List<FavoriteFilm>> = favoriteFilmDAO.getAll()
    //val error = MutableLiveData<String>()
    val pageCount = MutableLiveData<Int>()
    private val filmMapper = FilmMapper()

    fun getFilmById(id: Int): Single<Film> = filmDAO.getFilmById(id)

    fun getFilmList(loadedPage: Int): Flowable<List<Film>> {
        return requestNetworkFilms(loadedPage)
            .subscribeOn(Schedulers.io())
            .flatMap { apiFilmList ->
                addToCache(apiFilmList)
                    .andThen(getCachedFilms())
            }
    }

    private fun addToCache(collection: List<NetworkFilm>): Completable {
        val filmCollection = collection.map {
            filmMapper.map(it)
        }
        fillIsFavoriteFieldInList(filmCollection)
        return filmDAO.insertList(filmCollection)
    }

    private fun fillIsFavoriteFieldInList(collection: List<Film>) {
        val idList = collection.map { it.id }
        val map = favoriteFilmDAO.checkIfFilmsAreFavorites(idList).associateBy { it.id }
        collection.forEach {
            if (map.containsKey(it.id))
                it.isFavorite = true
        }
    }

    private fun getCachedFilms(): Flowable<List<Film>> = filmDAO.getAll()

    private fun requestNetworkFilms(page: Int): Flowable<List<NetworkFilm>> =
        filmInteractor.getFilms(page)
            .flatMap { it ->
                pageCount.postValue(it.pagesCount)
                Flowable.just(it.films)
            }

    fun update(film: Film): Completable = filmDAO.update(film)

    fun insertFavorite(favoriteFilm: FavoriteFilm): Completable =
        favoriteFilmDAO.insert(favoriteFilm)

    fun deleteFavorite(favoriteFilm: FavoriteFilm): Completable =
        favoriteFilmDAO.delete(favoriteFilm)

}