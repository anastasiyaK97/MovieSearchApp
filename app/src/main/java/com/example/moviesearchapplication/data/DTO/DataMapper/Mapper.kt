package com.example.moviesearchapplication.data.DTO.DataMapper

interface Mapper<I, O> {
    fun map(input: I): O
}