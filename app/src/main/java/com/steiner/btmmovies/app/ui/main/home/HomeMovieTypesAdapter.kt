package com.steiner.btmmovies.app.ui.main.home

import androidx.fragment.app.Fragment
import com.steiner.btmmovies.app.R
import com.steiner.btmmovies.app.ui.main.home.favourites.FavouriteMoviesFragment
import com.steiner.btmmovies.app.ui.main.home.ongoing.OngoingMoviesFragment
import com.steiner.btmmovies.core.extension.exhaustive
import com.steiner.btmmovies.core.widget.ExtendedFragmentStateAdapter

/**
 *
 */
class HomeMovieTypesAdapter(
    private val fragment: Fragment
) : ExtendedFragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = PAGE_COUNT

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            INDEX_ONGOING -> OngoingMoviesFragment.newInstance()
            INDEX_FAVOURITES -> FavouriteMoviesFragment.newInstance()
            else -> {
                throw IllegalArgumentException("Unknown position: $position!")
            }
        }.exhaustive
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            INDEX_ONGOING -> fragment.requireContext().getText(R.string.tab_films_ongoing)
            INDEX_FAVOURITES -> fragment.requireContext().getText(R.string.tab_films_favourites)
            else -> null
        }.exhaustive
    }

    companion object {
        private const val INDEX_ONGOING = 0
        private const val INDEX_FAVOURITES = 1

        private const val PAGE_COUNT = 2
    }
}