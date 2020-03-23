package com.steiner.btmmovies.app.di

import com.steiner.btmmovies.app.BtmMoviesApp
import com.steiner.btmmovies.app.di.account.AccountSubComponent
import com.steiner.btmmovies.app.di.guest.GuestSubComponent
import com.steiner.btmmovies.app.other.AccountManager
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

/**
 *
 */
@Singleton
@Component(
    modules = [AppSubComponentsModule::class, AppModule::class],
    dependencies = []
)
interface AppComponent {

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance instance: BtmMoviesApp): AppComponent
    }

    fun guestSubComponentFactory(): GuestSubComponent.Factory

    fun accountManager(): AccountManager
}

/**
 *
 */
@Module(includes = [], subcomponents = [GuestSubComponent::class, AccountSubComponent::class])
internal abstract class AppSubComponentsModule
