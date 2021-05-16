package com.example.moviesearchapplication.data.database

import androidx.room.Dao
import androidx.room.Query
import com.example.moviesearchapplication.data.model.Genre

@Dao
interface GenreDao {

    @Query("SELECT * FROM genre")
    fun getAll():List<Genre>
}