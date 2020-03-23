package com.steiner.btmmovies.app.di.account

import com.steiner.btmmovies.app.ui.main.MainActivity
import com.steiner.btmmovies.app.ui.main.MainHiltModule
import com.steiner.btmmovies.app.ui.main.home.HomeFragment
import com.steiner.btmmovies.app.ui.main.home.favourites.FavouriteMoviesFragment
import com.steiner.btmmovies.app.ui.main.home.ongoing.OngoingMoviesFragment
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Scope

/**
 *
 */
@MustBeDocumented
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AccountScope

/**
 *
 */
@AccountScope
@Subcomponent(
    modules = [
        AccountModule::class,
        //
        MainHiltModule::class
    ]
)
interface AccountSubComponent {

    @Subcomponent.Factory
    abstract class Factory {

        abstract fun create(@BindsInstance instance: BtmMoviesAccountSubApp): AccountSubComponent
    }

    fun inject(activity: MainActivity)

    fun inject(fragment: HomeFragment)

    fun inject(fragment: OngoingMoviesFragment)

    fun inject(fragment: FavouriteMoviesFragment)
}

data class BtmMoviesAccountSubApp(
    val accountId: String
)