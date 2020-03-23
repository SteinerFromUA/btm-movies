package com.steiner.btmmovies.model.block

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 *
 */
@Parcelize
data class FavouriteMovieBlock(
    @ColumnInfo(name = "tmdb_movie_id") val movieId: Int,
    @ColumnInfo(name = "title") val title: String? = null,
    @ColumnInfo(name = "description") val description: String? = null,
    @ColumnInfo(name = "poster_url") val posterUrl: String? = null
): Serializable, Parcelable