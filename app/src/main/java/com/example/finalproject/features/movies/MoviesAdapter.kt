package com.example.finalproject.features.movies

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.model.Movie


class MoviesAdapter(
    private val onClickCard: (item: Movie) -> Unit
) : ListAdapter<Movie, MoviesAdapter.MoviesViewHolder>(DiffCallback())
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        return MoviesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_movie, parent, false))
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClickCard)
    }

    class MoviesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //Do once part
        private val movieImage = view.findViewById<ImageView>(R.id.movie_image)
        private val ageRestrictionTextView = view.findViewById<TextView>(R.id.age_restr)
        private val isLikedButton = view.findViewById<ImageView>(R.id.like_button)
        private val genreTextView = view.findViewById<TextView>(R.id.genreTextView)
        private val starsImages: List<ImageView> = listOf(
            itemView.findViewById(R.id.star1_image),
            itemView.findViewById(R.id.star2_image),
            itemView.findViewById(R.id.star3_image),
            itemView.findViewById(R.id.star4_image),
            itemView.findViewById(R.id.star5_image)
        )
        private val reviewsTextView = view.findViewById<TextView>(R.id.movie_reviews_count_text)
        private val nameTextView = view.findViewById<TextView>(R.id.movie_name)
        private val durationTextView = view.findViewById<TextView>(R.id.movie_duration)


        //Do always
        fun bind(movie: Movie, onClickCard: (item: Movie) -> Unit) {
            Glide.with(itemView).load(movie.imageUrl).centerCrop().into(movieImage)

            ageRestrictionTextView.text = movie.pgAge.toString() + "+"
            val likeColor = if (movie.isLiked) {
                R.color.pink_text
            } else {
                R.color.white
            }

            ImageViewCompat.setImageTintList(
                isLikedButton, ColorStateList.valueOf(
                    ContextCompat.getColor(isLikedButton.context, likeColor)
                )
            )

            //set stars tint
            starsImages.forEachIndexed { index, imageView ->
                val colorId = if (movie.rating > index) R.color.pink_text else R.color.grey_text
                ImageViewCompat.setImageTintList(
                    imageView, ColorStateList.valueOf(
                        ContextCompat.getColor(imageView.context, colorId)
                    )
                )
            }

            genreTextView.text = movie.genres.joinToString { it.name }

            val stringReviews: String = movie.reviewCount.toString() + " REVIEWS"
            reviewsTextView.text = stringReviews

            nameTextView.text = movie.title

            val stringDuration: String = movie.runningTime.toString() + " MIN"
            durationTextView.text = stringDuration

            itemView.setOnClickListener {
                onClickCard(movie)
            }
        }


    }

    class DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
}