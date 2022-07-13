package com.example.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.data.JsonMovieRepository
import com.example.finalproject.data.MovieRepository
import com.example.finalproject.di.MovieRepositoryProvider
import com.example.finalproject.features.movieDetails.FragmentMoviesDetails
import com.example.finalproject.model.Movie
import java.net.URI.create

class MainActivity : AppCompatActivity(),
    MovieRepositoryProvider,
    FragmentMoviesList.MoviesListItemClickListener{

    private val jsonMovieRepository = JsonMovieRepository(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.main_container, FragmentMoviesList())
            .commit()
    }

    private fun navigateToDetailsClicked(movieId : Int) {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.main_container,
                FragmentMoviesDetails.create(movieId),
                FragmentMoviesDetails::class.java.simpleName
            )
            .addToBackStack("trans:${FragmentMoviesDetails::class.java.simpleName}")
            .commit()

    }

    override fun provideMovieRepository(): MovieRepository = jsonMovieRepository
    override fun onMovieSelected(movie: Movie) {
        navigateToDetailsClicked(movie.id)
    }
}