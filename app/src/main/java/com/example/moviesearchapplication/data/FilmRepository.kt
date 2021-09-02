package com.example.moviesearchapplication.data

import androidx.lifecycle.MutableLiveData
import com.example.moviesearchapplication.data.DTO.DataMapper.FilmMapper
import com.example.moviesearchapplication.data.DTO.NetworkFilm
import com.example.moviesearchapplication.data.model.entities.FavoriteFilm
import com.example.moviesearchapplication.data.model.entities.Film
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

const val TAG = "LOG_TAG"

class FilmRepository @Inject constructor(
private val localSource: MoviesLocalDataSource,
private val remoteSource: MoviesRemoteDataSource,

) {
    val allFilms: Flowable<List<Film>> = localSource.getAllMovies()
    val favoriteFilms: Flowable<List<FavoriteFilm>> = localSource.getAllFavoriteMovies()

    val pageCount = MutableLiveData<Int>()
    private val filmMapper = FilmMapper()

    fun getFilmById(id: Int): Single<Film> = localSource.getFilmById(id)

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
            .zipWith(localSource.getAllFavoriteMovies(), zipper)
            .flatMap { filmList ->
                addToCache(filmList)
                    .andThen(getCachedFilms())
            }
    }

    fun requestNetworkFilms(page: Int = 1): Flowable<List<NetworkFilm>> =
        remoteSource.getFilms(page)
            .flatMap { it ->
                pageCount.postValue(it.pagesCount)
                Flowable.just(it.films)
            }

    private fun addToCache(collection: List<Film>): Completable {
        return localSource.insertList(collection)
    }

    private fun getCachedFilms(): Flowable<List<Film>> = localSource.getAllMovies()

    fun update(film: Film): Completable = localSource.update(film)

    fun insertFavorite(favoriteFilm: FavoriteFilm): Completable =
        localSource.insertFavorite(favoriteFilm)

    fun deleteFavorite(favoriteFilm: FavoriteFilm): Completable =
        localSource.deleteFavorite(favoriteFilm)

}
