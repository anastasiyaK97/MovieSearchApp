package com.example.moviesearchapplication.data.DTO.DataMapper

import com.example.moviesearchapplication.data.DTO.NetworkFilm
import com.example.moviesearchapplication.data.model.entities.Film

class FilmListMapper {
    val mapper = FilmMapper()
    fun map(input: List<NetworkFilm>): List<Film> {
            return input.map { mapper.map(it) }
    }
}