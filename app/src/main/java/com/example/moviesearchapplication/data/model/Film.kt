package com.example.moviesearchapplication.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "film")
data class Film(
    @SerializedName("id")
    @PrimaryKey
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("originalTitle")
    val originalTitle: String,
    @SerializedName("serialYear")
    val year: String,
    @SerializedName("posterLink")
    val posterLink: String,
    var isFavorite: Boolean = false,
    @ColumnInfo(name = "genresArray")
    @SerializedName("genres")
    var genres : List<Genre>?
)