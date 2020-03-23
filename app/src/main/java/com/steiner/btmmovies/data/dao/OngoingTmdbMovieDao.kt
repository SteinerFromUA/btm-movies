package com.steiner.btmmovies.data.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.steiner.btmmovies.core.data.BaseDao
import com.steiner.btmmovies.model.block.OngoingMovieBlock
import com.steiner.btmmovies.model.entity.OngoingTmdbMovie

/**
 *
 */
@Dao
abstract class OngoingTmdbMovieDao : BaseDao<OngoingTmdbMovie>() {

    @Query("SELECT * FROM ongoing_tmdb_movies WHERE ongoing_movie_id LIKE :id LIMIT 1")
    abstract suspend fun takeByIdSuspend(id: Int): OngoingTmdbMovie?

    @Query("DELETE FROM ongoing_tmdb_movies")
    abstract suspend fun clearAllSuspend()

    @Query(
        value = """
            SELECT tmdb_movies.tmdb_movie_id, 
                   tmdb_movies.title, 
                   tmdb_movies.description,
                   tmdb_movies.poster_url,
                   ongoing_tmdb_movies.page AS ongoing_page,
                   CASE (SELECT EXISTS(SELECT 1 FROM favourite_tmdb_movies WHERE favourite_tmdb_movies.favourite_movie_id LIKE ongoing_tmdb_movies.ongoing_movie_id LIMIT 1))
                       WHEN 0
                           THEN 'unliked'
                       WHEN 1
                           THEN 'liked'
                       ELSE 'unsupported'
                    END           favourite_state
            FROM ongoing_tmdb_movies 
                INNER JOIN tmdb_movies
                        ON ongoing_tmdb_movies.ongoing_movie_id = tmdb_movies.tmdb_movie_id
            ORDER BY ongoing_tmdb_movies.page, ongoing_tmdb_movies.page_position
    """
    )
    abstract fun takeAllBlocksAsDsf(): DataSource.Factory<Int, OngoingMovieBlock>
}
