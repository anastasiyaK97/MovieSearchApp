package com.example.moviesearchapplication.frameworks.database

import androidx.room.*
import com.example.moviesearchapplication.data.model.entities.FavoriteFilm
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface FavoriteFilmDAO {

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    fun insert(film: FavoriteFilm): Completable

    @Update
    fun update(film: FavoriteFilm): Completable

    @Query("SELECT * FROM favorite_films")
    fun getAll(): Flowable<List<FavoriteFilm>>

    @Query("SELECT * FROM favorite_films WHERE id IN (:idList)")
    fun checkIfFilmsAreFavorites(idList: List<Int>): List<FavoriteFilm>

    @Delete
    fun delete(film: FavoriteFilm): Completable
}