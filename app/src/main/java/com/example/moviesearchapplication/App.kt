package com.example.moviesearchapplication

import android.app.Application
import com.example.moviesearchapplication.data.database.Database
import com.example.moviesearchapplication.domain.FilmApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

class App: Application() {
    lateinit var filmApiService: FilmApiService

    companion object {
        lateinit var instance: App
            private set
        const val BASE_URL = "https://my-json-server.typicode.com/anastasiyaK97/testapi/"
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        initRetrofit()
        initDatabase()
    }

    private fun initDatabase() {
        Executors.newSingleThreadScheduledExecutor().execute{
            Database.getInstance(this)
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
            .build()

        filmApiService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FilmApiService::class.java)
    }
}