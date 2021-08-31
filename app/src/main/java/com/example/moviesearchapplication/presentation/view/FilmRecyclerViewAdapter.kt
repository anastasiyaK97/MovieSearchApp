package com.example.moviesearchapplication.presentation.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesearchapplication.R
import com.example.moviesearchapplication.data.model.entities.Film

class FilmRecyclerViewAdapter(private var filmList: ArrayList<Film>,
                              private val clickListener: OnItemClickListener,
                              private val likeClickListener: OnLikeClickListener,
                              private val watchLaterClickListener: OnWatchlaterClickListener
)
    : RecyclerView.Adapter<FilmRecyclerViewAdapter.FilmViewHolder>() {

    // region Interfaces
    interface OnItemClickListener {
        fun onItemClick(item: Film, position: Int)
    }

    interface OnLikeClickListener {
        fun onLikeClick(item: Film, position: Int)
    }
    interface OnWatchlaterClickListener {
        fun onIconClick(item: Film)
    }
    // endregion

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.film_item, parent, false)
        return FilmViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film = filmList[position]
        holder.bind(film)
        holder.itemView.setOnClickListener{
            clickListener.onItemClick(film, position)
        }
        holder.setLikeClickListener(film, position)
        //holder.setWhatchLaterClickListener(film, position)
    }

    override fun getItemCount(): Int = filmList.size

    override fun onViewDetachedFromWindow(holder: FilmViewHolder){
        holder.itemView.clearAnimation()
        super.onViewDetachedFromWindow(holder)
    }

    fun setData(newList: ArrayList<Film>){
        this.filmList = newList
        this.notifyDataSetChanged()
    }

    inner class FilmViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameTV: TextView = view.findViewById(R.id.name)
        private val genreTV: TextView = view.findViewById(R.id.genre)
        private val posterIV: ImageView = view.findViewById(R.id.poster)
        private val likeIV: ImageView = view.findViewById(R.id.like)
        private val watchLaterIV: ImageView = view.findViewById(R.id.watch_later)

        fun bind(item : Film) {
            nameTV.text = item.title
            genreTV.text = item.originalTitle
            Glide.with(posterIV.context)
                .load(item.posterLink)
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_broken_image)
                .override(posterIV.resources.getDimensionPixelSize(R.dimen.poster_width), posterIV.resources.getDimensionPixelSize(R.dimen.poster_height))
                .into(posterIV)

            if (item.isFavorite) {
                likeIV.setImageResource(R.drawable.ic_favorite_light)
            }
            else {
                likeIV.setImageResource(R.drawable.ic_favorite_border_light)
            }

            if (item.isWatchingLater) {
                watchLaterIV.setImageResource(R.drawable.ic_watch_later)
                watchLaterIV.setOnClickListener{}
            }
            else {
                watchLaterIV.setImageResource(R.drawable.ic_add_to_watch_later)
                watchLaterIV.setOnClickListener{watchLaterClickListener.onIconClick(item)}
            }
        }

        fun setLikeClickListener(item: Film, position: Int){
            likeIV.setOnClickListener{likeClickListener.onLikeClick(item, position)}
        }
        /*fun setWhatchLaterClickListener(item: Film, position: Int){
            watchLaterIV.setOnClickListener{watchLaterClickListener.onIconClick(item, position)}
        }*/
    }

}