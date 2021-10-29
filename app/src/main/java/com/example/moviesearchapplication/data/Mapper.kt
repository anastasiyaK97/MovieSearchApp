package com.example.moviesearchapplication.data

interface Mapper<I, O> {
    fun map(input: I): O
}