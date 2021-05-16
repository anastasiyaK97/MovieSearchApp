package com.example.moviesearchapplication.domain

import com.example.moviesearchapplication.data.model.Film
import retrofit2.Call
import retrofit2.http.GET

interface FilmApiService {
    @GET("films")
    fun getFilms() : Call<List<Film>>
}