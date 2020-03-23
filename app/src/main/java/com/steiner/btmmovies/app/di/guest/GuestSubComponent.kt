package com.steiner.btmmovies.app.di.guest

import com.steiner.btmmovies.app.ui.entrance.EntranceActivity
import com.steiner.btmmovies.app.ui.entrance.EntranceHiltModule
import com.steiner.btmmovies.app.ui.entrance.help.EntranceHelpFragment
import com.steiner.btmmovies.app.ui.entrance.signin.SignInFragment
import dagger.Subcomponent
import javax.inject.Scope

/**
 *
 */
@MustBeDocumented
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class GuestScope

/**
 *
 */
@GuestScope
@Subcomponent(
    modules = [
        GuestModule::class,
        //
        EntranceHiltModule::class
    ]
)
interface GuestSubComponent {

    @Subcomponent.Factory
    abstract class Factory {

        abstract fun create(): GuestSubComponent
    }

    fun inject(activity: EntranceActivity)

    fun inject(fragment: SignInFragment)

    fun inject(fragment: EntranceHelpFragment)
}