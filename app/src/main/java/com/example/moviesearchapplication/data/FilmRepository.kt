package com.example.moviesearchapplication.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviesearchapplication.App
import com.example.moviesearchapplication.data.DTO.DataMapper.FilmMapper
import com.example.moviesearchapplication.data.DTO.NetworkFilm
import com.example.moviesearchapplication.data.model.entities.FavoriteFilm
import com.example.moviesearchapplication.data.model.entities.Film
import com.example.moviesearchapplication.domain.FilmInteractor
import com.example.moviesearchapplication.frameworks.database.FavoriteFilmDAO
import com.example.moviesearchapplication.frameworks.database.FilmDao
import java.util.concurrent.Executors

const val TAG = "LOG_TAG"

class FilmRepository(private val filmDAO: FilmDao, private val favoriteFilmDAO: FavoriteFilmDAO) {

    val allFilms: LiveData<List<Film>> = filmDAO.getAll()
    val favoriteFilms: LiveData<List<FavoriteFilm>> = favoriteFilmDAO.getAll()
    var error = MutableLiveData<String>()
    private set

    private val filmInteractor = App.instance.filmInteractor
    private val filmMapper = FilmMapper()

    private fun addToCache(collection: List<NetworkFilm>){

        Executors.newSingleThreadScheduledExecutor().execute {
        val idList = collection.map { it.id }
        val filmsMap = favoriteFilmDAO.checkIfFilmsAreFavorites(idList).associateBy { it.id }

        collection.forEach{
            val new = filmMapper.map(it)
            if (filmsMap.containsKey(new.id)) new.isFavorite = true
            filmDAO.insert(new)
            }
        }
    }
    private fun getCachedFilms() : LiveData<List<Film>> = filmDAO.getAll()

    @WorkerThread
    fun getFilmById(id: Int): Film = filmDAO.getFilmById(id)

    @WorkerThread
    fun getFilms(loadedPage: Int, callback: PageCountCallback): LiveData<List<Film>> {
        return if (filmDAO.getAll().value == null || filmDAO.getAll().value?.count() == 0)
        {
            requestNetworkFilms(loadedPage, callback)
            getCachedFilms()
        } else getCachedFilms()
    }

    private fun requestNetworkFilms(page: Int, callback: PageCountCallback) {
        filmInteractor.getFilms(page, object: FilmInteractor.GetFilmCallback{
            override fun onSuccess(pagesCount: Int, networkFilms: List<NetworkFilm>) {
                addToCache(networkFilms)
                callback.onSuccess(pagesCount)
            }

            override fun onError(error: String) {
                callback.onFailure(error)
                this@FilmRepository.error.value = error
            }

        })
    }

    fun update(film: Film) = Executors.newSingleThreadScheduledExecutor().execute {
        filmDAO.update(film)
    }

    fun insertFavorite(favoriteFilm: FavoriteFilm) = favoriteFilmDAO.insert(favoriteFilm)

    fun deleteFavorite(favoriteFilm: FavoriteFilm) = favoriteFilmDAO.delete(favoriteFilm)


    interface PageCountCallback {
        fun onSuccess(count: Int)
        fun onFailure(error: String)
    }

}