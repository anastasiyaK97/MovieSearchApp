package com.example.moviesearchapplication.presentation.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearchapplication.R
import com.example.moviesearchapplication.data.model.entities.Film
import com.example.moviesearchapplication.presentation.utilities.CustomDecorator
import com.example.moviesearchapplication.presentation.viewmodel.FilmListViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar

class FavoriteFilmsFragment : Fragment() {

    private val viewModel: FilmListViewModel by activityViewModels()

    private lateinit var recycler : RecyclerView
    private var adapter: FilmRecyclerViewAdapter? = null
    var clickListener: OnFilmClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fav_films_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initToolbar()
        recycler = view.findViewById(R.id.list)
        initRecycler(recycler)

        viewModel.favoriteFilms.observe(viewLifecycleOwner, Observer<List<Film>>{films ->
            adapter?.setData(films as ArrayList<Film>)
        })
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
                setAdapter(viewModel.favoriteFilms.value?:ArrayList())

                val filmDecoration =
                    CustomDecorator(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                ContextCompat.getDrawable(requireActivity(), R.drawable.line_2dp)?.let {
                    filmDecoration.setDrawable(it)
                }
                addItemDecoration(filmDecoration)

                //С аниматором почему-то не работает: после
                //this@with.adapter?.notifyItemInserted(position)
                //добавляется пустой holder (без данных).
                // Не понимаю почему......

                /*val animator = FilmItemAnimator(requireContext())
                itemAnimator = animator*/
            }
        }
    }

    private fun setAdapter(favoriteFilms: List<Film>) {
        val filmAdapter  = FilmRecyclerViewAdapter(
            favoriteFilms as ArrayList,
            clickListener = itemClickListener,
            likeClickListener = likeClickListener,
            watchLaterClickListener = iconWatchLaterClickListener
        )
        adapter = filmAdapter
        recycler.adapter = filmAdapter
    }

    // region clickListeners
    private val likeClickListener  = object : FilmRecyclerViewAdapter.OnLikeClickListener {
        override fun onLikeClick(filmItem: Film, position: Int) {
            filmItem.isFavorite = !filmItem.isFavorite
            viewModel.removeFromFavorite(filmItem)
            recycler.adapter?.notifyItemRemoved(position)
            recycler.adapter?.notifyItemRangeChanged(
                position,
                viewModel.favoriteFilms.value?.size?:position - position
            )
            Snackbar.make(
                recycler,
                R.string.unlike_film_snackbar_text,
                Snackbar.LENGTH_SHORT
            ).apply {
                setAction(R.string.Undo) {
                    filmItem.isFavorite = !filmItem.isFavorite
                    viewModel.addToFavorite(filmItem)
                    recycler.adapter?.notifyItemRangeChanged(
                        position, viewModel.favoriteFilms.value?.size?:position - position
                    )
                }
            }.show()
        }
    }

    private val itemClickListener = object : FilmRecyclerViewAdapter.OnItemClickListener{
        override fun onItemClick(filmItem: Film, position: Int) {
            clickListener?.onClick(filmItem.id)
        }
    }

    private val iconWatchLaterClickListener = object : FilmRecyclerViewAdapter.OnWatchlaterClickListener{
        override fun onIconClick(item: Film) {
            startActivity(
                Intent(requireContext(), SetUpWatchLaterFragment::class.java)
            )
        }
    }
    // endregion
}