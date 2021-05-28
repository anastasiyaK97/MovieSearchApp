package com.example.moviesearchapplication.data.DTO.DataMapper

import com.example.moviesearchapplication.data.DTO.NetworkFilm
import com.example.moviesearchapplication.data.model.entities.Film
import com.example.moviesearchapplication.data.model.entities.Genre

class FilmMapper: Mapper<NetworkFilm, Film> {
    override fun map(input: NetworkFilm): Film {
        return Film(
            input.id,
            input.title,
            input.originalTitle?:"",
            input.year,
            input.posterLink,
            input.networkGenres?.map { networkGenre ->  Genre(name = networkGenre.name) },
            false)
    }
}