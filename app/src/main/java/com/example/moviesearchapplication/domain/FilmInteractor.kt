package com.example.moviesearchapplication.domain

import com.example.moviesearchapplication.data.DTO.FilmsWithPage
import com.example.moviesearchapplication.data.DTO.NetworkFilm
import com.example.moviesearchapplication.frameworks.apiServices.FilmApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilmInteractor(
    private val apiService: FilmApiService,
    //private val db: RoomDB
) {
    fun getFilms(page: Int = 1,
        callback: GetFilmCallback
    ){
        apiService.getTopFilms(page = page)
            .enqueue(object: Callback<FilmsWithPage> {
                override fun onFailure(call: Call<FilmsWithPage>, t: Throwable) {
                    callback.onError("Error ${t.message}")
                }

                override fun onResponse(call: Call<FilmsWithPage>, response: Response<FilmsWithPage>) {
                    if (response.isSuccessful) {
                        callback.onSuccess(response.body()!!.pagesCount, response.body()!!.films)
                    } else {
                        callback.onError("Error ${response.code()}")
                    }
                }
            })
    }

    interface GetFilmCallback{
        fun onSuccess(pagesCount: Int, networkFilms: List<NetworkFilm>)
        fun onError(error: String)
    }

}