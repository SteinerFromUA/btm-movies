package com.steiner.btmmovies.app.ui.main.home.ongoing

import android.view.ViewGroup
import com.steiner.btmmovies.app.databinding.ItemOngoingMovieCardBinding
import com.steiner.btmmovies.core.extension.inflater

/**
 *
 */
class OngoingMoviesItemViewHoldersFactory(
    private val listener: OngoingMoviesAdapterActionListener
) {

    fun createForOngoingMovieCard(parent: ViewGroup): OngoingMovieCardViewHolder {
        return OngoingMovieCardViewHolder(
            binding = ItemOngoingMovieCardBinding.inflate(
                parent.inflater,
                parent,
                false
            ),
            listener = listener
        )
    }
}