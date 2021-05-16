package com.example.moviesearchapplication.view.main

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearchapplication.App
import com.example.moviesearchapplication.R
import com.example.moviesearchapplication.data.FilmRepository
import com.example.moviesearchapplication.data.database.Database
import com.example.moviesearchapplication.data.model.Film
import com.example.moviesearchapplication.view.OnFilmClickListener
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

const val TAG = "LOG_TAG"

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

                App.instance.filmApiService.getFilms()
                    .enqueue(object: Callback<List<Film>> {
                        override fun onFailure(call: Call<List<Film>>, t: Throwable) {
                            Toast.makeText(requireActivity(), "Request error", Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(call: Call<List<Film>>, response: Response<List<Film>>) {
                            FilmRepository.filmCollection.clear()
                            if (response.isSuccessful) {
                                response.body()?.forEach {
                                    FilmRepository.filmCollection.add(it)
                                    Executors.newSingleThreadScheduledExecutor().execute{
                                        Database.getInstance(requireContext()).getFilmDao().insert(it)
                                        Log.d(TAG, "${it.title} (${it.id}) was inserted to DB")
                                    }
                                }
                            }
                            adapter?.notifyDataSetChanged()
                        }
                    })

                val films: MutableList<Film> = FilmRepository.filmCollection
                adapter =
                    FilmRecyclerViewAdapter(
                        films,
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
                    CustomDecorator(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                ContextCompat.getDrawable(requireActivity(), R.drawable.line_2dp)?.let {
                    filmDecoration.setDrawable(it)
                }
                addItemDecoration(filmDecoration)
            }
        }
    }
}