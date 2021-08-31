package com.example.moviesearchapplication.data.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "film")
data class Film(
        @PrimaryKey
        val id: Int,
        val title: String,
        val originalTitle: String?,
        val year: String,
        val posterLink: String,
        @ColumnInfo(name = "genresArray")
        var genres : List<Genre>?,
        var isFavorite: Boolean = false,
        var isWatchingLater: Boolean = false
)
{
        constructor(film: FavoriteFilm) : this(film.id, film.title, film.originalTitle, film.year, film.posterLink, film.genres, true)
}