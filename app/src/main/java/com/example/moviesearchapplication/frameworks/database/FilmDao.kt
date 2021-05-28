package com.example.moviesearchapplication.frameworks.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moviesearchapplication.data.model.entities.Film

@Dao
interface FilmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(film: Film)

    @Update
    fun update(film: Film)

    @Query("SELECT * FROM film")
    fun getAll(): LiveData<List<Film>>

    @Query("SELECT * FROM film WHERE isFavorite = 1")
    fun getAllFavorite(): LiveData<List<Film>>

    @Query("SELECT * FROM film where id = :id")
    fun getFilmById(id: Int): Film
}