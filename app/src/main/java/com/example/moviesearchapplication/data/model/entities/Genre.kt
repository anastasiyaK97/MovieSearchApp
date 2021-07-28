package com.example.moviesearchapplication.data.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genre")
data class Genre(
    @PrimaryKey (autoGenerate = true)
    val id: Int? = null,
    val name: String
)