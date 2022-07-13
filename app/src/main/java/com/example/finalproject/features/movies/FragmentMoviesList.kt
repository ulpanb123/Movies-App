package com.example.finalproject

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.di.MovieRepositoryProvider
import com.example.finalproject.features.movies.MoviesAdapter
import com.example.finalproject.features.movies.MoviesListViewModel
import com.example.finalproject.features.movies.MoviesListViewModelFactory
import com.example.finalproject.model.Movie
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class FragmentMoviesList : Fragment() {


    private var movies: List<Movie> = emptyList()

    private lateinit var adapter : MoviesAdapter

    private var listener: MoviesListItemClickListener? = null

    private val viewmodel: MoviesListViewModel by viewModels {
        MoviesListViewModelFactory((requireActivity() as MovieRepositoryProvider).provideMovieRepository())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MoviesListItemClickListener) {
            listener = context
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movies_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<RecyclerView>(R.id.movies_list).apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)

            val adapter = MoviesAdapter { movieData ->
                listener?.onMovieSelected(movieData)
            }

            this.adapter = adapter

            loadDataToAdapter(adapter)
        }
    }


    private fun loadDataToAdapter(adapter: MoviesAdapter) {
        lifecycleScope.launch {
            viewmodel.movies.collect { movieList ->
                adapter.submitList(movieList)
            }
        }
    }

    override fun onDetach() {
        listener = null

        super.onDetach()
    }

    interface MoviesListItemClickListener {
        fun onMovieSelected(movie: Movie)
    }

    companion object {
        fun create() = FragmentMoviesList()
    }
}