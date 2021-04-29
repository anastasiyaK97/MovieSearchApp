package com.example.moviesearchapplication.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearchapplication.R
import com.example.moviesearchapplication.data.Film
import com.example.moviesearchapplication.data.FilmRepository

class FavoriteFilmsFragment : Fragment() {

    private lateinit var recycler : RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main_films_list, container, false)
        initRecycler(view)

        return view
    }

    private fun initRecycler(view: View) {
        if (view is RecyclerView) {
            with(view){
                recycler = this
                layoutManager = LinearLayoutManager(context)
                val favoriteFilms : ArrayList<Film> = FilmRepository.getFavoriteFilms()
                adapter = FilmRecyclerViewAdapter (favoriteFilms,
                    {
                            filmItem, _ ->
                        val intent = Intent(context, FilmDetailActivity::class.java).apply {
                            putExtra(FilmDetailActivity.FILM_EXTRA, filmItem.id)
                        }
                        startActivityForResult(intent, MainFilmsFragment.INTENT_CODE)
                    },
                    {
                            filmItem, position ->
                        filmItem.isFavorite = !filmItem.isFavorite
                        favoriteFilms.remove(filmItem)
                        this.adapter?.notifyItemRemoved(position)
                        this.adapter?.notifyItemRangeChanged(position, favoriteFilms.size - position)
                    }
                )
                val filmDecoration = CustomDecorator(requireContext(), DividerItemDecoration.VERTICAL)
                ContextCompat.getDrawable(requireActivity(), R.drawable.line_2dp)?.let {
                    filmDecoration.setDrawable(it)
                }
                addItemDecoration(filmDecoration)

                val animator = FilmItemAnimator(requireContext())
                itemAnimator = animator
            }
        }
    }
}