package com.steiner.btmmovies.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.steiner.btmmovies.core.data.BaseDao
import com.steiner.btmmovies.model.block.FavouriteMovieBlock
import com.steiner.btmmovies.model.entity.FavouriteTmdbMovie
import kotlinx.coroutines.flow.Flow

/**
 *
 */
@Dao
abstract class FavouriteTmdbMovieDao : BaseDao<FavouriteTmdbMovie>() {

    @Query(
        value = """
            SELECT tmdb_movies.tmdb_movie_id, 
                   tmdb_movies.title, 
                   tmdb_movies.description,
                   tmdb_movies.poster_url
            FROM favourite_tmdb_movies 
                INNER JOIN tmdb_movies
                        ON favourite_tmdb_movies.favourite_movie_id = tmdb_movies.tmdb_movie_id
            ORDER BY favourite_tmdb_movies.liked_at DESC
    """
    )
    abstract fun takeAllBlocksAsFlow(): Flow<MutableList<FavouriteMovieBlock>>

    @Query("SELECT EXISTS(SELECT 1 FROM favourite_tmdb_movies WHERE favourite_movie_id LIKE :id LIMIT 1)")
    abstract suspend fun isExistingByMovieIdSuspend(id: Int): Boolean

    @Query("DELETE FROM favourite_tmdb_movies WHERE favourite_movie_id LIKE :id")
    abstract suspend fun deleteByMovieIdSuspend(id: Int)
}