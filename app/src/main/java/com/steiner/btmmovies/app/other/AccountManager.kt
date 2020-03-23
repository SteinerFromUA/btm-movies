package com.steiner.btmmovies.app.other

import com.facebook.AccessToken
import com.facebook.Profile
import com.facebook.login.LoginManager
import com.steiner.btmmovies.app.di.account.AccountSubComponent
import com.steiner.btmmovies.app.di.account.BtmMoviesAccountSubApp
import javax.inject.Inject
import javax.inject.Singleton

/**
 *
 */
@Singleton
class AccountManager @Inject constructor(
    private val accountComponentFactory: AccountSubComponent.Factory
) {

    var accountComponent: AccountSubComponent? = null
        private set

    fun isRegistered() = Profile.getCurrentProfile() != null &&
            AccessToken.getCurrentAccessToken() != null

    fun login() {
        accountComponent = accountComponentFactory.create(
            BtmMoviesAccountSubApp(
                accountId = Profile.getCurrentProfile()?.id
                    ?: throw IllegalAccessException("Profile not exist")
            )
        )
    }

    fun isLoggedIn() = accountComponent != null

    fun logOut() {
        LoginManager.getInstance().logOut()
        accountComponent = null
    }
}
