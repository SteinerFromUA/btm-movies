package com.steiner.btmmovies.core.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.steiner.btmmovies.core.ViewModelFactory
import com.steiner.btmmovies.core.extension.lazyFast
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactoryCreator: ViewModelFactory.Creator
        protected set

    private val viewModelFactory: ViewModelProvider.Factory by lazyFast {
        viewModelFactoryCreator.create(this, intent.extras ?: Bundle())
    }

    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory {
        checkNotNull(application) {
            "Your activity is not yet attached to the " +
                    "Application instance. You can't request ViewModel before onCreate call."
        }
        return viewModelFactory
    }
}
