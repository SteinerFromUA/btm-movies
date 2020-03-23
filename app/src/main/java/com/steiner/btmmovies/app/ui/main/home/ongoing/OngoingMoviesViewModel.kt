package com.steiner.btmmovies.app.ui.main.home.ongoing

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.dropbox.android.external.store4.StoreResponse
import com.steiner.btmmovies.app.R
import com.steiner.btmmovies.core.Notice
import com.steiner.btmmovies.core.RefreshInfo
import com.steiner.btmmovies.core.ViewModelAssistedFactory
import com.steiner.btmmovies.core.extension.distinctUntilChangedAndFilterEvents
import com.steiner.btmmovies.core.extension.exhaustive
import com.steiner.btmmovies.core.ui.BaseViewModel
import com.steiner.btmmovies.core.usecase.OpResult
import com.steiner.btmmovies.core.util.CoroutineDispatcherProvider
import com.steiner.btmmovies.domain.usecase.movie.ChangeMovieFavStateParameters
import com.steiner.btmmovies.domain.usecase.movie.ChangeMovieFavStateUseCase
import com.steiner.btmmovies.domain.usecase.movie.ObserveOngoingMoviesParameters
import com.steiner.btmmovies.domain.usecase.movie.ObserveOngoingMoviesUseCase
import com.steiner.btmmovies.domain.usecase.movie.RefreshOngoingMoviesParameters
import com.steiner.btmmovies.domain.usecase.movie.RefreshOngoingMoviesUseCase
import com.steiner.btmmovies.model.block.OngoingMovieBlock
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
class OngoingMoviesViewModel private constructor(
    application: Application,
    coroutineDispatchers: CoroutineDispatcherProvider,
    handle: SavedStateHandle,
    private val observeOngoingMovies: ObserveOngoingMoviesUseCase,
    private val refreshOngoingMovies: RefreshOngoingMoviesUseCase,
    private val changeMovieFavState: ChangeMovieFavStateUseCase
) : BaseViewModel(application, coroutineDispatchers, handle) {

    /**  */
    private val _listRefreshInfo = MediatorLiveData<RefreshInfo>()
    val listRefreshInfo: LiveData<RefreshInfo>
        get() = _listRefreshInfo

    /**  */
    private val _dataResponse = MediatorLiveData<StoreResponse<PagedList<OngoingMovieBlock>>>()
    val dataResponse: LiveData<StoreResponse<PagedList<OngoingMovieBlock>>>
        get() = _dataResponse

    init {
        observeOngoingMovies()
    }

    /**  */
    fun refreshList() {
        viewModelScope.launch(coroutineDispatchers.ui) {
            _listRefreshInfo.value = RefreshInfo.InProgress(
                notice = Notice.Snackbar(messageId = R.string.state_refreshing)
            )

            val opResult: OpResult = refreshOngoingMovies(
                parameters = RefreshOngoingMoviesParameters()
            )

            _listRefreshInfo.value = RefreshInfo.Enabled
            val notice = opResult.fold(onSuccess = {
                Timber.v("Ongoing movies refreshed")
                Notice.Snackbar(messageId = R.string.title_refreshed)
            }, onFailure = { throwable ->
                Timber.w(throwable, "Failed to refresh ongoing movies")
                Notice.Snackbar(messageId = R.string.title_not_refreshed)
            })
            _anyNoticeEvent.setNotice(notice)
        }
    }

    /**  */
    fun changeFavouriteState(movieId: Int, newFavState: FavouriteState) {
        viewModelScope.launch(coroutineDispatchers.ui) {
            val opResult: OpResult = changeMovieFavState(
                parameters = ChangeMovieFavStateParameters(
                    movieId = movieId,
                    newFavState = newFavState
                )
            )

            opResult.fold(onSuccess = {
                Timber.v("Movie with ID $movieId favourite status changed to $newFavState")
            }, onFailure = { throwable ->
                Timber.w(
                    throwable,
                    "Failed change favourite status to $newFavState for a movie with ID $movieId"
                )
                _anyNoticeEvent.setNotice(
                    Notice.Snackbar(messageId = R.string.text_internal_error)
                )
            })
        }
    }

    private fun observeOngoingMovies() {
        observeOngoingMovies(
            parameters = ObserveOngoingMoviesParameters()
        ).distinctUntilChangedAndFilterEvents(
            sameTypeAreEquivalent = { _, _ -> false },
            onRefreshing = { _, _ ->
                _listRefreshInfo.value = RefreshInfo.InProgress()
            },
            onErrorNotice = { _, new ->
                _listRefreshInfo.value = RefreshInfo.Enabled
                _anyNoticeEvent.setNotice(
                    Notice.Snackbar(messageId = R.string.error_data_loading_title)
                )
            }
        ).onEach { omcListResponse ->
            _dataResponse.value = omcListResponse
            when (omcListResponse) {
                is StoreResponse.Data -> {
                    _listRefreshInfo.value = RefreshInfo.Enabled
                }
                is StoreResponse.Loading -> {
                    _listRefreshInfo.value = RefreshInfo.Disabled
                }
                is StoreResponse.Error -> {
                    _listRefreshInfo.value = RefreshInfo.Enabled
                }
            }.exhaustive
        }.flowOn(coroutineDispatchers.ui).launchIn(viewModelScope)
    }

    class AssistedFactory @Inject internal constructor(
        private val application: Provider<Application>,
        private val coroutineDispatchers: Provider<CoroutineDispatcherProvider>,
        private val observeOngoingMovies: Provider<ObserveOngoingMoviesUseCase>,
        private val refreshOngoingMovies: Provider<RefreshOngoingMoviesUseCase>,
        private val changeMovieFavState: Provider<ChangeMovieFavStateUseCase>
    ) : ViewModelAssistedFactory<OngoingMoviesViewModel> {

        override fun create(handle: SavedStateHandle): OngoingMoviesViewModel {
            return OngoingMoviesViewModel(
                application.get(),
                coroutineDispatchers.get(),
                handle,
                observeOngoingMovies.get(),
                refreshOngoingMovies.get(),
                changeMovieFavState.get()
            )
        }
    }
}
