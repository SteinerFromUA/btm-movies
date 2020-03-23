package com.steiner.btmmovies.domain.usecase.movie

import com.dropbox.android.external.store4.ResponseOrigin
import com.dropbox.android.external.store4.StoreResponse
import com.steiner.btmmovies.app.di.account.AccountScope
import com.steiner.btmmovies.core.usecase.FlowUseCase
import com.steiner.btmmovies.core.util.CoroutineDispatcherProvider
import com.steiner.btmmovies.data.dao.FavouriteTmdbMovieDao
import com.steiner.btmmovies.model.block.FavouriteMovieBlock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 *
 */
@AccountScope
class ObserveFavouriteMoviesUseCase @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatcherProvider,
    private val favouriteTmdbMovieDao: FavouriteTmdbMovieDao
) : FlowUseCase<ObserveFavouriteMoviesParameters, StoreResponse<List<FavouriteMovieBlock>>>(
    coroutineDispatchers.computation
) {

    override fun execute(
        parameters: ObserveFavouriteMoviesParameters
    ): Flow<StoreResponse<List<FavouriteMovieBlock>>> {
        return favouriteTmdbMovieDao.takeAllBlocksAsFlow().map { fmcList ->
            @Suppress("USELESS_CAST")
            StoreResponse.Data(value = fmcList, origin = ResponseOrigin.Persister)
                    as StoreResponse<List<FavouriteMovieBlock>>
        }.flowOn(coroutineDispatchers.database).distinctUntilChanged().onStart {
            emit(StoreResponse.Loading(origin = ResponseOrigin.Cache))
        }.catch { throwable ->
            emit(StoreResponse.Error(origin = ResponseOrigin.Cache, error = throwable))
        }
    }
}

/**
 *
 */
class ObserveFavouriteMoviesParameters
