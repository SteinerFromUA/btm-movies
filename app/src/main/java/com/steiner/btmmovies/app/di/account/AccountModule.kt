package com.steiner.btmmovies.app.di.account

import com.steiner.btmmovies.core.AccountLifetime
import com.steiner.btmmovies.domain.DomainModule
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 *
 */
@Module(
    includes = [
        //
        TmdbModule::class,
        DomainModule::class,
        //
        AccountModuleBinds::class,
        AccountModuleProvides::class
    ],
    subcomponents = []
)
class AccountModule

/**
 *
 */
@Module(includes = [], subcomponents = [])
internal abstract class AccountModuleBinds

/**
 *
 */
@Module(includes = [], subcomponents = [])
internal class AccountModuleProvides {

    @Provides
    @AccountScope
    @AccountLifetime
    internal fun provideAccountCoroutineScope(
        accountSubApp: BtmMoviesAccountSubApp
    ): CoroutineScope {
        return CoroutineScope(
            SupervisorJob() +
                    CoroutineName("coroutine-for-account-${accountSubApp.accountId}") +
                    Dispatchers.Main.immediate
        )
    }
}
