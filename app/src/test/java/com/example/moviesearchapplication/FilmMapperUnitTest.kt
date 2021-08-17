package com.example.moviesearchapplication

import com.example.moviesearchapplication.data.DTO.DataMapper.FilmMapper
import com.example.moviesearchapplication.data.DTO.NetworkFilm
import com.example.moviesearchapplication.data.model.entities.Film
import junit.framework.TestCase
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class FilmMapperUnitTest : TestCase() {

    @Mock
    lateinit var mapper: FilmMapper

    @Before
    override fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `test map from NetworkFilm to Film`() {
        val networkFilm = NetworkFilm(1, "Title1", "Original Title1", "2021", "posterUrl1", null)
        Mockito.`when`(mapper.map(networkFilm)).thenCallRealMethod()
        val actualFilm = mapper.map(networkFilm)
        val expectedFilm = Film(1,"Title1", "Original Title1",
            "2021", "posterUrl1", null, false, isWatchingLater = false)
        assertThat(actualFilm, `is`(expectedFilm))
    }
}