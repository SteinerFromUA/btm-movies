package com.steiner.btmmovies.fetcher.calback

import androidx.room.withTransaction
import com.steiner.btmmovies.core.extension.bodyOrThrow
import com.steiner.btmmovies.core.extension.executeWithRetry
import com.steiner.btmmovies.core.paging.SuspendedBoundaryCallback
import com.steiner.btmmovies.core.util.CoroutineDispatcherProvider
import com.steiner.btmmovies.data.dao.OngoingTmdbMovieDao
import com.steiner.btmmovies.data.dao.TmdbMovieDao
import com.steiner.btmmovies.data.db.AccountRoomDatabase
import com.steiner.btmmovies.fetcher.mapper.OngoingResultToOngoingInsertion
import com.steiner.btmmovies.fetcher.model.OngoingMoviesKey
import com.steiner.btmmovies.model.block.OngoingMovieBlock
import com.uwetrottmann.tmdb2.DiscoverMovieBuilder
import com.uwetrottmann.tmdb2.Tmdb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 *
 */
class OngoingMoviesBoundaryCallback(
    scope: CoroutineScope,
    private val coroutineDispatchers: CoroutineDispatcherProvider,
    private val accountDatabase: AccountRoomDatabase,
    private val tmdbMovieDao: TmdbMovieDao,
    private val ongoingTmdbMovieDao: OngoingTmdbMovieDao,
    private val tmdbClient: Tmdb,
    private val insertionMapper: OngoingResultToOngoingInsertion,
    private val key: OngoingMoviesKey
) : SuspendedBoundaryCallback<OngoingMovieBlock>(scope, coroutineDispatchers.computation) {

    private var lastRequestedPage = -1

    override fun isOnItemAtEndLoadedRequestNeed(itemAtEnd: OngoingMovieBlock): Boolean {
        return lastRequestedPage == -1 || itemAtEnd.page != (lastRequestedPage - 1)
    }

    override suspend fun doOnItemAtEndLoaded(itemAtEnd: OngoingMovieBlock) {
        val requestPage = itemAtEnd.page + 1
        val networkResponse = withContext(coroutineDispatchers.network) {
            val response = DiscoverMovieBuilder(tmdbClient.discoverService())
                .release_date_gte(key.gteReleaseDate)
                .release_date_lte(key.lteReleaseDate)
                .page(requestPage)
                .build()
                .executeWithRetry()

            tmdbClient.throwOnKnownError(response)
            response.bodyOrThrow()
        }
        val fromFetcher = withContext(coroutineDispatchers.computation) {
            insertionMapper.map(networkResponse)
        }

        accountDatabase.withTransaction {
            fromFetcher.forEach { ongoingPair ->
                tmdbMovieDao.upsert(ongoingPair.first)
                ongoingTmdbMovieDao.insertOrReplaceSuspend(ongoingPair.second)
            }
        }

        lastRequestedPage = requestPage
        Timber.v("Page $requestPage of ongoing movies with key $key loaded")
    }
}
