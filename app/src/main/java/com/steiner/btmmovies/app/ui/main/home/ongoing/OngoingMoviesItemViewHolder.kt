package com.steiner.btmmovies.app.ui.main.home.ongoing

import android.view.View
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.steiner.btmmovies.app.R
import com.steiner.btmmovies.app.databinding.ItemOngoingMovieCardBinding
import com.steiner.btmmovies.core.widget.BindableViewHolder
import com.steiner.btmmovies.model.block.OngoingMovieBlock
import com.steiner.btmmovies.model.enumeration.FavouriteState

/**
 * [RecyclerView.ViewHolder] types used by [OngoingMoviesAdapter].
 */
sealed class OngoingMoviesItemViewHolder<T : Any>(
    itemView: View
) : BindableViewHolder<T>(itemView)

/**
 *
 */
class OngoingMovieCardViewHolder(
    private val binding: ItemOngoingMovieCardBinding,
    listener: ActionListener
) : OngoingMoviesItemViewHolder<OngoingMovieBlock>(binding.root) {

    init {
        setListeners(listener)
    }

    override fun bind(item: OngoingMovieBlock): Unit = binding.run {
        itemView.tag = item

        movieTitle.text = item.title
        movieDescription.text = item.description

        Glide.with(moviePoster)
            .load(item.posterUrl)
            .apply(
                RequestOptions
                    .fitCenterTransform()
                    .placeholder(R.drawable.tmdb_logo)
                    .error(R.drawable.tmdb_logo)
            )
            .into(moviePoster)

        addToFav.isInvisible = item.favouriteState == FavouriteState.LIKED
    }

    private fun setListeners(listener: ActionListener) = binding.run {
        itemView.setOnClickListener {
            if (RecyclerView.NO_POSITION != bindingAdapterPosition) {
                (itemView.tag as? OngoingMovieBlock)?.let { movie ->
                    listener.onMovieClicked(itemView, movie)
                }
            }
        }

        addToFav.setOnClickListener {
            if (RecyclerView.NO_POSITION != bindingAdapterPosition) {
                (itemView.tag as? OngoingMovieBlock)?.let { movie ->
                    listener.onAddToFavClicked(itemView, movie)
                }
            }
        }

        share.setOnClickListener {
            if (RecyclerView.NO_POSITION != bindingAdapterPosition) {
                (itemView.tag as? OngoingMovieBlock)?.let { movie ->
                    listener.onShareClicked(itemView, movie)
                }
            }
        }
    }

    /**
     *
     */
    interface ActionListener {

        @JvmDefault
        fun onMovieClicked(itemView: View, item: OngoingMovieBlock) {
        }

        @JvmDefault
        fun onAddToFavClicked(itemView: View, item: OngoingMovieBlock) {
        }

        @JvmDefault
        fun onShareClicked(itemView: View, item: OngoingMovieBlock) {
        }
    }
}
