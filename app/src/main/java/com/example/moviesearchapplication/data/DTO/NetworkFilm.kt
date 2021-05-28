package com.example.moviesearchapplication.data.DTO

import com.google.gson.annotations.SerializedName

data class NetworkFilm(
    @SerializedName("filmId")
    val id: Int,
    @SerializedName("nameRu")
    val title: String,
    @SerializedName("nameEn")
    val originalTitle: String?,
    @SerializedName("year")
    val year: String,
    @SerializedName("posterUrl")
    val posterLink: String,
    @SerializedName("genres")
    var networkGenres : List<NetworkGenre>?
)