package com.steiner.btmmovies.app.ui.main.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.steiner.btmmovies.app.R
import com.steiner.btmmovies.core.Notice
import com.steiner.btmmovies.core.ViewModelAssistedFactory
import com.steiner.btmmovies.core.ui.BaseViewModel
import com.steiner.btmmovies.core.util.CoroutineDispatcherProvider
import com.steiner.btmmovies.domain.model.Profile
import com.steiner.btmmovies.domain.usecase.other.GetProfileParameters
import com.steiner.btmmovies.domain.usecase.other.GetProfileUseCase
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider


/**
 *
 */
class HomeViewModel private constructor(
    application: Application,
    coroutineDispatchers: CoroutineDispatcherProvider,
    handle: SavedStateHandle,
    private val getProfile: GetProfileUseCase
) : BaseViewModel(application, coroutineDispatchers, handle) {

    /**  */
    private val _profile = MediatorLiveData<Profile>()
    val profile: LiveData<Profile>
        get() = _profile

    init {
        /**  */
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch(coroutineDispatchers.ui) {
            val profileResult = getProfile(parameters = GetProfileParameters())

            profileResult.fold(onSuccess = { newProfile ->
                _profile.value = newProfile
                Timber.v("Facebook profile loaded: $newProfile")
            }, onFailure = { throwable ->
                Timber.e(throwable, "Cannot load Facebook profile")
                _anyNoticeEvent.setNotice(
                    Notice.Snackbar(messageId = R.string.text_internal_error)
                )
            })
        }
    }

    class AssistedFactory @Inject internal constructor(
        private val application: Provider<Application>,
        private val coroutineDispatchers: Provider<CoroutineDispatcherProvider>,
        private val getProfile: Provider<GetProfileUseCase>
    ) : ViewModelAssistedFactory<HomeViewModel> {

        override fun create(handle: SavedStateHandle): HomeViewModel {
            return HomeViewModel(
                application.get(),
                coroutineDispatchers.get(),
                handle,
                getProfile.get()
            )
        }
    }
}
