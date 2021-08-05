package com.example.moviesearchapplication

import android.app.Application
import com.example.moviesearchapplication.di.AppComponent
import com.example.moviesearchapplication.di.AppModule
import com.example.moviesearchapplication.di.DaggerAppComponent
import com.example.moviesearchapplication.di.RoomModule
import com.example.moviesearchapplication.domain.FilmInteractor
import com.example.moviesearchapplication.frameworks.apiServices.FilmApiService
import com.example.moviesearchapplication.frameworks.database.RoomDB
import com.example.moviesearchapplication.presentation.utilities.MyNotifications
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import javax.inject.Inject

class App: Application() {
    lateinit var applicationComponent: AppComponent
    @Inject
    lateinit var db : RoomDB
    @Inject
    lateinit var filmApiService: FilmApiService
    @Inject
    lateinit var filmInteractor: FilmInteractor

    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        applicationComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .roomModule(RoomModule(this))
            .build()
        applicationComponent.inject(this)

        configureFirebaseSettings()
        initNotificationChannels()

    }

    private fun initNotificationChannels() {
        MyNotifications.createWatchLaterChannel(instance)
    }

    private fun configureFirebaseSettings() {
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(FeatureToggles.defaults)
        remoteConfig.fetchAndActivate()
    }

}