package com.example.moviesearchapplication.data.DTO

import com.google.gson.annotations.SerializedName

data class FilmsWithPageCount (
    @SerializedName("pagesCount")
    val pagesCount: Int,
    @SerializedName("films")
    val films: List<NetworkFilm>
)