package com.example.moviesearchapplication.domain

import com.example.moviesearchapplication.data.DTO.FilmsWithPageCount
import com.example.moviesearchapplication.frameworks.apiServices.FilmApiService
import io.reactivex.Flowable
import javax.inject.Inject

class FilmInteractor @Inject constructor(private val apiService: FilmApiService) {

//    private val remoteConfig = Firebase.remoteConfig

    fun getFilms(page: Int = 1): Flowable<FilmsWithPageCount> {
        val topCategory = "TOP_100_POPULAR_FILMS"//remoteConfig.getString(FeatureToggles.topCategory)
        //Log.d("LOG_TAG", "Loaded films - $topCategory")

        return apiService.getTopFilms(page = page, type = topCategory)
    }
}