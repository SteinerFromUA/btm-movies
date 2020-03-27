package com.steiner.btmmovies.app.ui.main.home.favourites

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.dropbox.android.external.store4.StoreResponse
import com.steiner.btmmovies.app.R
import com.steiner.btmmovies.app.databinding.FragmentFavouriteMoviesBinding
import com.steiner.btmmovies.app.ui.main.home.HomeFragment
import com.steiner.btmmovies.app.ui.main.home.HomeFragmentDirections
import com.steiner.btmmovies.core.extension.beginDelayedTransition
import com.steiner.btmmovies.core.extension.dpToPx
import com.steiner.btmmovies.core.extension.exhaustive
import com.steiner.btmmovies.core.extension.newFragment
import com.steiner.btmmovies.core.extension.setRefreshLock
import com.steiner.btmmovies.core.observeEvent
import com.steiner.btmmovies.core.ui.BaseFragment
import com.steiner.btmmovies.core.widget.SpaceItemDecoration
import com.steiner.btmmovies.model.block.FavouriteMovieBlock

/**
 *
 */
class FavouriteMoviesFragment :
    BaseFragment(R.layout.fragment_favourite_movies), FavouriteMoviesAdapterActionListener {

    private val viewModel: FavouriteMoviesViewModel by activityViewModels()
    private val favouriteFilmsAdapter: FavouriteMoviesAdapter by lazy {
        FavouriteMoviesAdapter(this)
    }

    private lateinit var binding: FragmentFavouriteMoviesBinding
    private lateinit var navController: NavController

    override fun onAttach(context: Context) {
        (requireParentFragment() as HomeFragment).accountSubComponent
            .inject(this)

        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentFavouriteMoviesBinding.bind(view)

        navController = findNavController()

        /** Unsupported! */
        binding.srlFavouriteFilms.apply {
            isEnabled = false
            isRefreshing = false
            setColorSchemeColors(*resources.getIntArray(R.array.swipe_refresh_indicator))
            setOnRefreshListener { /**  */ }
            setRefreshLock { !binding.rvFavouriteFilms.canScrollVertically(-1) }
        }

        binding.rvFavouriteFilms.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(
                SpaceItemDecoration(
                    start = requireContext().dpToPx(8f),
                    top = requireContext().dpToPx(4f),
                    end = requireContext().dpToPx(8f),
                    bottom = requireContext().dpToPx(4f)
                )
            )
        }

        subscribeToViewModel()
    }

    override fun onMovieClicked(itemView: View, item: FavouriteMovieBlock) {
        navController.navigate(
            HomeFragmentDirections.actionHomeToMovieDetails(
                movieId = item.movieId
            )
        )
    }

    override fun onRemoveFromFavClicked(itemView: View, item: FavouriteMovieBlock) {
        viewModel.removeFromFavourites(item.movieId)
    }

    override fun onShareClicked(itemView: View, item: FavouriteMovieBlock) {
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_SUBJECT, item.title)
            putExtra(Intent.EXTRA_TEXT, item.description)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share movie via"))
    }

    private fun subscribeToViewModel() {
        /**  */
        viewModel.anyNoticeEvent.observeEvent(viewLifecycleOwner) { notice ->
            showNotice(notice)
        }

        /**  */
        viewModel.dataResponse.observe(viewLifecycleOwner) { dataResponse ->
            when (dataResponse) {
                is StoreResponse.Data -> showDataValue(dataResponse.value)
                is StoreResponse.Loading -> showLoading()
                is StoreResponse.Error -> showError()
            }.exhaustive
        }
    }

    private fun showDataValue(value: List<FavouriteMovieBlock>) = binding.run {
        if (rvFavouriteFilms.adapter == null) {
            rvFavouriteFilms.adapter = favouriteFilmsAdapter
        }
        favouriteFilmsAdapter.submitList(value) {
            if (favouriteFilmsAdapter.itemCount > 0) {
                showContent()
            } else {
                showEmpty()
            }
        }
    }

    private fun showContent(withAnimation: Boolean = true) = binding.run {
        if (withAnimation) {
            clInternal.beginDelayedTransition()
        }
        groupEmptyInfo.visibility = View.GONE
        groupErrorInfo.visibility = View.GONE
        indicatorLoading.visibility = View.GONE
        rvFavouriteFilms.visibility = View.VISIBLE
    }

    private fun showEmpty(withAnimation: Boolean = true) = binding.run {
        if (withAnimation) {
            clInternal.beginDelayedTransition()
        }
        rvFavouriteFilms.visibility = View.GONE
        groupErrorInfo.visibility = View.GONE
        indicatorLoading.visibility = View.GONE
        groupEmptyInfo.visibility = View.VISIBLE
    }

    private fun showError(withAnimation: Boolean = true) = binding.run {
        if (withAnimation) {
            clInternal.beginDelayedTransition()
        }
        rvFavouriteFilms.visibility = View.GONE
        groupEmptyInfo.visibility = View.GONE
        indicatorLoading.visibility = View.GONE
        groupErrorInfo.visibility = View.VISIBLE
    }

    private fun showLoading(withAnimation: Boolean = true) = binding.run {
        if (withAnimation) {
            clInternal.beginDelayedTransition()
        }
        rvFavouriteFilms.visibility = View.GONE
        groupEmptyInfo.visibility = View.GONE
        groupErrorInfo.visibility = View.GONE
        indicatorLoading.visibility = View.VISIBLE
    }

    companion object {
        @JvmStatic
        fun newInstance(): FavouriteMoviesFragment = newFragment {}
    }
}