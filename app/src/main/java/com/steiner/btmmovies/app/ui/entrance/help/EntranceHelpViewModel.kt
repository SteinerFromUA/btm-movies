package com.steiner.btmmovies.app.ui.entrance.help

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.steiner.btmmovies.core.ViewModelAssistedFactory
import com.steiner.btmmovies.core.ui.BaseViewModel
import com.steiner.btmmovies.core.util.CoroutineDispatcherProvider
import javax.inject.Inject
import javax.inject.Provider

/**
 *
 */
class EntranceHelpViewModel private constructor(
    application: Application,
    coroutineDispatchers: CoroutineDispatcherProvider,
    handle: SavedStateHandle
) : BaseViewModel(application, coroutineDispatchers, handle) {

    class AssistedFactory @Inject internal constructor(
        private val application: Provider<Application>,
        private val coroutineDispatchers: Provider<CoroutineDispatcherProvider>
    ) : ViewModelAssistedFactory<EntranceHelpViewModel> {

        override fun create(handle: SavedStateHandle): EntranceHelpViewModel {
            return EntranceHelpViewModel(
                application.get(),
                coroutineDispatchers.get(),
                handle
            )
        }
    }
}
