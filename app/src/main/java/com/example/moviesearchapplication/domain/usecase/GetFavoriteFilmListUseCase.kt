package com.example.moviesearchapplication.domain.usecase

import com.example.moviesearchapplication.data.FilmRepository
import javax.inject.Inject

class GetFavoriteFilmListUseCase @Inject constructor(private val repository: FilmRepository) {
    fun getAllFavoriteFilms() = repository.favoriteFilms
}