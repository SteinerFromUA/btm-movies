package com.steiner.btmmovies.app.ui.main.home.favourites

import android.view.ViewGroup
import com.steiner.btmmovies.app.databinding.ItemFavouriteMovieCardBinding
import com.steiner.btmmovies.core.extension.inflater

/**
 *
 */
class FavouriteMoviesItemViewHoldersFactory(
    private val listener: FavouriteMoviesAdapterActionListener
) {

    fun createForFavouriteMovieCard(parent: ViewGroup): FavouriteMovieCardViewHolder {
        return FavouriteMovieCardViewHolder(
            binding = ItemFavouriteMovieCardBinding.inflate(
                parent.inflater,
                parent,
                false
            ),
            listener = listener
        )
    }
}