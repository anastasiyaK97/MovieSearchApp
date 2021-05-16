package com.example.moviesearchapplication.data.database

import androidx.room.TypeConverter
import com.example.moviesearchapplication.data.model.Genre
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class GenreConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToGenreList(data: String?): List<Genre?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type =
            object : TypeToken<List<Genre?>?>() {}.type
        return gson.fromJson<List<Genre?>>(data, listType)
    }

    @TypeConverter
    fun genreListToString(list: List<Genre?>?): String? {
        return gson.toJson(list)
    }
}