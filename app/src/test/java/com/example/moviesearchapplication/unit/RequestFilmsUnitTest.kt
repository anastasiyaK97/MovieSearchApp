package com.example.moviesearchapplication.unit

import com.example.moviesearchapplication.data.DTO.FilmsWithPageCount
import com.example.moviesearchapplication.data.DTO.NetworkFilm
import com.example.moviesearchapplication.data.MoviesRemoteDataSource
import com.example.moviesearchapplication.frameworks.apiServices.FilmApiService
import com.example.moviesearchapplication.rules.RxImmediateSchedulerRule
import io.reactivex.Flowable
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit

class RequestFilmsUnitTest {
    @Rule @JvmField
    val rule = MockitoJUnit.rule()!!
    @Rule @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    lateinit var apiService: FilmApiService
    lateinit var remoteDataSource: MoviesRemoteDataSource

    @Before
    fun setUp() {
        remoteDataSource = MoviesRemoteDataSource(apiService)
    }

    @Test
    fun `when top films are requested, should call api service and return films with page count`() {
        val moviesResponse = FilmsWithPageCount(1, listOf(
            NetworkFilm(1, "Title1", "Original Title1", "2021", "posterUrl1", null),
            NetworkFilm(2, "Title2", "Original Title2", "2021", "posterUrl2", null),
        ))
        Mockito.`when`(apiService.getTopFilms()).thenReturn(Flowable.just(moviesResponse))

        val testSubscriber = remoteDataSource.getFilms().test()

        val actualResult = testSubscriber.values()[0]
        Assert.assertEquals(2, actualResult.films.count())
        Assert.assertEquals(1, actualResult.films[0].id)
        Assert.assertEquals("Title1", actualResult.films[0].title)
        Assert.assertEquals("Original Title1", actualResult.films[0].originalTitle)
        Assert.assertEquals("2021", actualResult.films[0].year)
        Assert.assertEquals("posterUrl1", actualResult.films[0].posterLink)

        testSubscriber.dispose()
    }

}