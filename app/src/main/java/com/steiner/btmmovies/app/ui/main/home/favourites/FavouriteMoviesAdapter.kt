package com.steiner.btmmovies.app.ui.main.home.favourites

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.steiner.btmmovies.app.R
import com.steiner.btmmovies.core.extension.exhaustive
import com.steiner.btmmovies.core.widget.ExtendedListAdapter
import com.steiner.btmmovies.core.widget.Payloads
import com.steiner.btmmovies.model.block.FavouriteMovieBlock

private typealias ViewHolder = FavouriteMoviesItemViewHolder<*>

/**
 *
 */
class FavouriteMoviesAdapter(
    private val listener: FavouriteMoviesAdapterActionListener
) : ExtendedListAdapter<Any, ViewHolder>(DiffCallback) {

    private val viewHolderFactory = FavouriteMoviesItemViewHoldersFactory(listener)

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is FavouriteMovieBlock -> VIEW_FAVOURITE_MOVIE_CARD
            else -> throw IllegalArgumentException("Unknown item in position: $position")
        }.exhaustive
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            VIEW_FAVOURITE_MOVIE_CARD -> viewHolderFactory.createForFavouriteMovieCard(parent)
            else -> throw IllegalArgumentException("Unknown item viewType: $viewType")
        }.exhaustive
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: Payloads) {
        when (holder) {
            is FavouriteMovieCardViewHolder -> holder.bind(
                getItem(position) as FavouriteMovieBlock
            )
        }.exhaustive
    }

    companion object {
        private const val VIEW_FAVOURITE_MOVIE_CARD = R.layout.item_favourite_movie_card
    }
}

/**
 * Interface for selectable item action in [FavouriteMoviesAdapter]
 */
interface FavouriteMoviesAdapterActionListener : FavouriteMovieCardViewHolder.ActionListener

/**
 * [DiffUtil.ItemCallback]s presented by [FavouriteMoviesAdapter] adapter.
 */
internal object DiffCallback : DiffUtil.ItemCallback<Any>() {

    override fun areItemsTheSame(
        oldItem: Any,
        newItem: Any
    ): Boolean = when (oldItem) {
        is FavouriteMovieBlock -> newItem is FavouriteMovieBlock && oldItem.movieId == newItem.movieId
        else -> false
    }.exhaustive

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: Any,
        newItem: Any
    ): Boolean = oldItem == newItem
}
