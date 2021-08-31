package com.example.moviesearchapplication.unit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviesearchapplication.data.DTO.FilmsWithPageCount
import com.example.moviesearchapplication.data.DTO.NetworkFilm
import com.example.moviesearchapplication.data.FilmRepository
import com.example.moviesearchapplication.data.MoviesLocalDataSource
import com.example.moviesearchapplication.data.MoviesRemoteDataSource
import com.example.moviesearchapplication.data.model.entities.FavoriteFilm
import com.example.moviesearchapplication.data.model.entities.Film
import com.example.moviesearchapplication.frameworks.apiServices.FilmApiService
import com.example.moviesearchapplication.frameworks.database.FavoriteFilmDAO
import com.example.moviesearchapplication.frameworks.database.FilmDao
import com.example.moviesearchapplication.rules.RxImmediateSchedulerRule
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Matchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit


class RepositoryMethodsUnitTests {

    @Rule
    @JvmField
    val rule = MockitoJUnit.rule()!!
    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()
    @Rule
    @JvmField
    var rule2: TestRule = InstantTaskExecutorRule()

    @Mock lateinit var mockFilmDao: FilmDao
    @Mock lateinit var mockFavoriteFilmDao: FavoriteFilmDAO
    @Mock lateinit var mockedApiService: FilmApiService

    lateinit var remoteDataSource: MoviesRemoteDataSource
    lateinit var localDataSource: MoviesLocalDataSource
    lateinit var repository: FilmRepository

    @Before
    fun setUp() {
        remoteDataSource = MoviesRemoteDataSource(mockedApiService)
        localDataSource = MoviesLocalDataSource(mockFilmDao, mockFavoriteFilmDao)
        repository = FilmRepository(localDataSource, remoteDataSource)
        repository.pageCount.value = 1
    }

    @Test
    fun `getFilmList requests films from network and return films with favorite flags`() {
        val moviesResponse = FilmsWithPageCount(1, listOf(
            NetworkFilm(1, "Title1", "Original Title1", "2021", "posterUrl1", null),
            NetworkFilm(2, "Title2", "Original Title2", "2021", "posterUrl2", null),
        ))
        val moviesFavorite = listOf(
            FavoriteFilm(1, "Title1", "Original Title1", "2021", "posterUrl1", null),
        )

        val requiredMovies = listOf(
            Film(1, "Title1", "Original Title1", "2021", "posterUrl1", null, true),
            Film(2, "Title2", "Original Title2", "2021", "posterUrl2", null, false),
        )

        Mockito.`when`(mockedApiService.getTopFilms(anyString(), anyInt()))
            .thenReturn(Flowable.just(moviesResponse))
        Mockito.`when`(mockFavoriteFilmDao.getAll())
            .thenReturn(Flowable.just(moviesFavorite))
        Mockito.`when`(mockFilmDao.insertList(Matchers.anyList()))
            .thenReturn(Completable.complete())
        Mockito.`when`(mockFilmDao.getAll())
            .thenReturn(Flowable.just(requiredMovies))

        val testSubscriber = repository.getFilmList(1).test()
        val actualResult = testSubscriber.values()[0]

        Assert.assertEquals(requiredMovies.size, actualResult.size)
        Assert.assertEquals(requiredMovies[0].id, actualResult[0].id)
        Assert.assertEquals(requiredMovies[1].id, actualResult[1].id)
        Assert.assertEquals(requiredMovies[0].isFavorite, actualResult[0].isFavorite)
        Assert.assertEquals(requiredMovies[1].isFavorite, actualResult[1].isFavorite)

        testSubscriber.dispose()

    }

    @Test
    fun `when requestNetworkFilms, should call api service and return NetworkFilms without page count`() {
        val moviesResponse = FilmsWithPageCount(1, listOf(
            NetworkFilm(1, "Title1", "Original Title1", "2021", "posterUrl1", null),
            NetworkFilm(2, "Title2", "Original Title2", "2021", "posterUrl2", null),
        ))

        val requiredMovies = listOf(
            NetworkFilm(1, "Title1", "Original Title1", "2021", "posterUrl1", null),
            NetworkFilm(2, "Title2", "Original Title2", "2021", "posterUrl2", null),
        )

        Mockito.`when`(mockedApiService.getTopFilms(anyString(), anyInt()))
            .thenReturn(Flowable.just(moviesResponse))

        val testSubscriber = repository.requestNetworkFilms(1).test()
        val actualResult = testSubscriber.values()[0]

        Assert.assertEquals(requiredMovies.size, actualResult.size)
        Assert.assertEquals(requiredMovies[0].id, actualResult[0].id)
        Assert.assertEquals(requiredMovies[1].id, actualResult[1].id)
    }

}