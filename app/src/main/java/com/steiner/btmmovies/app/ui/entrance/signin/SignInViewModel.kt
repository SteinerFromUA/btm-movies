package com.steiner.btmmovies.app.ui.entrance.signin

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.steiner.btmmovies.app.R
import com.steiner.btmmovies.core.ActionEventLiveData
import com.steiner.btmmovies.core.Event
import com.steiner.btmmovies.core.Notice
import com.steiner.btmmovies.core.ViewModelAssistedFactory
import com.steiner.btmmovies.core.ui.BaseViewModel
import com.steiner.btmmovies.core.util.CoroutineDispatcherProvider
import javax.inject.Inject
import javax.inject.Provider

/**
 *
 */
class SignInViewModel private constructor(
    application: Application,
    coroutineDispatchers: CoroutineDispatcherProvider,
    handle: SavedStateHandle
) : BaseViewModel(application, coroutineDispatchers, handle) {

    /**  */
    private val _signedInEvent = ActionEventLiveData()
    val signedInEvent: LiveData<Event<Unit>>
        get() = _signedInEvent

    val fbCbManager: CallbackManager by lazy {
        CallbackManager.Factory.create()
    }

    private val fbCallback = object : FacebookCallback<LoginResult> {

        override fun onSuccess(result: LoginResult?) {
            if (result?.accessToken != null) {
                _signedInEvent.setAction()
            } else {
                _anyNoticeEvent.setNotice(
                    Notice.Snackbar(messageId = R.string.text_sign_in_error)
                )
            }
        }

        override fun onError(error: FacebookException?) {
            _anyNoticeEvent.setNotice(
                Notice.Snackbar(messageId = R.string.text_sign_in_error)
            )
        }

        override fun onCancel() = Unit
    }

    init {
        LoginManager.getInstance().registerCallback(fbCbManager, fbCallback)
    }

    override fun onCleared() {
        super.onCleared()

        LoginManager.getInstance().unregisterCallback(fbCbManager)
    }

    class AssistedFactory @Inject internal constructor(
        private val application: Provider<Application>,
        private val coroutineDispatchers: Provider<CoroutineDispatcherProvider>
    ) : ViewModelAssistedFactory<SignInViewModel> {

        override fun create(handle: SavedStateHandle): SignInViewModel {
            return SignInViewModel(
                application.get(),
                coroutineDispatchers.get(),
                handle
            )
        }
    }
}
