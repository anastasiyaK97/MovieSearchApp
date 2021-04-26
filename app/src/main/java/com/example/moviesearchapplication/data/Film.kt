package com.example.moviesearchapplication.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Film(val name: String, val descr: String, val imageID: Int) : Parcelable