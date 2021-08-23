package com.example.moviesearchapplication

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.moviesearchapplication.data.DTO.FilmsWithPageCount
import com.example.moviesearchapplication.data.DTO.NetworkFilm
import com.example.moviesearchapplication.data.FilmRepository
import com.example.moviesearchapplication.data.MoviesLocalDataSource
import com.example.moviesearchapplication.data.MoviesRemoteDataSource
import com.example.moviesearchapplication.data.model.entities.Film
import com.example.moviesearchapplication.domain.usecase.GetFilmListUseCase
import com.example.moviesearchapplication.frameworks.apiServices.FilmApiService
import com.example.moviesearchapplication.frameworks.database.FavoriteFilmDAO
import com.example.moviesearchapplication.frameworks.database.FilmDao
import com.example.moviesearchapplication.frameworks.database.RoomDB
import com.example.moviesearchapplication.rules.RxSchedulerRule
import org.junit.*
import org.junit.runner.RunWith
//import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
@MediumTest
class FilmListUseCaseInstrumentedTest {
    @Rule
    @JvmField
    var testSchedulerRule = RxSchedulerRule()

    lateinit var database: RoomDB
    lateinit var filmDao: FilmDao
    lateinit var favoriteFilmDao: FavoriteFilmDAO
    lateinit var repository: FilmRepository
    lateinit var filmListUseCase: GetFilmListUseCase

    lateinit var localSource: MoviesLocalDataSource
    lateinit var remoteSource: MoviesRemoteDataSource
    lateinit var apiService: FilmApiService

    @Before
    fun setUp() {
        //apiService = Mockito.mock(FilmApiService::class.java)
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RoomDB::class.java
        )
            .allowMainThreadQueries()
            .build()
        filmDao = database.getFilmDao()
        favoriteFilmDao = database.getFavoriteFilmDao()
        localSource = MoviesLocalDataSource(filmDao, favoriteFilmDao)
        remoteSource = MoviesRemoteDataSource(apiService)
        repository = FilmRepository(localSource, remoteSource)
        filmListUseCase = GetFilmListUseCase(repository)
    }

    @After
    fun tearDown() {
//        database.close()
    }

    @Test
    fun testGetAllFilm_returnFilmList() {
        val moviesResponse = FilmsWithPageCount(1, listOf(
            NetworkFilm(1, "Title1", "Original Title1", "2021", "https://kinopoiskapiunofficial.tech/images/posters/kp/1101569.jpg", null)
        ))
        //Mockito.`when`(interactor.getFilms()).thenReturn(Flowable.just(moviesResponse))
        val requiredFilm = Film(
            1, "Title1", "Original Title",
            "2021",
            "https://kinopoiskapiunofficial.tech/images/posters/kp/1101569.jpg",
            null
        )

        val testSubscriber = filmListUseCase.getAllFilms().test()
        val actualFilmList = testSubscriber.values()[0]
        Assert.assertEquals(requiredFilm.id, actualFilmList[0].id)
        Assert.assertEquals(requiredFilm.title, actualFilmList[0].title)
        Assert.assertEquals(requiredFilm.originalTitle, actualFilmList[0].originalTitle)
        Assert.assertEquals(requiredFilm.year, actualFilmList[0].year)
        Assert.assertEquals(requiredFilm.posterLink, actualFilmList[0].posterLink)
    }

}