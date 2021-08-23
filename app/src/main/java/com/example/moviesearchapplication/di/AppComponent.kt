package com.example.moviesearchapplication.di

import com.example.moviesearchapplication.App
import com.example.moviesearchapplication.data.FilmRepository
import com.example.moviesearchapplication.presentation.view.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RoomModule::class,
    NetModule::class, UseCaseModule::class, InteractorModule::class, ViewModelModule::class], dependencies = [])
interface AppComponent {
    fun inject(app: App)
    fun inject(activity: MainActivity)
    fun inject(fragment: FavoriteFilmsFragment)
    fun inject(fragment: FilmDetailFragment)
    fun inject(fragment: SetUpWatchLaterFragment)
    fun inject(fragment: MainFilmsFragment)

    fun repo(): FilmRepository

}