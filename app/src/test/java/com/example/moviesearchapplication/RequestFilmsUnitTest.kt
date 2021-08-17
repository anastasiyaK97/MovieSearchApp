package com.example.moviesearchapplication

import com.example.moviesearchapplication.data.DTO.FilmsWithPageCount
import com.example.moviesearchapplication.data.DTO.NetworkFilm
import com.example.moviesearchapplication.domain.FilmInteractor
import com.example.moviesearchapplication.frameworks.apiServices.FilmApiService
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
    lateinit var filmInteractor: FilmInteractor

    @Before
    fun setUp() {
        filmInteractor = FilmInteractor(apiService)
    }

    @Test
    fun `when top films are requested, should call api service and return films with page count`() {
        val moviesResponse = FilmsWithPageCount(1, listOf(
            NetworkFilm(1, "Title1", "Original Title1", "2021", "posterUrl1", null),
            NetworkFilm(2, "Title2", "Original Title2", "2021", "posterUrl2", null),
        ))
        Mockito.`when`(apiService.getTopFilms()).thenReturn(Flowable.just(moviesResponse))

        val testSubscriber = filmInteractor.getFilms().test()

        val testResult = testSubscriber.values()[0]
        Assert.assertEquals(2, testResult.films.count())
        Assert.assertEquals(1, testResult.films[0].id)
        Assert.assertEquals("Title1", testResult.films[0].title)
        Assert.assertEquals("Original Title1", testResult.films[0].originalTitle)
        Assert.assertEquals("2021", testResult.films[0].year)
        Assert.assertEquals("posterUrl1", testResult.films[0].posterLink)

        testSubscriber.dispose()
    }

}