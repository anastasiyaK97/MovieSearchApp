package com.example.moviesearchapplication.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearchapplication.App
import com.example.moviesearchapplication.R
import com.example.moviesearchapplication.data.model.entities.Film
import com.example.moviesearchapplication.databinding.FragmentFavFilmsListBinding
import com.example.moviesearchapplication.presentation.utilities.CustomDecorator
import com.example.moviesearchapplication.presentation.viewmodel.FilmListViewModel
import com.example.moviesearchapplication.presentation.viewmodel.MainViewModelFactory
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class FavoriteFilmsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    private val viewModel: FilmListViewModel by activityViewModels{viewModelFactory}
    private lateinit var binding: FragmentFavFilmsListBinding

    private var adapter: FilmRecyclerViewAdapter? = null
    private var clickListener: OnFilmClickListener? = null
    private var watchLaterClickListener: OnWatchesClickListeners? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        App.instance.applicationComponent.inject(this)
        binding = FragmentFavFilmsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initToolbar()
        initRecycler(binding.list)

        viewModel.favoriteFilms.observe(viewLifecycleOwner, { films ->
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
        if (context is OnWatchesClickListeners) {
            watchLaterClickListener = context
        } else {
            Throwable("Activity must implement OnWatchesClickListeners")
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
        binding.list.adapter = filmAdapter
    }

    // region clickListeners
    private val likeClickListener  = object : FilmRecyclerViewAdapter.OnLikeClickListener {
        override fun onLikeClick(item: Film, position: Int) {
            item.isFavorite = !item.isFavorite
            viewModel.removeFromFavorite(item)
            binding.list.adapter?.notifyItemRemoved(position)
            binding.list.adapter?.notifyItemRangeChanged(
                position,
                viewModel.favoriteFilms.value?.size?:position - position
            )
            Snackbar.make(
                binding.list,
                R.string.unlike_film_snackbar_text,
                Snackbar.LENGTH_SHORT
            ).apply {
                setAction(R.string.Undo) {
                    item.isFavorite = !item.isFavorite
                    viewModel.addToFavorite(item)
                    binding.list.adapter?.notifyItemRangeChanged(
                        position, viewModel.favoriteFilms.value?.size?:position - position
                    )
                }
            }.show()
        }
    }

    private val itemClickListener = object : FilmRecyclerViewAdapter.OnItemClickListener{
        override fun onItemClick(item: Film, position: Int) {
            clickListener?.onClick(item.id)
        }
    }

    private val iconWatchLaterClickListener = object : FilmRecyclerViewAdapter.OnWatchlaterClickListener {
        override fun onIconClick(item: Film) {
            watchLaterClickListener?.onWatchIconClick(item.id)
        }
    }
    // endregion
}