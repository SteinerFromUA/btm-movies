package com.steiner.btmmovies.app.ui.main

import androidx.lifecycle.ViewModel
import com.steiner.btmmovies.app.ui.main.home.HomeViewModel
import com.steiner.btmmovies.app.ui.main.home.favourites.FavouriteMoviesViewModel
import com.steiner.btmmovies.app.ui.main.home.ongoing.OngoingMoviesViewModel
import com.steiner.btmmovies.core.ViewModelAssistedFactory
import com.steiner.btmmovies.core.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 *
 */
@Module(includes = [], subcomponents = [])
abstract class MainHiltModule {

    /**
     *
     */
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    internal abstract fun bindHomeViewModelAssistedFactory(
        impl: HomeViewModel.AssistedFactory
    ): ViewModelAssistedFactory<out ViewModel>

    /**
     *
     */
    @Binds
    @IntoMap
    @ViewModelKey(OngoingMoviesViewModel::class)
    internal abstract fun bindOngoingFilmsViewModelAssistedFactory(
        impl: OngoingMoviesViewModel.AssistedFactory
    ): ViewModelAssistedFactory<out ViewModel>

    /**
     *
     */
    @Binds
    @IntoMap
    @ViewModelKey(FavouriteMoviesViewModel::class)
    internal abstract fun bindFavouriteFilmsViewModelAssistedFactory(
        impl: FavouriteMoviesViewModel.AssistedFactory
    ): ViewModelAssistedFactory<out ViewModel>
}
