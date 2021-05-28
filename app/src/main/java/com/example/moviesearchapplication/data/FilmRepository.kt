package com.example.moviesearchapplication.data

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviesearchapplication.App
import com.example.moviesearchapplication.data.DTO.DataMapper.FilmMapper
import com.example.moviesearchapplication.data.DTO.NetworkFilm
import com.example.moviesearchapplication.data.model.entities.Film
import com.example.moviesearchapplication.domain.FilmInteractor
import com.example.moviesearchapplication.frameworks.database.FilmDao
import java.util.concurrent.Executors

const val TAG = "LOG_TAG"

class FilmRepository(private val filmDAO: FilmDao) {

    val allFilms: LiveData<List<Film>> = filmDAO.getAll()
    val favoriteFilms: LiveData<List<Film>> = filmDAO.getAllFavorite()
    val error = MutableLiveData<String>()

    private val filmInteractor = App.instance.filmInteractor
    private val filmMapper = FilmMapper()

    private fun addToCache(collection: List<NetworkFilm>){

        Executors.newSingleThreadScheduledExecutor().execute{
            collection.forEach{
                filmDAO.insert(filmMapper.map(it))
                Log.d(TAG, "${it.title} (${it.id}) was inserted to DB")
            }
        }
    }
    private fun getCachedFilms() : LiveData<List<Film>> {
        return filmDAO.getAll()
    }

    private fun requestNetworkFilms(
        page: Int,
        callback: PageCountCallback
    ){
        filmInteractor.getFilms(page, object: FilmInteractor.GetFilmCallback{
            override fun onSuccess(pagesCount: Int, networkFilms: List<NetworkFilm>) {
                addToCache(networkFilms)
                callback.onSuccess(pagesCount)
                Log.d("LOG_TAG", "request completed, totalPages = $pagesCount")
            }

            override fun onError(error: String) {
                this@FilmRepository.error.value = error
            }

        })
    }

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

    fun update(film: Film) = Executors.newSingleThreadScheduledExecutor().execute{
        filmDAO.update(film)
    }

    interface PageCountCallback {
        fun onSuccess(count: Int)
    }

}