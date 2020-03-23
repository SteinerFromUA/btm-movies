package com.steiner.btmmovies.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.steiner.btmmovies.core.data.BaseDao
import com.steiner.btmmovies.model.entity.TmdbMovie

/**
 *
 */
@Dao
abstract class TmdbMovieDao : BaseDao<TmdbMovie>() {

    @Query("SELECT EXISTS(SELECT 1 FROM tmdb_movies WHERE tmdb_movie_id LIKE :id LIMIT 1)")
    abstract suspend fun isExistingByIdSuspend(id: Int): Boolean

    @Query("DELETE FROM tmdb_movies WHERE tmdb_movie_id = :id AND tmdb_movie_id != (SELECT ongoing_tmdb_movies.ongoing_movie_id FROM ongoing_tmdb_movies WHERE ongoing_movie_id = :id LIMIT 1)")
    abstract suspend fun deleteIfNotInOngoingSuspend(id: Int)

    @Query("DELETE FROM tmdb_movies")
    abstract suspend fun clearAllSuspend()

    @Query("DELETE FROM tmdb_movies WHERE tmdb_movie_id NOT IN (SELECT favourite_tmdb_movies.favourite_movie_id FROM favourite_tmdb_movies)")
    abstract suspend fun clearExceptFavouritesSuspend()
}
