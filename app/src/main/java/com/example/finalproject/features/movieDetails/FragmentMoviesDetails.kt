package com.example.finalproject.features.movieDetails

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.di.MovieRepositoryProvider
import com.example.finalproject.model.Movie
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FragmentMoviesDetails : Fragment() {

    private val viewModel: MoviesDetailsViewModel by viewModels {
        MoviesDetailsViewModelFactory((requireActivity() as MovieRepositoryProvider).provideMovieRepository())
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_movies_details, container, false)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = arguments?.getSerializable(PARAM_MOVIE_ID) as? Int ?: return
        view.findViewById<RecyclerView>(R.id.actors_list).apply {
            this.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            this.adapter = ActorsAdapter()
        }

        viewModel.loadDetails(movieId)

        lifecycleScope.launch {
            viewModel.movie.collect { movie ->
                movie?.let { bindUI(view, it) } ?: showMovieNotFoundError()
            }
        }
    }

    private fun showMovieNotFoundError() {
        Toast.makeText(
            requireContext(),
            R.string.error_movie_not_found,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun updateMovieDetailsInfo(movie: Movie) {
        view?.findViewById<ImageView>(R.id.mainImage)?.load(movie.detailImageUrl)

        view?.findViewById<TextView>(R.id.age_restr_text)?.text =
            context?.getString(R.string.movies_list_allowed_age_template, movie.pgAge)

        view?.findViewById<TextView>(R.id.avengersName)?.text = movie.title
        view?.findViewById<TextView>(R.id.avengersGenre)?.text = movie.genres.joinToString { it.name }
        view?.findViewById<TextView>(R.id.movie_reviews_count_text)?.text =
            context?.getString(R.string.movies_list_reviews_template, movie.reviewCount)
        view?.findViewById<TextView>(R.id.storylineContentText)?.text = movie.storyLine
        val starsImages = listOf<ImageView?>(
            view?.findViewById(R.id.star1_image),
            view?.findViewById(R.id.star2_image),
            view?.findViewById(R.id.star3_image),
            view?.findViewById(R.id.star4_image),
            view?.findViewById(R.id.star5_image)
        )
        starsImages.forEachIndexed { index, imageView ->
            imageView?.let {
                val colorId = if (movie.rating > index) R.color.pink_text else R.color.grey_text
                ImageViewCompat.setImageTintList(
                    imageView, ColorStateList.valueOf(
                        ContextCompat.getColor(imageView.context, colorId)
                    )
                )
            }
        }
    }
    private fun bindUI(view: View, movie: Movie) {
        updateMovieDetailsInfo(movie)
        val adapter = view.findViewById<RecyclerView>(R.id.actors_list).adapter as ActorsAdapter
        adapter.submitList(movie.actors)
    }

    companion object {
        private const val PARAM_MOVIE_ID = "movie_id"

        fun create(movieId: Int) = FragmentMoviesDetails().also {
            val args = bundleOf(
                PARAM_MOVIE_ID to movieId
            )
            it.arguments = args
        }
    }

}