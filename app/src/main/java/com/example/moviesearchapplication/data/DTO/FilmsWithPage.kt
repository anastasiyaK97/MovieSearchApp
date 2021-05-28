package com.example.moviesearchapplication.data.DTO

import com.google.gson.annotations.SerializedName

data class FilmsWithPage (
    @SerializedName("pagesCount")
    val pagesCount: Int,
    @SerializedName("films")
    val films: List<NetworkFilm>
)