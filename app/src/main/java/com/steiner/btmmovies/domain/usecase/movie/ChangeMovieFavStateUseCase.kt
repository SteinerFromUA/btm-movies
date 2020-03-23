package com.steiner.btmmovies.domain.usecase.movie

import androidx.room.withTransaction
import com.steiner.btmmovies.app.di.account.AccountScope
import com.steiner.btmmovies.core.extension.exhaustive
import com.steiner.btmmovies.core.usecase.OpResultSuspendUseCase
import com.steiner.btmmovies.core.util.CoroutineDispatcherProvider
import com.steiner.btmmovies.data.dao.FavouriteTmdbMovieDao
import com.steiner.btmmovies.data.dao.TmdbMovieDao
import com.steiner.btmmovies.data.db.AccountRoomDatabase
import com.steiner.btmmovies.model.entity.FavouriteTmdbMovie
import com.steiner.btmmovies.model.enumeration.FavouriteState
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 *
 */
@AccountScope
class ChangeMovieFavStateUseCase @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatcherProvider,
    private val accountDatabase: AccountRoomDatabase,
    private val tmdbMovieDao: TmdbMovieDao,
    private val favouriteTmdbMovieDao: FavouriteTmdbMovieDao
) : OpResultSuspendUseCase<ChangeMovieFavStateParameters>(coroutineDispatchers.computation) {

    override suspend fun execute(parameters: ChangeMovieFavStateParameters) = coroutineScope {
        when (parameters.newFavState) {
            FavouriteState.LIKED -> saveToFavourites(parameters.movieId)
            FavouriteState.UNLIKED -> removeFromFavourites(parameters.movieId)
            FavouriteState.UNSUPPORTED -> throw IllegalArgumentException("Impossible fav state.")
        }.exhaustive
    }

    private suspend fun saveToFavourites(movieId: Int) {
        accountDatabase.withTransaction {
            if (!tmdbMovieDao.isExistingByIdSuspend(movieId)) {
                throw IllegalArgumentException("Incorrect movie ID: $movieId")
            }
            if (!favouriteTmdbMovieDao.isExistingByMovieIdSuspend(movieId)) {
                val favouriteTmdbMovie = FavouriteTmdbMovie(
                    movieId = movieId,
                    likedAt = System.currentTimeMillis()
                )
                favouriteTmdbMovieDao.insertOrReplaceSuspend(favouriteTmdbMovie)
            }
        }
    }

    private suspend fun removeFromFavourites(movieId: Int) {
        accountDatabase.withTransaction {
            favouriteTmdbMovieDao.deleteByMovieIdSuspend(movieId)
            tmdbMovieDao.deleteIfNotInOngoingSuspend(movieId)
        }
    }
}

/**
 *
 */
data class ChangeMovieFavStateParameters(
    val movieId: Int,
    val newFavState: FavouriteState
)
