package com.example.moviesearchapplication.view

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearchapplication.R
import com.example.moviesearchapplication.data.Film
import com.example.moviesearchapplication.data.FilmRepository

class MainFilmsFragment : Fragment() {

    companion object {
        const val INTENT_CODE = 1
    }

    private lateinit var recycler : RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main_films_list, container, false)
        initRecyclerView(view)

        return view
    }

    private fun initRecyclerView(view: View){
        if (view is RecyclerView) {
            with(view) {
                recycler = this
                layoutManager = if(activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    LinearLayoutManager(context)
                } else {
                    GridLayoutManager(context, 2)
                }

                val films : ArrayList<Film> = FilmRepository.filmCollection
                adapter =
                    FilmRecyclerViewAdapter(films,
                        {
                            filmItem, _ ->
                            val intent = Intent(context, FilmDetailActivity::class.java).apply {
                                putExtra(FilmDetailActivity.FILM_EXTRA, filmItem.id)
                            }
                            startActivityForResult(intent, INTENT_CODE)
                        },
                        {
                            filmItem, position ->
                            filmItem.isFavorite = !filmItem.isFavorite
                            this.adapter?.notifyItemChanged(position)
                        }
                    )

                val filmDecoration = CustomDecorator(requireContext(), DividerItemDecoration.VERTICAL)
                ContextCompat.getDrawable(requireActivity(), R.drawable.line_2dp)?.let {
                    filmDecoration.setDrawable(it)
                }
                addItemDecoration(filmDecoration)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == INTENT_CODE && resultCode == Activity.RESULT_OK && data != null) {
            recycler.adapter?.notifyItemChanged(data.getIntExtra(FilmDetailActivity.FILM_EXTRA, 0))
            Log.d(
                MainActivity.TAG, "Фильм ID = " +
                    "${data.getIntExtra(FilmDetailActivity.FILM_EXTRA, -1)}")
        }
    }

}