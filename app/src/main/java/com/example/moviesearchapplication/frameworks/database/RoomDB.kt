package com.example.moviesearchapplication.frameworks.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moviesearchapplication.data.model.entities.FavoriteFilm
import com.example.moviesearchapplication.data.model.entities.Film
import com.example.moviesearchapplication.data.model.entities.Genre

@Database(
    entities = arrayOf(
        Film::class,
        Genre::class,
        FavoriteFilm::class
    ), version = 4)
@TypeConverters(GenreConverter::class)
abstract class RoomDB : RoomDatabase() {
    abstract fun getFilmDao(): FilmDao
    abstract fun getFavoriteFilmDao(): FavoriteFilmDAO
}