package com.steiner.btmmovies.model.block

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.steiner.btmmovies.model.enumeration.FavouriteState
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 *
 */
@Parcelize
data class OngoingMovieBlock(
    @ColumnInfo(name = "tmdb_movie_id") val movieId: Int,
    @ColumnInfo(name = "title") val title: String? = null,
    @ColumnInfo(name = "description") val description: String? = null,
    @ColumnInfo(name = "poster_url") val posterUrl: String? = null,
    @ColumnInfo(name = "ongoing_page") val page: Int,
    @ColumnInfo(name = "favourite_state") val favouriteState: FavouriteState
): Serializable, Parcelable
