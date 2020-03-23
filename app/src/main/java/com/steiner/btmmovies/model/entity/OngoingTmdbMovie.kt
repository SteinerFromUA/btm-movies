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
    tableName = "ongoing_tmdb_movies",
    indices = [
        Index(value = ["ongoing_movie_id"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(
            entity = TmdbMovie::class,
            parentColumns = ["tmdb_movie_id"],
            childColumns = ["ongoing_movie_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
            deferred = true
        )
    ]
)
data class OngoingTmdbMovie(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "ongoing_movie_id") val movieId: Int,
    @ColumnInfo(name = "page") val page: Int,
    @ColumnInfo(name = "page_position") val pagePosition: Int
) : Serializable, Parcelable