package com.example.moviesearchapplication.data

import com.example.moviesearchapplication.R

class FilmRepository {

    companion object {
        val filmCollectoin : ArrayList<Film> = arrayListOf(
            Film("Интерстеллар", "", R.drawable.interstellar),
            Film("Начало", "", R.drawable.inception),
            Film("Прибытие", "", R.drawable.pribyitie2)
        )
    }
}