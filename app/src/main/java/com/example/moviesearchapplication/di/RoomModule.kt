package com.example.moviesearchapplication.di

import androidx.room.Room
import com.example.moviesearchapplication.App
import com.example.moviesearchapplication.data.FilmRepository
import com.example.moviesearchapplication.data.MoviesLocalDataSource
import com.example.moviesearchapplication.data.MoviesRemoteDataSource
import com.example.moviesearchapplication.data.db.FavoriteFilmDAO
import com.example.moviesearchapplication.data.db.FilmDao
import com.example.moviesearchapplication.data.db.RoomDB
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule(mApplication: App) {

    private val databaseInstance = Room
        .databaseBuilder(mApplication, RoomDB::class.java, "film_database.db")
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
    fun providesRepository(
        localSource: MoviesLocalDataSource,
        remoteSource: MoviesRemoteDataSource
        ): FilmRepository {
        return FilmRepository(localSource, remoteSource)
    }
}