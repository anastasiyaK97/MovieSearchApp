package com.example.moviesearchapplication.view

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearchapplication.R
import com.example.moviesearchapplication.data.Film
import com.example.moviesearchapplication.data.FilmRepository
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar

class MainFilmsFragment : Fragment() {

    var clickListener: OnFilmClickListener? = null
    private lateinit var recycler: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_films_list, container, false)
        initRecyclerView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initToolbar()
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).supportActionBar
            ?.setDisplayHomeAsUpEnabled(false)

        requireActivity().findViewById<AppBarLayout>(R.id.appbar).setExpanded(false, false)
        requireActivity().findViewById<CollapsingToolbarLayout>(R.id.main_collapsing).title = resources.getString(R.string.film_nav_title)
        requireActivity().findViewById<ImageView>(R.id.expandedImage).apply {
            setImageResource(R.drawable.simple_rectangle)
            adjustViewBounds = false
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFilmClickListener) {
            clickListener = context
        } else {
            Throwable("Activity must implement OnFilmClickListener")
        }
    }

    private fun initRecyclerView(view: View) {
        if (view is RecyclerView) {
            with(view) {
                recycler = this
                layoutManager =
                    if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        LinearLayoutManager(context)
                    } else {
                        GridLayoutManager(context, 2)
                    }

                val films: ArrayList<Film> = FilmRepository.filmCollection
                adapter =
                    FilmRecyclerViewAdapter(films,
                        clickListener = { filmItem, _ ->
                            clickListener?.onClick(filmItem.id)
                        },
                        likeClickListener = { filmItem, position ->
                            filmItem.isFavorite = !filmItem.isFavorite
                            this.adapter?.notifyItemChanged(position)
                            val text =
                                if (filmItem.isFavorite) resources.getString(R.string.like_film_snackbar_text)
                                else resources.getString(R.string.unlike_film_snackbar_text)
                            Snackbar.make(this, text, Snackbar.LENGTH_SHORT).apply {
                                setAction(R.string.Undo) {
                                    filmItem.isFavorite = !filmItem.isFavorite
                                    this@with.adapter?.notifyItemChanged(position)
                                }
                            }.show()
                        }
                    )

                val filmDecoration =
                    CustomDecorator(requireContext(), DividerItemDecoration.VERTICAL)
                ContextCompat.getDrawable(requireActivity(), R.drawable.line_2dp)?.let {
                    filmDecoration.setDrawable(it)
                }
                addItemDecoration(filmDecoration)
            }
        }
    }
}