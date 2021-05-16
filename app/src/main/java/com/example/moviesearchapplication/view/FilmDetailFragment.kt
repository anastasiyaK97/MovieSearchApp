package com.example.moviesearchapplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.moviesearchapplication.R
import com.example.moviesearchapplication.data.FilmRepository
import com.google.android.material.appbar.CollapsingToolbarLayout

class FilmDetailFragment : Fragment() {

    companion object {
        private const val FILM_ID_EXTRA = "FILM_ID_EXTRA"
        fun newInstance(filmId: Int): FilmDetailFragment {
            val args = Bundle()
            args.putInt(FILM_ID_EXTRA, filmId)

            val newFragment =
                FilmDetailFragment()
            newFragment.arguments = args
            return newFragment
        }
    }

    private var filmId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.film_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        filmId = arguments?.getInt(FILM_ID_EXTRA) ?: 0
        with(FilmRepository.getFilmById(filmId)) {
            view.findViewById<TextView>(R.id.film_name).text = this.title
            view.findViewById<TextView>(R.id.descr).text = this.originalTitle

            initToolbar(this.title, this.posterLink)
        }
    }

    private fun initToolbar(title: String, posterLink: String) {
        (activity as AppCompatActivity).supportActionBar
            ?.setDisplayHomeAsUpEnabled(true)

        (requireActivity().findViewById<ImageView>(R.id.expandedImage))?.let { imageView ->
            Glide.with(imageView.context)
                .load(posterLink)
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_broken_image)
                .override(imageView.resources.getDimensionPixelSize(R.dimen.poster_width), imageView.resources.getDimensionPixelSize(R.dimen.poster_height))
                .into(imageView)
            imageView.visibility = View.VISIBLE
            imageView.adjustViewBounds = true
            imageView.maxHeight = resources.getDimension(R.dimen.expanded_image_height).toInt()
        }
        requireActivity().findViewById<CollapsingToolbarLayout>(R.id.main_collapsing).title = title

    }

}