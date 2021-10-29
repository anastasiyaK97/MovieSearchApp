package com.example.moviesearchapplication.data.db

import androidx.room.Dao
import androidx.room.Query
import com.example.moviesearchapplication.data.model.entities.Genre
import io.reactivex.Flowable

@Dao
interface GenreDao {

    @Query("SELECT * FROM genre")
    fun getAll(): Flowable<List<Genre>>
}