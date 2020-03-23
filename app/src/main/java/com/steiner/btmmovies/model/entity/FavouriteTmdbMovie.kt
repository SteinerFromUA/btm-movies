package com.steiner.btmmovies.model.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 *
 */
@Parcelize
@Entity(
    tableName = "favourite_tmdb_movies",
    indices = [
        Index(value = ["favourite_movie_id"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(
            entity = TmdbMovie::class,
            parentColumns = ["tmdb_movie_id"],
            childColumns = ["favourite_movie_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
            deferred = true
        )
    ]
)
data class FavouriteTmdbMovie(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "favourite_movie_id") val movieId: Int,
    @ColumnInfo(name = "liked_at") val likedAt: Long = 0
) : Serializable, Parcelable
