package com.steiner.btmmovies.domain.usecase.movie

import androidx.paging.PagedList
import com.dropbox.android.external.store4.ResponseOrigin
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.steiner.btmmovies.app.di.account.AccountScope
import com.steiner.btmmovies.core.usecase.FlowUseCase
import com.steiner.btmmovies.core.util.CoroutineDispatcherProvider
import com.steiner.btmmovies.data.dao.LastRequestDao
import com.steiner.btmmovies.fetcher.model.OngoingMoviesKey
import com.steiner.btmmovies.fetcher.store.OngoingMoviesStore
import com.steiner.btmmovies.model.block.OngoingMovieBlock
import com.steiner.btmmovies.model.enumeration.Request
import com.uwetrottmann.tmdb2.entities.TmdbDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

/**
 *
 */
@AccountScope
class ObserveOngoingMoviesUseCase @Inject constructor(
    coroutineDispatchers: CoroutineDispatcherProvider,
    private val lastRequestDao: LastRequestDao,
    private val ongoingMoviesStore: OngoingMoviesStore
) : FlowUseCase<ObserveOngoingMoviesParameters, StoreResponse<PagedList<OngoingMovieBlock>>>(
    coroutineDispatchers.computation
) {

    override fun execute(
        parameters: ObserveOngoingMoviesParameters
    ): Flow<StoreResponse<PagedList<OngoingMovieBlock>>> {
        @Suppress("RemoveExplicitTypeArguments")
        return flow<Long> {
            emit(lastRequestDao.takeByRequest(Request.ONGOING_MOVIES)?.timestamp ?: 0L)
        }.catch { emit(0L) }.flatMapLatest { lastRequestTs ->
            val shouldSkipFirstFromLocal = AtomicBoolean(lastRequestTs <= 0L)
            val twoWeeksAgoDate = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_MONTH, -14)
            }.time
            val storeKey = OngoingMoviesKey(
                lteReleaseDate = TmdbDate(Date()),
                gteReleaseDate = TmdbDate(twoWeeksAgoDate)
            )
            ongoingMoviesStore.stream(
                request = if (lastRequestTs > 0L) {
                    StoreRequest.skipMemory(key = storeKey, refresh = true)
                } else {
                    // StoreRequest.fresh(key = Unit)
                    StoreRequest.skipMemory(key = storeKey, refresh = true)
                }
            ).transform { omsResponse ->
                if (omsResponse.origin != ResponseOrigin.SourceOfTruth ||
                    omsResponse !is StoreResponse.Data ||
                    !shouldSkipFirstFromLocal.compareAndSet(true, false)
                ) emit(omsResponse)
            }
        }.onStart {
            emit(StoreResponse.Loading(origin = ResponseOrigin.Cache))
        }.catch { throwable ->
            emit(StoreResponse.Error.Exception(origin = ResponseOrigin.Cache, error = throwable))
        }
    }
}

/**
 *
 */
class ObserveOngoingMoviesParameters
