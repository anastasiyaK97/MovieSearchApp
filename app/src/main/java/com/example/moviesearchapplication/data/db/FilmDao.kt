package com.example.moviesearchapplication.data.db

import androidx.room.*
import com.example.moviesearchapplication.data.model.entities.Film
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface FilmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(film: Film): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(film: List<Film>): Completable

    @Update
    fun update(film: Film): Completable

    @Query("SELECT * FROM film")
    fun getAll(): Flowable<List<Film>>

    @Query("SELECT * FROM film WHERE isFavorite = 1")
    fun getAllFavorite(): Flowable<List<Film>>

    @Query("SELECT * FROM film where id = :id")
    fun getFilmById(id: Int): Single<Film>
}