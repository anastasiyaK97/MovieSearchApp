package com.example.moviesearchapplication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesearchapplication.App
import com.example.moviesearchapplication.data.FilmRepository
import com.example.moviesearchapplication.data.model.entities.Film
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FilmListViewModel: ViewModel() {
    val ITEMS_ON_PAGE_COUNT = 100

    private val repository: FilmRepository

    val allFilms :LiveData<List<Film>>
    val favoriteFilms :LiveData<List<Film>>
    val error : LiveData<String>

    private var totalPages: Int
    private var currentPage: Int
    var isLoading = false
    var isLastPage = false

    init {
        val dao = App.instance.db.getFilmDao()
        repository = FilmRepository(dao)
        allFilms = repository.allFilms
        favoriteFilms = repository.favoriteFilms
        error = repository.error
        totalPages = 1
        currentPage = 1
        getFilms()

    }

    fun update(film: Film) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(film)
    }
    private fun getFilms() = viewModelScope.launch(Dispatchers.IO) {
        repository.getFilms(currentPage, object: FilmRepository.PageCountCallback {
            override fun onSuccess(count: Int) {
                isLoading = false
                totalPages = count
                Log.d("LOG_TAG", "callback method in viewModel. totalPages = $totalPages")
            }

        })
    }

    fun loadPageOnScroll() {
        if (currentPage < totalPages && !isLoading) {
            isLoading = true
            currentPage += 1
            Log.d("LOG_TAG1", "scroll listener. page = $currentPage")
            getFilms()
        }
    }

    fun removeFromFavorite(filmItem: Film) {
        update(filmItem)
    }

    fun addToFavorite(filmItem: Film) {
        update(filmItem)
    }

}