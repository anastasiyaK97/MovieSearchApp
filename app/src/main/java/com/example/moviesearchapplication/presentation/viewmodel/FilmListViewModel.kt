package com.example.moviesearchapplication.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesearchapplication.App
import com.example.moviesearchapplication.R
import com.example.moviesearchapplication.data.model.entities.Film
import com.example.moviesearchapplication.domain.usecase.FilmUseCases
import com.example.moviesearchapplication.domain.usecase.GetFavoriteFilmListUseCase
import com.example.moviesearchapplication.domain.usecase.GetFilmListUseCase
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

class FilmListViewModel @Inject constructor(
    private val filmUseCases: FilmUseCases,
    private val filmListUseCase: GetFilmListUseCase,
    private val favoriteFilmListUseCase: GetFavoriteFilmListUseCase
): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val allFilms = MutableLiveData<List<Film>>()
    val favoriteFilms = MutableLiveData<List<Film>>()
    val error = MutableLiveData<String>()
    val loadingLiveData = MutableLiveData<Boolean>()

    private var totalPages: LiveData<Int> = filmListUseCase.pageCount
    private var currentPage: Int
    var isLastPage = false

    init {
        currentPage = 1
        loadingLiveData.value = true

        val disposableAllFilms = filmListUseCase.getAllFilms()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .subscribe(
                {films ->
                    error.postValue("")
                    allFilms.postValue(films)},
                {e -> error.postValue(App.instance.resources.getString(R.string.db_error))}
            )
        val disposableFavoriteFilm = favoriteFilmListUseCase.getAllFavoriteFilms()
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

    private fun update(film: Film): Completable = filmUseCases.updateFilm(film)

    private fun loadFilms() {
        val d = filmListUseCase.requestFilmListInPage(currentPage)
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
        filmUseCases.addFilmToFavorite(filmItem)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .subscribe()
    }

    fun removeFromFavorite(filmItem: Film) {
        filmUseCases.removeFilmFromFavorite(filmItem)
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
        val disposable = filmUseCases.resetWatchLaterFilmState(id)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .subscribe({}, {})
        compositeDisposable.add(disposable)
    }
    fun getFilmById(id: Int): Single<Film> {
       return filmUseCases.getFilmById(id)
    }

    fun updateFilmNotificationSettings(id: Int) {
        val disposable = filmUseCases.getFilmById(id)
            .flatMapCompletable { film ->
                if (!film.isWatchingLater)
                film.isWatchingLater = true
                update(film)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .subscribe({}, {})
        compositeDisposable.add(disposable)
    }

}