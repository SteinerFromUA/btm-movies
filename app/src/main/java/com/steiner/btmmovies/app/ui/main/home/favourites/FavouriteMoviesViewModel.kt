package com.steiner.btmmovies.app.ui.main.home.favourites

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dropbox.android.external.store4.StoreResponse
import com.steiner.btmmovies.app.R
import com.steiner.btmmovies.core.Notice
import com.steiner.btmmovies.core.ViewModelAssistedFactory
import com.steiner.btmmovies.core.ui.BaseViewModel
import com.steiner.btmmovies.core.usecase.OpResult
import com.steiner.btmmovies.core.util.CoroutineDispatcherProvider
import com.steiner.btmmovies.domain.usecase.movie.ChangeMovieFavStateParameters
import com.steiner.btmmovies.domain.usecase.movie.ChangeMovieFavStateUseCase
import com.steiner.btmmovies.domain.usecase.movie.ObserveFavouriteMoviesParameters
import com.steiner.btmmovies.domain.usecase.movie.ObserveFavouriteMoviesUseCase
import com.steiner.btmmovies.model.block.FavouriteMovieBlock
import com.steiner.btmmovies.model.enumeration.FavouriteState
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

/**
 *
 */
class FavouriteMoviesViewModel private constructor(
    application: Application,
    coroutineDispatchers: CoroutineDispatcherProvider,
    handle: SavedStateHandle,
    private val observeFavouriteMovies: ObserveFavouriteMoviesUseCase,
    private val changeMovieFavState: ChangeMovieFavStateUseCase
) : BaseViewModel(application, coroutineDispatchers, handle) {

    /**  */
    private val _dataResponse = MediatorLiveData<StoreResponse<List<FavouriteMovieBlock>>>()
    val dataResponse: LiveData<StoreResponse<List<FavouriteMovieBlock>>>
        get() = _dataResponse

    init {
        observeFavouriteMovies()
    }

    /**  */
    fun removeFromFavourites(movieId: Int) {
        viewModelScope.launch(coroutineDispatchers.ui) {
            val opResult: OpResult = changeMovieFavState(
                parameters = ChangeMovieFavStateParameters(
                    movieId = movieId,
                    newFavState = FavouriteState.UNLIKED
                )
            )

            opResult.fold(onSuccess = {
                Timber.v("Movie with ID $movieId removed from favourites")
            }, onFailure = { throwable ->
                Timber.w(
                    throwable,
                    "Failed a remove from favourites movie with ID $movieId"
                )
                _anyNoticeEvent.setNotice(
                    Notice.Snackbar(messageId = R.string.text_internal_error)
                )
            })
        }
    }

    private fun observeFavouriteMovies() {
        observeFavouriteMovies(
            parameters = ObserveFavouriteMoviesParameters()
        ).onEach { fmcListResponse ->
            _dataResponse.value = fmcListResponse
        }.flowOn(coroutineDispatchers.ui).launchIn(viewModelScope)
    }

    class AssistedFactory @Inject internal constructor(
        private val application: Provider<Application>,
        private val coroutineDispatchers: Provider<CoroutineDispatcherProvider>,
        private val observeFavouriteMovies: Provider<ObserveFavouriteMoviesUseCase>,
        private val changeMovieFavState: Provider<ChangeMovieFavStateUseCase>
    ) : ViewModelAssistedFactory<FavouriteMoviesViewModel> {

        override fun create(handle: SavedStateHandle): FavouriteMoviesViewModel {
            return FavouriteMoviesViewModel(
                application.get(),
                coroutineDispatchers.get(),
                handle,
                observeFavouriteMovies.get(),
                changeMovieFavState.get()
            )
        }
    }
}
