package com.steiner.btmmovies.fetcher.store

import androidx.paging.PagedList
import androidx.paging.PagedList.Config
import androidx.room.withTransaction
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.nonFlowValueFetcher
import com.steiner.btmmovies.app.di.account.AccountScope
import com.steiner.btmmovies.core.AccountLifetime
import com.steiner.btmmovies.core.extension.bodyOrThrow
import com.steiner.btmmovies.core.extension.executeWithRetry
import com.steiner.btmmovies.core.paging.toFlow
import com.steiner.btmmovies.core.util.CoroutineDispatcherProvider
import com.steiner.btmmovies.data.dao.LastRequestDao
import com.steiner.btmmovies.data.dao.OngoingTmdbMovieDao
import com.steiner.btmmovies.data.dao.TmdbMovieDao
import com.steiner.btmmovies.data.db.AccountRoomDatabase
import com.steiner.btmmovies.fetcher.calback.OngoingMoviesBoundaryCallback
import com.steiner.btmmovies.fetcher.mapper.OngoingResultToOngoingInsertion
import com.steiner.btmmovies.fetcher.model.OngoingMoviesKey
import com.steiner.btmmovies.model.block.OngoingMovieBlock
import com.steiner.btmmovies.model.entity.LastRequest
import com.steiner.btmmovies.model.entity.OngoingTmdbMovie
import com.steiner.btmmovies.model.entity.TmdbMovie
import com.steiner.btmmovies.model.enumeration.Request
import com.uwetrottmann.tmdb2.DiscoverMovieBuilder
import com.uwetrottmann.tmdb2.Tmdb
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.time.seconds

typealias OngoingMoviesStore = Store<OngoingMoviesKey, PagedList<OngoingMovieBlock>>

/**
 *
 */
@Module(
    includes = [
        UnitsModuleBinds::class,
        UnitsModuleProvides::class
    ],
    subcomponents = []
)
class UnitsModule

/**
 *
 */
@Module(includes = [], subcomponents = [])
internal abstract class UnitsModuleBinds

/**
 *
 */
@Module(includes = [], subcomponents = [])
internal class UnitsModuleProvides {

    @AccountScope
    @Provides
    internal fun provideOngoingMoviesStore(
        coroutineDispatchers: CoroutineDispatcherProvider,
        @AccountLifetime storeScope: CoroutineScope,
        accountDatabase: AccountRoomDatabase,
        lastRequestDao: LastRequestDao,
        tmdbMovieDao: TmdbMovieDao,
        ongoingTmdbMovieDao: OngoingTmdbMovieDao,
        tmdbClient: Tmdb,
        insertionMapper: OngoingResultToOngoingInsertion
    ): OngoingMoviesStore {
        return StoreBuilder.from<OngoingMoviesKey, List<Pair<TmdbMovie, OngoingTmdbMovie>>, PagedList<OngoingMovieBlock>>(
            fetcher = nonFlowValueFetcher { key ->
                // FIXME: Special delay for test
                delay(2.seconds)

                val networkResponse = withContext(coroutineDispatchers.network) {
                    val response = DiscoverMovieBuilder(tmdbClient.discoverService())
                        .release_date_gte(key.gteReleaseDate)
                        .release_date_lte(key.lteReleaseDate)
                        .page(1)
                        .build()
                        .executeWithRetry()

                    tmdbClient.throwOnKnownError(response)
                    response.bodyOrThrow()
                }
                withContext(coroutineDispatchers.computation) {
                    insertionMapper.map(networkResponse)
                }
            },
            sourceOfTruth = SourceOfTruth.from(
                reader = { key ->
                    val callback = OngoingMoviesBoundaryCallback(
                        scope = storeScope,
                        coroutineDispatchers = coroutineDispatchers,
                        accountDatabase = accountDatabase,
                        tmdbMovieDao = tmdbMovieDao,
                        ongoingTmdbMovieDao = ongoingTmdbMovieDao,
                        tmdbClient = tmdbClient,
                        insertionMapper = insertionMapper,
                        key = key
                    )
                    ongoingTmdbMovieDao.takeAllBlocksAsDsf().toFlow(
                        config = DEFAULT_DB_PAGED_LIST_CONFIG_BUILDER.build(),
                        boundaryCallback = callback,
                        fetchExecutor = coroutineDispatchers.database.asExecutor()
                    )
                },
                writer = { key, fromFetcher ->
                    accountDatabase.withTransaction {
                        ongoingTmdbMovieDao.clearAllSuspend()
                        tmdbMovieDao.clearExceptFavouritesSuspend()
                        fromFetcher.forEach { ongoingPair ->
                            tmdbMovieDao.upsert(ongoingPair.first)
                            ongoingTmdbMovieDao.insertOrReplaceSuspend(ongoingPair.second)
                        }
                        lastRequestDao.insertOrReplaceSuspend(
                            LastRequest(Request.ONGOING_MOVIES, System.currentTimeMillis())
                        )
                    }
                    Timber.v("Page 1 of ongoing movies with key $key loaded")
                },
                delete = { key ->
                    throw UnsupportedOperationException("This Store don't support this operation!")
                },
                deleteAll = {
                    accountDatabase.withTransaction {
                        ongoingTmdbMovieDao.clearAllSuspend()
                        tmdbMovieDao.clearExceptFavouritesSuspend()
                    }
                    Timber.v("Ongoing movies cleared")
                }
            )
        ).disableCache().scope(storeScope).build()
    }

    companion object {
        private const val DEFAULT_DB_PAGE_SIZE = 20

        private val DEFAULT_DB_PAGED_LIST_CONFIG_BUILDER: Config.Builder = Config.Builder()
            .setPageSize(DEFAULT_DB_PAGE_SIZE)
            .setPrefetchDistance(DEFAULT_DB_PAGE_SIZE / 3)
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(DEFAULT_DB_PAGE_SIZE)
    }
}
