package com.example.moviesearchapplication.data

import com.example.moviesearchapplication.data.model.Film

class FilmRepository {

    companion object {
        val filmCollection: MutableList<Film> = mutableListOf()

        /*  arrayListOf(
          Film(0, "Интерстеллар", "2014, фантастика, драма, приключения", R.drawable.interstellar, true),
          Film(1, "Начало", "2010, фантастика, боевик, триллер", R.drawable.inception, true),
          Film(2, "Прибытие", "2016, фантастика, триллер, драма", R.drawable.pribyitie2, true),
          Film(3, "Интерстеллар2", "2014, фантастика, драма, приключения", R.drawable.interstellar, true),
          Film(4, "Начало2", "2010, фантастика, боевик, триллер", R.drawable.inception),
          Film(5, "Прибытие2", "2016, фантастика, триллер, драма", R.drawable.pribyitie2)
        )*/

        fun getFilmById(id: Int): Film {
            return filmCollection.find { film -> film.id == id } ?: filmCollection[0]
        }

        private val favoriteFilmCollection = ArrayList<Film>()

        fun getFavoriteFilms() : ArrayList<Film> {
            favoriteFilmCollection.clear()
            for (item in filmCollection) {
                if (item.isFavorite)
                    favoriteFilmCollection.add(item)
                else
                    favoriteFilmCollection.remove(item)
            }
            return favoriteFilmCollection
        }
    }


}