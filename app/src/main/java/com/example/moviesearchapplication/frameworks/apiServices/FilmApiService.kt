package com.example.moviesearchapplication.frameworks.apiServices

import com.example.moviesearchapplication.data.DTO.FilmsWithPageCount
import com.example.moviesearchapplication.data.DTO.NetworkFilm
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FilmApiService {
    @GET("films/{id}")
    fun getFilmById(@Path("id") id: Int): Single<NetworkFilm>

    @GET("api/v2.2/films/top")
    fun getTopFilms(
        @Query("type") type: String = "TOP_100_POPULAR_FILMS",
        @Query("page") page: Int = 1
    ): Flowable<FilmsWithPageCount>
}