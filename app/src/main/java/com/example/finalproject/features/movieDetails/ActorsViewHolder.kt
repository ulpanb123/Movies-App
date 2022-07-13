package com.example.finalproject.features.movieDetails

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.model.Actor

class ActorsViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    private val actorImage = view.findViewById<ImageView>(R.id.img_actor)
    private val actorName = view.findViewById<TextView>(R.id.actor_name)



    fun bind(actor : Actor) {
        Glide.with(itemView).load(actor.imageUrl).into(actorImage)
        actorName.text = actor.name
    }
}