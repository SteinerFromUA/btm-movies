package com.steiner.btmmovies.app.ui.entrance

import androidx.lifecycle.ViewModel
import com.steiner.btmmovies.app.ui.entrance.help.EntranceHelpViewModel
import com.steiner.btmmovies.app.ui.entrance.signin.SignInViewModel
import com.steiner.btmmovies.core.ViewModelAssistedFactory
import com.steiner.btmmovies.core.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 *
 */
@Module(includes = [], subcomponents = [])
abstract class EntranceHiltModule {

    /**
     *
     */
    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    internal abstract fun bindSignInViewModelAssistedFactory(
        impl: SignInViewModel.AssistedFactory
    ): ViewModelAssistedFactory<out ViewModel>

    /**
     *
     */
    @Binds
    @IntoMap
    @ViewModelKey(EntranceHelpViewModel::class)
    internal abstract fun bindEntranceHelpViewModelAssistedFactory(
        impl: EntranceHelpViewModel.AssistedFactory
    ): ViewModelAssistedFactory<out ViewModel>
}