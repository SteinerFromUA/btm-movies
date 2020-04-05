package com.steiner.btmmovies.app.ui.main.home.favourites

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.steiner.btmmovies.app.R
import com.steiner.btmmovies.app.databinding.ItemFavouriteMovieCardBinding
import com.steiner.btmmovies.core.widget.BindableViewHolder
import com.steiner.btmmovies.model.block.FavouriteMovieBlock

/**
 * [RecyclerView.ViewHolder] types used by [FavouriteMoviesAdapter].
 */
sealed class FavouriteMoviesItemViewHolder<T : Any>(
    itemView: View
) : BindableViewHolder<T>(itemView)

/**
 *
 */
class FavouriteMovieCardViewHolder(
    private val binding: ItemFavouriteMovieCardBinding,
    listener: ActionListener
) : FavouriteMoviesItemViewHolder<FavouriteMovieBlock>(binding.root) {

    init {
        setListeners(listener)
    }

    override fun bind(item: FavouriteMovieBlock): Unit = binding.run {
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
    }

    private fun setListeners(listener: ActionListener) = binding.run {
        itemView.setOnClickListener {
            if (RecyclerView.NO_POSITION != bindingAdapterPosition) {
                (itemView.tag as? FavouriteMovieBlock)?.let { movie ->
                    listener.onMovieClicked(itemView, movie)
                }
            }
        }

        removeFromFav.setOnClickListener {
            if (RecyclerView.NO_POSITION != bindingAdapterPosition) {
                (itemView.tag as? FavouriteMovieBlock)?.let { movie ->
                    listener.onRemoveFromFavClicked(itemView, movie)
                }
            }
        }

        share.setOnClickListener {
            if (RecyclerView.NO_POSITION != bindingAdapterPosition) {
                (itemView.tag as? FavouriteMovieBlock)?.let { movie ->
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
        fun onMovieClicked(itemView: View, item: FavouriteMovieBlock) {
        }

        @JvmDefault
        fun onRemoveFromFavClicked(itemView: View, item: FavouriteMovieBlock) {
        }

        @JvmDefault
        fun onShareClicked(itemView: View, item: FavouriteMovieBlock) {
        }
    }
}
