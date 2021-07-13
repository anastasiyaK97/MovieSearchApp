package com.example.moviesearchapplication

import android.app.Application
import com.example.moviesearchapplication.domain.FilmInteractor
import com.example.moviesearchapplication.frameworks.apiServices.FilmApiService
import com.example.moviesearchapplication.frameworks.database.Database
import com.example.moviesearchapplication.frameworks.database.RoomDB
import com.example.moviesearchapplication.presentation.utilities.MyNotifications
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

class App: Application() {
    lateinit var filmApiService: FilmApiService
    lateinit var filmInteractor: FilmInteractor
    lateinit var db : RoomDB

    companion object {
        lateinit var instance: App
            private set
        const val BASE_URL = "https://kinopoiskapiunofficial.tech/"
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        initDatabase()
        initRetrofit()
        configureFirebaseSettings()
        initInteractor()
        initNotificationChannels()

    }

    private fun initDatabase() {
        Executors.newSingleThreadScheduledExecutor().execute{
            db = Database.getInstance(this)
        }
    }

    private fun initRetrofit() {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.BASIC
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor{chain ->
                return@addInterceptor chain.proceed(
                    chain
                        .request()
                        .newBuilder()
                        .addHeader("X-API-KEY", "d4c64b7d-d347-4dff-9b1a-109d52f5c27a")
                        .addHeader("accept", "application/json")
                        .build()
                )
            }
            .build()

        filmApiService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FilmApiService::class.java)
    }

    private fun initInteractor() {
        filmInteractor = FilmInteractor(filmApiService)
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