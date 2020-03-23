package com.steiner.btmmovies.model.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 *
 */
@Parcelize
@Entity(
    tableName = "tmdb_movies",
    primaryKeys = ["tmdb_movie_id"],
    indices = [
        Index(value = ["tmdb_movie_id"], unique = true)
    ]
)
data class TmdbMovie(
    @ColumnInfo(name = "tmdb_movie_id") val movieId: Int = 0,
    @ColumnInfo(name = "title") val title: String? = null,
    @ColumnInfo(name = "description") val description: String? = null,
    @ColumnInfo(name = "poster_url") val posterUrl: String? = null
) : Serializable, Parcelable