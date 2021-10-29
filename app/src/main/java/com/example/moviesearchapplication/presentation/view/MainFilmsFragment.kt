package com.example.moviesearchapplication.presentation.view

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
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearchapplication.App
import com.example.moviesearchapplication.R
import com.example.moviesearchapplication.data.model.entities.Film
import com.example.moviesearchapplication.databinding.FragmentMainFilmsListBinding
import com.example.moviesearchapplication.presentation.utilities.CustomDecorator
import com.example.moviesearchapplication.presentation.viewmodel.FilmListViewModel
import com.example.moviesearchapplication.presentation.viewmodel.MainViewModelFactory
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class MainFilmsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    val viewModel: FilmListViewModel by activityViewModels{viewModelFactory}
    private lateinit var binding: FragmentMainFilmsListBinding

    var clickListener: OnFilmClickListener? = null
    var watchLaterClickListener: OnWatchesClickListeners? = null
    private lateinit var adapter: FilmRecyclerViewAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFilmClickListener) {
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        App.instance.applicationComponent.inject(this)
        binding = FragmentMainFilmsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initToolbar()
        initRecyclerView(binding.list)

        binding.swiperefresh.post{
            binding.swiperefresh.isRefreshing = true
            viewModel.tryLoadDataAgain()
        }
        binding.swiperefresh.setOnRefreshListener {
            viewModel.tryLoadDataAgain()
        }

        viewModel.loadingLiveData.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading == false)
                binding.swiperefresh.isRefreshing = false
        })

        viewModel.allFilms.observe(viewLifecycleOwner, { films ->
            adapter.setData(films as ArrayList<Film>)
        })
        viewModel.error.observe(viewLifecycleOwner, { error->
            if (error != "") {
                Snackbar.make(binding.list, error, Snackbar.LENGTH_SHORT).apply {
                    setAction(R.string.retry) {
                        viewModel.tryLoadDataAgain()
                    }
                }.show()
            }
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

    private fun initRecyclerView(view: View) {
        if (view is RecyclerView) {
            with(view) {
                val mLayoutManager = if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    LinearLayoutManager(context)
                } else {
                    GridLayoutManager(context, 2)
                }
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
                                               val visibleItemCount = mLayoutManager.childCount
                        val totalItemCount = mLayoutManager.itemCount
                        val firstVisibleItemPosition: Int =
                            mLayoutManager.findFirstVisibleItemPosition()
                        val needLoad = (visibleItemCount + firstVisibleItemPosition) >= totalItemCount

                        if (!viewModel.loadingLiveData.value!! && !viewModel.isLastPage && needLoad) {
                            viewModel.loadNextPageOnScroll()
                        }
                    }
                })
            }
        }
    }

    private fun setAdapter(list: List<Film>){
        val filmAdapter  =
            FilmRecyclerViewAdapter(
                list as ArrayList<Film>,
                clickListener = itemClickListener,
                likeClickListener = likeClickListener,
                watchLaterClickListener = iconWatchLaterClickListener
            )
        binding.list.adapter = filmAdapter
        adapter = filmAdapter
    }

    // region clickListeners
    private val likeClickListener  = object : FilmRecyclerViewAdapter.OnLikeClickListener {
        override fun onLikeClick(item: Film, position: Int) {
            item.isFavorite = !item.isFavorite
            viewModel.updateLikeState(item)
            binding.list.adapter?.notifyItemChanged(position)
            val text =
                if (item.isFavorite) resources.getString(R.string.like_film_snackbar_text)
                else resources.getString(R.string.unlike_film_snackbar_text)
            Snackbar.make(binding.list, text, Snackbar.LENGTH_SHORT).apply {
                setAction(R.string.Undo) {
                    item.isFavorite = !item.isFavorite
                    viewModel.updateLikeState(item)
                    binding.list.adapter?.notifyItemChanged(position)
                }
            }.show()
        }
    }

    private val itemClickListener = object : FilmRecyclerViewAdapter.OnItemClickListener{
        override fun onItemClick(item: Film, position: Int) {
            clickListener?.onClick(item.id)
        }
    }
    private val iconWatchLaterClickListener = object : FilmRecyclerViewAdapter.OnWatchlaterClickListener{
        override fun onIconClick(item: Film) {
            watchLaterClickListener?.onWatchIconClick(item.id)
        }
    }
    // endregion
}