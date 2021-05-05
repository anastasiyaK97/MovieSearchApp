package com.example.moviesearchapplication.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearchapplication.R
import com.example.moviesearchapplication.data.Film
import com.example.moviesearchapplication.data.FilmRepository
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar

class FavoriteFilmsFragment : Fragment() {

    private lateinit var recycler : RecyclerView
    var clickListener: OnFilmClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main_films_list, container, false)
        initRecycler(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initToolbar()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnFilmClickListener) {
            clickListener = context
        } else {
            Throwable("Activity must implement OnFilmClickListener")
        }
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).supportActionBar
            ?.setDisplayHomeAsUpEnabled(true)

        requireActivity().findViewById<AppBarLayout>(R.id.appbar).setExpanded(false, false)
        requireActivity().findViewById<CollapsingToolbarLayout>(R.id.main_collapsing).title = resources.getString(R.string.favorites_nav_title)
        requireActivity().findViewById<ImageView>(R.id.expandedImage).apply{
            setImageResource(R.drawable.simple_rectangle)
            adjustViewBounds = false
        }
    }

    private fun initRecycler(view: View) {
        if (view is RecyclerView) {
            with(view){
                recycler = this
                layoutManager = LinearLayoutManager(context)
                val favoriteFilms : ArrayList<Film> = FilmRepository.getFavoriteFilms()
                adapter = FilmRecyclerViewAdapter (favoriteFilms,
                    clickListener = {
                        filmItem, _ ->
                        clickListener?.onClick(filmItem.id)
                    },
                    likeClickListener = {
                        filmItem, position ->
                        filmItem.isFavorite = !filmItem.isFavorite
                        favoriteFilms.remove(filmItem)
                        this.adapter?.notifyItemRemoved(position)
                        this.adapter?.notifyItemRangeChanged(position, favoriteFilms.size - position)
                        Snackbar.make(this, R.string.unlike_film_snackbar_text, Snackbar.LENGTH_SHORT).apply {
                            setAction(R.string.Undo) {
                                filmItem.isFavorite = !filmItem.isFavorite
                                favoriteFilms.add(position, filmItem)
                                this@with.adapter?.notifyItemInserted(position)
                                this@with.adapter?.notifyItemRangeChanged(
                                position,favoriteFilms.size - position)
                            }}.show()
                    }
                )
                val filmDecoration = CustomDecorator(requireContext(), DividerItemDecoration.VERTICAL)
                ContextCompat.getDrawable(requireActivity(), R.drawable.line_2dp)?.let {
                    filmDecoration.setDrawable(it)
                }
                addItemDecoration(filmDecoration)

                //С аниматором почему-то не работает: после
                //this@with.adapter?.notifyItemInserted(position)
                //добавляется пустой holder (без данных).
                // Не понимаю почему......

               /* val animator = FilmItemAnimator(requireContext())
                itemAnimator = animator*/
            }
        }
    }
}