package com.example.finalproject.features.movieDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.data.MovieRepository
import com.example.finalproject.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MoviesDetailsViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _movie = MutableStateFlow<Movie?>(null)
    val movie : StateFlow<Movie?> get() = _movie

    fun loadDetails(movieId : Int) {
        viewModelScope.launch {
            _movie.value = repository.loadMovie(movieId)
        }
    }
}