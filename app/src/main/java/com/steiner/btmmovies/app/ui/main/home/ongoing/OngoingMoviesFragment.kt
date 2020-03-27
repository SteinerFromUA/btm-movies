package com.steiner.btmmovies.app.ui.main.home.ongoing

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.dropbox.android.external.store4.StoreResponse
import com.steiner.btmmovies.app.R
import com.steiner.btmmovies.app.databinding.FragmentOngoingMoviesBinding
import com.steiner.btmmovies.app.ui.main.home.HomeFragment
import com.steiner.btmmovies.app.ui.main.home.HomeFragmentDirections
import com.steiner.btmmovies.core.RefreshInfo
import com.steiner.btmmovies.core.extension.beginDelayedTransition
import com.steiner.btmmovies.core.extension.dpToPx
import com.steiner.btmmovies.core.extension.exhaustive
import com.steiner.btmmovies.core.extension.newFragment
import com.steiner.btmmovies.core.extension.setRefreshLock
import com.steiner.btmmovies.core.observeEvent
import com.steiner.btmmovies.core.ui.BaseFragment
import com.steiner.btmmovies.core.widget.SpaceItemDecoration
import com.steiner.btmmovies.model.block.OngoingMovieBlock
import com.steiner.btmmovies.model.enumeration.FavouriteState

/**
 *
 */
class OngoingMoviesFragment :
    BaseFragment(R.layout.fragment_ongoing_movies), OngoingMoviesAdapterActionListener {

    private val viewModel: OngoingMoviesViewModel by activityViewModels()
    private val ongoingFilmsAdapter: OngoingMoviesAdapter by lazy {
        OngoingMoviesAdapter(this)
    }

    private lateinit var binding: FragmentOngoingMoviesBinding
    private lateinit var navController: NavController

    override fun onAttach(context: Context) {
        (requireParentFragment() as HomeFragment).accountSubComponent
            .inject(this)

        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentOngoingMoviesBinding.bind(view)

        navController = findNavController()

        binding.srlOngoingFilms.apply {
            isEnabled = false
            isRefreshing = false
            setColorSchemeColors(*resources.getIntArray(R.array.swipe_refresh_indicator))
            setOnRefreshListener { viewModel.refreshList() }
            setRefreshLock { !binding.rvOngoingFilms.canScrollVertically(-1) }
        }

        binding.rvOngoingFilms.apply {
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

    override fun onMovieClicked(itemView: View, item: OngoingMovieBlock) {
        navController.navigate(
            HomeFragmentDirections.actionHomeToMovieDetails(
                movieId = item.movieId
            )
        )
    }

    override fun onAddToFavClicked(itemView: View, item: OngoingMovieBlock) {
        val newFavState = if (item.favouriteState == FavouriteState.UNLIKED) {
            FavouriteState.LIKED
        } else {
            FavouriteState.UNLIKED
        }
        viewModel.changeFavouriteState(item.movieId, newFavState)
    }

    override fun onShareClicked(itemView: View, item: OngoingMovieBlock) {
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
        viewModel.listRefreshInfo.observe(viewLifecycleOwner) { refreshInfo ->
            binding.srlOngoingFilms.apply {
                isEnabled = refreshInfo.isEnabled
                isRefreshing = refreshInfo.isRefreshing
            }
            if (refreshInfo is RefreshInfo.InProgress && refreshInfo.notice != null) {
                showNotice(refreshInfo.notice)
            }
        }

        /**  */
        viewModel.dataResponse.observe(viewLifecycleOwner) { dataResponse ->
            when (dataResponse) {
                is StoreResponse.Data -> showDataValue(dataResponse.value)
                is StoreResponse.Loading -> showLoading()
                is StoreResponse.Error -> showErrorValue(dataResponse.error)
            }.exhaustive
        }
    }

    private fun showDataValue(value: PagedList<OngoingMovieBlock>) = binding.run {
        if (rvOngoingFilms.adapter == null) {
            rvOngoingFilms.adapter = ongoingFilmsAdapter
        }
        @Suppress("UNCHECKED_CAST")
        ongoingFilmsAdapter.submitList(value as PagedList<Any>?) {
            if (ongoingFilmsAdapter.itemCount > 0) {
                showContent()
            } else {
                showEmpty()
            }
        }
    }

    private fun showErrorValue(error: Throwable) = binding.run {
        textOngoingFilmsErrorBody.text = getString(
            R.string.format_error_data_loading_body,
            error.localizedMessage
        )
        showError()
    }

    private fun showContent(withAnimation: Boolean = true) = binding.run {
        if (withAnimation) {
            clInternal.beginDelayedTransition()
        }
        groupEmptyInfo.visibility = View.GONE
        groupErrorInfo.visibility = View.GONE
        indicatorLoading.visibility = View.GONE
        rvOngoingFilms.visibility = View.VISIBLE
    }

    private fun showEmpty(withAnimation: Boolean = true) = binding.run {
        if (withAnimation) {
            clInternal.beginDelayedTransition()
        }
        rvOngoingFilms.visibility = View.GONE
        groupErrorInfo.visibility = View.GONE
        indicatorLoading.visibility = View.GONE
        groupEmptyInfo.visibility = View.VISIBLE
    }

    private fun showError(withAnimation: Boolean = true) = binding.run {
        if (withAnimation) {
            clInternal.beginDelayedTransition()
        }
        rvOngoingFilms.visibility = View.GONE
        groupEmptyInfo.visibility = View.GONE
        indicatorLoading.visibility = View.GONE
        groupErrorInfo.visibility = View.VISIBLE
    }

    private fun showLoading(withAnimation: Boolean = true) = binding.run {
        if (withAnimation) {
            clInternal.beginDelayedTransition()
        }
        rvOngoingFilms.visibility = View.GONE
        groupEmptyInfo.visibility = View.GONE
        groupErrorInfo.visibility = View.GONE
        indicatorLoading.visibility = View.VISIBLE
    }

    companion object {
        @JvmStatic
        fun newInstance(): OngoingMoviesFragment = newFragment {}
    }
}


