package com.example.moviesearchapplication.domain.usecase

import com.example.moviesearchapplication.data.FilmRepository
import javax.inject.Inject

class GetFilmListUseCase @Inject constructor(private val repository: FilmRepository) {
    val pageCount = repository.pageCount
    fun getAllFilms() = repository.allFilms
    fun requestFilmListInPage(page: Int) = repository.getFilmList(page)

}