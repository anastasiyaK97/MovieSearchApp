package com.example.moviesearchapplication.data.database

import androidx.room.*
import com.example.moviesearchapplication.data.model.Film

@Dao
interface FilmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(film: Film)

    @Query("SELECT * from film")
    fun getAll(): List<Film>

    @Update
    fun update(film: Film)
}