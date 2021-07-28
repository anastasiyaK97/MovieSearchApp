package com.example.moviesearchapplication.data.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "favorite_films")
data class FavoriteFilm (
    @PrimaryKey
    val id: Int,
    val title: String,
    val originalTitle: String? = "",
    val year: String,
    val posterLink: String,
    @ColumnInfo(name = "genresArray")
    var genres : List<Genre>?
) {
    constructor(film: Film) : this(film.id, film.title, film.originalTitle, film.year, film.posterLink, film.genres)
}