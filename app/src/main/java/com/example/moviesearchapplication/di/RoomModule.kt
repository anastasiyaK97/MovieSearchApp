package com.example.moviesearchapplication.di

import androidx.room.Room
import com.example.moviesearchapplication.App
import com.example.moviesearchapplication.data.FilmRepository
import com.example.moviesearchapplication.frameworks.database.FavoriteFilmDAO
import com.example.moviesearchapplication.frameworks.database.FilmDao
import com.example.moviesearchapplication.frameworks.database.RoomDB
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule(mApplication: App) {

    private val databaseInstance = Room
        .databaseBuilder(mApplication, RoomDB::class.java, "film_database.db")
        // .addCallback()
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun providesRoomDatabase(): RoomDB {
        return databaseInstance
    }

    @Singleton
    @Provides
    fun providesFilmDao(db: RoomDB): FilmDao {
        return db.getFilmDao()
    }

    @Singleton
    @Provides
    fun providesFavoriteFilmDao(db: RoomDB): FavoriteFilmDAO {
        return db.getFavoriteFilmDao()
    }
    @Singleton
    @Provides
    fun providesRepository(filmDao: FilmDao, favoriteFilmDAO: FavoriteFilmDAO): FilmRepository {
        return FilmRepository(filmDao, favoriteFilmDAO)
    }
}