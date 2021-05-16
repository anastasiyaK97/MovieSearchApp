package com.example.moviesearchapplication.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moviesearchapplication.data.model.Film
import com.example.moviesearchapplication.data.model.Genre

@Database(
    entities = arrayOf(
        Film::class,
        Genre::class
    ), version = 1)
@TypeConverters(GenreConverter::class)
abstract class RoomDB : RoomDatabase() {
    abstract fun getFilmDao(): FilmDao
    //abstract fun getGenreDao(): GenreDao
}