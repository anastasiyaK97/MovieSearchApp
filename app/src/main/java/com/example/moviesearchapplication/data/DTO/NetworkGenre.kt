package com.example.moviesearchapplication.data.DTO

import com.google.gson.annotations.SerializedName

data class NetworkGenre(
    /*@SerializedName("id")
    val id: Int,*/
    @SerializedName("genre")
    val name: String
)