package com.example.moviesearchapplication.presentation.viewmodel

import androidx.lifecycle.*
import com.example.moviesearchapplication.App
import com.example.moviesearchapplication.data.FilmRepository
import com.example.moviesearchapplication.data.model.entities.FavoriteFilm
import com.example.moviesearchapplication.data.model.entities.Film
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FilmListViewModel: ViewModel() {

    private val repository: FilmRepository

    val allFilms :LiveData<List<Film>>
    private val favorite : LiveData<List<FavoriteFilm>>
    val favoriteFilms: LiveData<List<Film>>
    var error = MutableLiveData<String>()
    private set
    var loadingLiveData = MutableLiveData<Boolean>()
    private set


    private var totalPages: Int
    private var currentPage: Int
    var isLastPage = false

    init {
        val dao = App.instance.db.getFilmDao()
        val favoriteDao = App.instance.db.getFavoriteFilmDao()
        repository = FilmRepository(dao, favoriteDao)
        allFilms = repository.allFilms
        favorite = repository.favoriteFilms
        favoriteFilms = Transformations.map(favorite) {
            it.map { favoriteFilm-> Film(favoriteFilm) }
        }

        totalPages = 1
        currentPage = 1

        loadingLiveData.value = true
        loadFilms()

    }

    private fun update(film: Film) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(film)
    }
    private fun loadFilms() = viewModelScope.launch(Dispatchers.IO) {
        repository.getFilms(currentPage, object: FilmRepository.PageCountCallback {
            override fun onSuccess(count: Int) {
                totalPages = count
                error.postValue("")
                loadingLiveData.postValue(false)
            }

            override fun onFailure(e: String) {
                currentPage -= 1
                error.postValue(e)
                loadingLiveData.postValue(false)
            }
        })
    }

    fun loadNextPageOnScroll() {
        if (currentPage == totalPages) {
            isLastPage = true
        } else
            if (currentPage < totalPages) {
            loadingLiveData.value = true
            currentPage += 1
            loadFilms()
        }
    }

    fun tryLoadDataAgain() {
        if (currentPage == totalPages)
            currentPage = 0

        if (currentPage < totalPages && !loadingLiveData.value!!) {
            loadingLiveData.value = true
            currentPage += 1
            loadFilms()
        }
    }

    fun addToFavorite(filmItem: Film) {
        update(filmItem)
        val favoriteFilm = FavoriteFilm(filmItem)
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertFavorite(favoriteFilm)
        }

    }

    fun removeFromFavorite(filmItem: Film) {
        update(filmItem)
        val favoriteFilm = FavoriteFilm(filmItem)
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFavorite(favoriteFilm)
        }
    }

    fun updateLikeState(filmItem: Film) {
        if (filmItem.isFavorite)
            addToFavorite(filmItem)
        else
            removeFromFavorite(filmItem)
    }

}