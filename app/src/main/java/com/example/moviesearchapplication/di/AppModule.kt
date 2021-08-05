package com.example.moviesearchapplication.di

import com.example.moviesearchapplication.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(var mApplication: App) {
    @Provides
    @Singleton
    fun providesApplication(): App {
        return mApplication
    }
}