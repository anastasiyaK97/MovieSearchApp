package com.example.moviesearchapplication.presentation.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesearchapplication.BuildConfig
import com.example.moviesearchapplication.R
import com.example.moviesearchapplication.data.model.entities.Film
import com.example.moviesearchapplication.databinding.FilmItemBinding

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
        val inflater = LayoutInflater.from(parent.context)
        val filmItemBinding = FilmItemBinding.inflate(inflater, parent, false)
        if (!BuildConfig.PAID) {
            filmItemBinding.like.visibility = View.INVISIBLE
        }
        return FilmViewHolder(filmItemBinding)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film = filmList[position]
        holder.bind(film)
        holder.itemView.setOnClickListener{
            clickListener.onItemClick(film, position)
        }
        holder.setLikeClickListener(film, position)
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

    inner class FilmViewHolder(private val binding: FilmItemBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(item : Film) {
            binding.name.text = item.title
            binding.genre.text = item.originalTitle
            Glide.with(binding.poster.context)
                .load(item.posterLink)
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_broken_image)
                .override(binding.poster.resources.getDimensionPixelSize(R.dimen.poster_width), binding.poster.resources.getDimensionPixelSize(R.dimen.poster_height))
                .into(binding.poster)

            if (item.isFavorite) {
                binding.like.setImageResource(R.drawable.ic_favorite_light)
            }
            else {
                binding.like.setImageResource(R.drawable.ic_favorite_border_light)
            }

            if (item.isWatchingLater) {
                binding.watchLater.setImageResource(R.drawable.ic_watch_later)
                binding.watchLater.setOnClickListener{}
            }
            else {
                binding.watchLater.setImageResource(R.drawable.ic_add_to_watch_later)
                binding.watchLater.setOnClickListener{watchLaterClickListener.onIconClick(item)}
            }
        }

        fun setLikeClickListener(item: Film, position: Int){
            binding.like.setOnClickListener{likeClickListener.onLikeClick(item, position)}
        }
    }

}