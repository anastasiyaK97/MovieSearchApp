package com.example.moviesearchapplication.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.moviesearchapplication.App
import com.example.moviesearchapplication.R
import com.example.moviesearchapplication.presentation.viewmodel.FilmDetailViewModel
import com.example.moviesearchapplication.presentation.viewmodel.MainViewModelFactory
import com.google.android.material.appbar.CollapsingToolbarLayout
import javax.inject.Inject

class FilmDetailFragment : Fragment() {

    companion object {
        const val FILM_ID_EXTRA = "FILM_ID_EXTRA"
        @JvmStatic
        fun newInstance(filmId: Int): FilmDetailFragment {
            val args = Bundle()
            args.putInt(FILM_ID_EXTRA, filmId)

            val newFragment = FilmDetailFragment()
            newFragment.arguments = args
            return newFragment
        }
    }
    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    private val viewModel: FilmDetailViewModel by viewModels{viewModelFactory}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        App.instance.applicationComponent.inject(this)
        return inflater.inflate(R.layout.film_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val filmId: Int = arguments?.getInt(FILM_ID_EXTRA) ?: 0

        viewModel.setFilm(filmId)

        viewModel.film.observe(viewLifecycleOwner, { film ->
                    view.findViewById<TextView>(R.id.film_name).text = film?.title
                    view.findViewById<TextView>(R.id.descr).text = film?.originalTitle

                    initToolbar(film?.title?:"", film?.posterLink?:"")
            })

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