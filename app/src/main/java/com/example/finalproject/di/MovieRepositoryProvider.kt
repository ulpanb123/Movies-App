package com.example.finalproject.di

import com.example.finalproject.data.MovieRepository

internal interface MovieRepositoryProvider {
    fun provideMovieRepository(): MovieRepository
}