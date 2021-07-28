package com.example.moviesearchapplication.domain

import com.example.moviesearchapplication.App
import com.example.moviesearchapplication.R
import com.example.moviesearchapplication.data.DTO.FilmsWithPageCount
import com.example.moviesearchapplication.data.DTO.NetworkFilm
import com.example.moviesearchapplication.frameworks.apiServices.FilmApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilmInteractor(private val apiService: FilmApiService) {

    fun getFilms(page: Int = 1, callback: GetFilmCallback) {
        apiService.getTopFilms(page = page)
            .enqueue(object: Callback<FilmsWithPageCount> {
                override fun onFailure(call: Call<FilmsWithPageCount>, t: Throwable) {
                    callback.onError(App.instance.resources.getString(R.string.connection_error))
                }

                override fun onResponse(call: Call<FilmsWithPageCount>, response: Response<FilmsWithPageCount>) {
                    if (response.isSuccessful) {
                        callback.onSuccess(response.body()!!.pagesCount, response.body()!!.films)
                    } else {
                        callback.onError(App.instance.resources.getString(R.string.server_error))
                    }
                }
            })
    }

    interface GetFilmCallback{
        fun onSuccess(pagesCount: Int, networkFilms: List<NetworkFilm>)
        fun onError(error: String)
    }

}