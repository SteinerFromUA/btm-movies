package com.steiner.btmmovies.app.ui.main.home.ongoing

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.steiner.btmmovies.app.R
import com.steiner.btmmovies.core.extension.exhaustive
import com.steiner.btmmovies.core.widget.ExtendedPagedListAdapter
import com.steiner.btmmovies.core.widget.Payloads
import com.steiner.btmmovies.model.block.OngoingMovieBlock

private typealias ViewHolder = OngoingMoviesItemViewHolder<*>

/**
 *
 */
class OngoingMoviesAdapter(
    private val listener: OngoingMoviesAdapterActionListener
) : ExtendedPagedListAdapter<Any, ViewHolder>(DiffCallback) {

    private val viewHolderFactory = OngoingMoviesItemViewHoldersFactory(listener)

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is OngoingMovieBlock -> VIEW_ONGOING_MOVIE_CARD
            else -> throw IllegalArgumentException("Unknown item in position: $position")
        }.exhaustive
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            VIEW_ONGOING_MOVIE_CARD -> viewHolderFactory.createForOngoingMovieCard(parent)
            else -> throw IllegalArgumentException("Unknown item viewType: $viewType")
        }.exhaustive
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: Payloads) {
        when (holder) {
            is OngoingMovieCardViewHolder -> holder.bind(
                getItem(position) as OngoingMovieBlock
            )
        }.exhaustive
    }

    companion object {
        private const val VIEW_ONGOING_MOVIE_CARD = R.layout.item_ongoing_movie_card
    }
}

/**
 * Interface for selectable item action in [OngoingMoviesAdapter]
 */
interface OngoingMoviesAdapterActionListener : OngoingMovieCardViewHolder.ActionListener

/**
 * [DiffUtil.ItemCallback]s presented by [OngoingMoviesAdapter] adapter.
 */
internal object DiffCallback : DiffUtil.ItemCallback<Any>() {

    override fun areItemsTheSame(
        oldItem: Any,
        newItem: Any
    ): Boolean = when (oldItem) {
        is OngoingMovieBlock -> newItem is OngoingMovieBlock && oldItem.movieId == newItem.movieId
        else -> false
    }.exhaustive

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: Any,
        newItem: Any
    ): Boolean = oldItem == newItem
}
