package com.example.moviesearchapplication.data

class Film(
    val id: Int,
    val name: String,
    val descr: String,
    val imageID: Int,
    var isFavorite: Boolean = false
)