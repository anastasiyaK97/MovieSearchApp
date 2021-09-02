package com.example.moviesearchapplication.unit

import com.example.moviesearchapplication.data.DTO.DataMapper.FilmMapper
import com.example.moviesearchapplication.data.DTO.NetworkFilm
import com.example.moviesearchapplication.data.model.entities.Film
import junit.framework.TestCase
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class FilmMapperUnitTest : TestCase() {

    lateinit var mapper: FilmMapper

    @Before
    override fun setUp() {
        mapper = FilmMapper()
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `test mapper from NetworkFilm to Film`() {
        val networkFilm = NetworkFilm(1, "Title1", "Original Title1", "2021", "posterUrl1", null)
        val actualFilm = mapper.map(networkFilm)
        val expectedFilm = Film(1,"Title1", "Original Title1",
            "2021", "posterUrl1", null, false, isWatchingLater = false)
        assertThat(actualFilm, `is`(expectedFilm))
    }
}