package com.steiner.btmmovies.domain.usecase.movie

import com.dropbox.android.external.store4.fresh
import com.steiner.btmmovies.app.di.account.AccountScope
import com.steiner.btmmovies.core.usecase.OpResultSuspendUseCase
import com.steiner.btmmovies.core.util.CoroutineDispatcherProvider
import com.steiner.btmmovies.fetcher.model.OngoingMoviesKey
import com.steiner.btmmovies.fetcher.store.OngoingMoviesStore
import com.uwetrottmann.tmdb2.entities.TmdbDate
import kotlinx.coroutines.coroutineScope
import java.util.*
import javax.inject.Inject

/**
 *
 */
@AccountScope
class RefreshOngoingMoviesUseCase @Inject constructor(
    coroutineDispatchers: CoroutineDispatcherProvider,
    private val ongoingMoviesStore: OngoingMoviesStore
) : OpResultSuspendUseCase<RefreshOngoingMoviesParameters>(coroutineDispatchers.computation) {

    override suspend fun execute(parameters: RefreshOngoingMoviesParameters) = coroutineScope {
        val twoWeeksAgoDate = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, AMOUNT_DAYS)
        }.time
        ongoingMoviesStore.fresh(
            key = OngoingMoviesKey(
                lteReleaseDate = TmdbDate(Date()),
                gteReleaseDate = TmdbDate(twoWeeksAgoDate)
            )
        )
        Unit
    }

    companion object {
        private val AMOUNT_DAYS = -14
    }
}

/**
 *
 */
class RefreshOngoingMoviesParameters
