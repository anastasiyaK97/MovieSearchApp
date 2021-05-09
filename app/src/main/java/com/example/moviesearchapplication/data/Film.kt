package com.example.moviesearchapplication.data

data class Film(
    val id: Int,
    val name: String,
    val descr: String,
    val imageID: Int,
    var isFavorite: Boolean = false
)
