package com.example.moviesearchapplication.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearchapplication.R
import com.example.moviesearchapplication.data.Film

class FilmRecyclerViewAdapter(private val filmList: List<Film>,
                              private val clickListener: (item: Film, position: Int) -> Unit,
                              private val likeClickListener : (item: Film, position: Int) -> Unit)
    : RecyclerView.Adapter<FilmRecyclerViewAdapter.FilmViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.film_item, parent, false)
        return FilmViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film = filmList[position]
        holder.bind(film)
        holder.itemView.setOnClickListener{
            clickListener(film, position)
        }
        holder.setLikeClickListener(film, position)
    }

    override fun getItemCount(): Int = filmList.size

    override fun onViewDetachedFromWindow(holder: FilmViewHolder){
        holder.itemView.clearAnimation()
        super.onViewDetachedFromWindow(holder)
    }

    inner class FilmViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameTV: TextView = view.findViewById(R.id.name)
        private val genreTV: TextView = view.findViewById(R.id.genre)
        private val posterIV: ImageView = view.findViewById(R.id.poster)
        private val likeIV: ImageView = view.findViewById(R.id.like)

        fun bind(item : Film) {
            nameTV.text = item.name
            genreTV.text = item.descr
            posterIV.setImageResource(item.imageID)

            if (item.isFavorite) {
                likeIV.setImageResource(R.drawable.ic_favorite_light)
            }
            else {
                likeIV.setImageResource(R.drawable.ic_favorite_border_light)
            }
        }

        fun setLikeClickListener(item: Film, position: Int){
            likeIV.setOnClickListener{likeClickListener(item, position)}
        }
    }

}