package com.example.moviesearchapplication.domain

import com.example.moviesearchapplication.FeatureToggles
import com.example.moviesearchapplication.data.DTO.FilmsWithPageCount
import com.example.moviesearchapplication.frameworks.apiServices.FilmApiService
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import io.reactivex.Flowable
import javax.inject.Inject

class FilmInteractor @Inject constructor(private val apiService: FilmApiService) {

    val remoteConfig = Firebase.remoteConfig

    fun getFilms(page: Int = 1): Flowable<FilmsWithPageCount> {
        val topCategory = remoteConfig.getString(FeatureToggles.topCategory)
        //Log.d("LOG_TAG", "Loaded films - $topCategory")

        return apiService.getTopFilms(page = page, type = topCategory)
    }
}