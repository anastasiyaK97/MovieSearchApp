package com.example.moviesearchapplication.presentation.view

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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearchapplication.R
import com.example.moviesearchapplication.data.model.entities.Film
import com.example.moviesearchapplication.presentation.utilities.CustomDecorator
import com.example.moviesearchapplication.presentation.viewmodel.FilmListViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar

const val TAG = "LOG_TAG"

class MainFilmsFragment : Fragment() {

    var clickListener: OnFilmClickListener? = null
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: FilmRecyclerViewAdapter
    val viewModel: FilmListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_films_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initToolbar()
        initRecyclerView(view)

        viewModel.allFilms.observe(viewLifecycleOwner, Observer<List<Film>>{ films ->
            setAdapter(films)
        })
        viewModel.error.observe(viewLifecycleOwner, Observer { error->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        })
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
                val mLayoutManager = if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    LinearLayoutManager(context)
                } else {
                    GridLayoutManager(context, 2)
                }
                recycler = this
                layoutManager = mLayoutManager

                setAdapter(viewModel.allFilms.value?: ArrayList())

                val filmDecoration =
                    CustomDecorator(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                ContextCompat.getDrawable(requireActivity(), R.drawable.line_2dp)?.let {
                    filmDecoration.setDrawable(it)
                }
                addItemDecoration(filmDecoration)

                addOnScrollListener(object: RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        val size = viewModel.allFilms.value?.size ?: 0

                        val visibleItemCount = mLayoutManager.childCount
                        val totalItemCount = mLayoutManager.itemCount
                        val firstVisibleItemPosition: Int =
                            mLayoutManager.findFirstVisibleItemPosition()
                        val needLoad = (visibleItemCount + firstVisibleItemPosition) >= totalItemCount

                        Log.d("LOG_TAG1", "visibleItemCount = ${visibleItemCount}, firstVisibleItemPosition = ${firstVisibleItemPosition}, totalItemCount = ${totalItemCount}")

                        if (!viewModel.isLoading && !viewModel.isLastPage && needLoad) {
                            Log.d("LOG_TAG1", "call load method")
                            viewModel.loadPageOnScroll()
                            //recycler.adapter?.notifyDataSetChanged()
                        }
                    }
                })
            }
        }
    }

    private fun setAdapter(list: List<Film>){
        recycler.adapter =
            FilmRecyclerViewAdapter(
                list as ArrayList<Film>,
                clickListener = itemClickListener,
                likeClickListener = likeClickListener
            )
    }
    // region clickListeners
    private val likeClickListener  = object : FilmRecyclerViewAdapter.OnLikeClickListener {
        override fun onLikeClick(filmItem: Film, position: Int) {
            filmItem.isFavorite = !filmItem.isFavorite
            viewModel.update(filmItem)
            recycler.adapter?.notifyItemChanged(position)
            val text =
                if (filmItem.isFavorite) resources.getString(R.string.like_film_snackbar_text)
                else resources.getString(R.string.unlike_film_snackbar_text)
            Snackbar.make(recycler, text, Snackbar.LENGTH_SHORT).apply {
                setAction(R.string.Undo) {
                    filmItem.isFavorite = !filmItem.isFavorite
                    viewModel.update(filmItem)
                    recycler.adapter?.notifyItemChanged(position)
                }
            }.show()
        }
    }

    private val itemClickListener = object : FilmRecyclerViewAdapter.OnItemClickListener{
        override fun onItemClick(filmItem: Film, position: Int) {
            clickListener?.onClick(filmItem.id)
        }
    }
    // endregion
}