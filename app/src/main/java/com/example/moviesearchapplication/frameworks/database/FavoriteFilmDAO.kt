package com.example.moviesearchapplication.frameworks.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moviesearchapplication.data.model.entities.FavoriteFilm

@Dao
interface FavoriteFilmDAO {

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    fun insert(film: FavoriteFilm)

    @Update
    fun update(film: FavoriteFilm)

    @Query("SELECT * FROM favorite_films")
    fun getAll(): LiveData<List<FavoriteFilm>>

    @Query("SELECT * FROM favorite_films WHERE id IN (:idList)")
    fun checkIfFilmsAreFavorites(idList: List<Int>): List<FavoriteFilm>

    @Delete
    fun delete(film: FavoriteFilm)
}