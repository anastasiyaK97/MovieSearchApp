package com.example.moviesearchapplication.di

import com.example.moviesearchapplication.data.FilmRepository
import com.example.moviesearchapplication.presentation.viewmodel.MainViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelModule {

    @Provides
    @Singleton
    fun providesViewModelFactory(repository: FilmRepository): MainViewModelFactory {
        return MainViewModelFactory(repository)
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