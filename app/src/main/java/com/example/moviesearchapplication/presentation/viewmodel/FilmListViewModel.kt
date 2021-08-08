package com.example.moviesearchapplication.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesearchapplication.App
import com.example.moviesearchapplication.R
import com.example.moviesearchapplication.data.FilmRepository
import com.example.moviesearchapplication.data.model.entities.FavoriteFilm
import com.example.moviesearchapplication.data.model.entities.Film
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

class FilmListViewModel @Inject constructor(private val repository: FilmRepository): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val allFilms = MutableLiveData<List<Film>>()
    val favoriteFilms = MutableLiveData<List<Film>>()
    val error = MutableLiveData<String>()
    val loadingLiveData = MutableLiveData<Boolean>()

    private var totalPages: LiveData<Int> = repository.pageCount
    private var currentPage: Int
    var isLastPage = false

    init {
        currentPage = 1
        loadingLiveData.value = true

        val disposableAllFilms = repository.allFilms
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .subscribe(
                {films ->
                    error.postValue("")
                    allFilms.postValue(films)},
                {e -> error.postValue(App.instance.resources.getString(R.string.db_error))}
            )
        val disposableFavoriteFilm = repository.favoriteFilms
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .subscribe(
                {films ->
                    error.postValue("")
                    favoriteFilms.postValue( films.map {Film(it)} )},
                {e -> error.postValue(App.instance.resources.getString(R.string.db_error))}
            )
        compositeDisposable.addAll(disposableAllFilms, disposableFavoriteFilm)

        loadFilms()
    }

    private fun update(film: Film): Completable = repository.update(film)

    private fun loadFilms() {
        val d = repository.getFilmList(currentPage)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .subscribe(
                {
                    error.postValue("")
                    loadingLiveData.postValue(false)
                },
                {
                    currentPage -= 1
                    loadingLiveData.postValue(false)
                    val errorString =
                        if (it is HttpException) {
                            when (val errorCode = it.code()) {
                                in (400..451) -> App.instance.resources.getString(R.string.request_error) + " ($errorCode)"
                                in (500..511) -> App.instance.resources.getString(R.string.server_error) + " ($errorCode)"
                                else -> "$errorCode error"
                            }
                        } else if (it is UnknownHostException) App.instance.resources.getString(R.string.connection_error)
                        else App.instance.resources.getString(R.string.unknown_error)
                    error.postValue(errorString)
                }
            )
        compositeDisposable.add(d)
    }

    fun loadNextPageOnScroll() {
        if (currentPage == totalPages.value?:1) {
            isLastPage = true
        } else
            if (currentPage < totalPages.value?:1) {
            loadingLiveData.value = true
            currentPage += 1
            loadFilms()
        }
    }

    fun tryLoadDataAgain() {
        if (currentPage == totalPages.value?:1) {
            currentPage = 0
            loadingLiveData.value = false
        }

        if (currentPage < totalPages.value?:1 && !loadingLiveData.value!!) {
            loadingLiveData.value = true
            currentPage += 1
            loadFilms()
        }
    }

    fun addToFavorite(filmItem: Film) {
        update(filmItem)
            .andThen(repository.insertFavorite(FavoriteFilm(filmItem)))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .subscribe()
    }

    fun removeFromFavorite(filmItem: Film) {
        update(filmItem)
            .andThen( repository.deleteFavorite(FavoriteFilm(filmItem)))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .subscribe()
    }

    fun updateLikeState(filmItem: Film) {
        if (filmItem.isFavorite)
            addToFavorite(filmItem)
        else
            removeFromFavorite(filmItem)
    }

    fun resetWatchLaterState(id: Int) {
        repository.getFilmById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .flatMapCompletable { film ->
                film.isWatchingLater = false
                update(film) }
            .subscribe()
    }

}