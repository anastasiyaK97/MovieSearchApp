package com.example.moviesearchapplication.units

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.moviesearchapplication.data.model.entities.Film
import com.example.moviesearchapplication.frameworks.database.FavoriteFilmDAO
import com.example.moviesearchapplication.frameworks.database.FilmDao
import com.example.moviesearchapplication.frameworks.database.RoomDB
import com.example.moviesearchapplication.rules.RxSchedulerRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@MediumTest
class DatabaseUnitTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxSchedulerRule()

    lateinit var database: RoomDB
    lateinit var filmDao: FilmDao
    lateinit var favoriteFilmDao: FavoriteFilmDAO

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RoomDB::class.java
        )
            .allowMainThreadQueries()
            .build()
        filmDao = database.getFilmDao()
        favoriteFilmDao = database.getFavoriteFilmDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertFilmItem() {

        val requiredMovie = Film(
            1, "Title1", "Original Title",
            "2021",
            "https://kinopoiskapiunofficial.tech/images/posters/kp/1101569.jpg",
            null
        )
/*        val testSubscriber = filmDao.insert(requiredMovie)
            .andThen(
                filmDao.getAll()
            ).test()
        val allFilms = testSubscriber.valueCount()
        Assert.assertEquals(1, allFilms)*/
    }
}