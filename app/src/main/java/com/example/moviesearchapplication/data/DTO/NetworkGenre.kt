package com.example.moviesearchapplication.data.DTO

import com.google.gson.annotations.SerializedName

data class NetworkGenre(
    @SerializedName("genre")
    val name: String
)