package com.example.moviesearchapplication.di

import com.example.moviesearchapplication.domain.usecase.FilmUseCases
import com.example.moviesearchapplication.domain.usecase.GetFavoriteFilmListUseCase
import com.example.moviesearchapplication.domain.usecase.GetFilmListUseCase
import com.example.moviesearchapplication.presentation.viewmodel.MainViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelModule {

    @Provides
    @Singleton
    fun providesViewModelFactory(
        filmUseCases: FilmUseCases,
        filmListUseCase: GetFilmListUseCase,
        favoriteFilmListUseCase: GetFavoriteFilmListUseCase,
        ): MainViewModelFactory {
        return MainViewModelFactory(filmUseCases, filmListUseCase, favoriteFilmListUseCase)
    }

    /*@Provides
    @ActivityScope
    fun providesViewModel (fragment: Fragment, viewModelFactory: MainViewModelFactory): ViewModel {
        return ViewModelProviders.of(fragment, viewModelFactory)
    }
    @Provides
    @ActivityScope
    fun providesFragment(): Fragment {
        return
    }*/


}