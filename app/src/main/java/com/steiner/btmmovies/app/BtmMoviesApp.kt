package com.steiner.btmmovies.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.steiner.btmmovies.app.di.AppComponent
import com.steiner.btmmovies.app.di.DaggerAppComponent
import timber.log.Timber
import timber.log.Timber.DebugTree

/**
 *
 */
class BtmMoviesApp : Application() {

    lateinit var appComponent: AppComponent
        private set

    init {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun onCreate() {
        super.onCreate()

        sInstance = this

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        appComponent = DaggerAppComponent.factory().create(this)
    }

    companion object {
        private lateinit var sInstance: BtmMoviesApp

        @JvmStatic
        fun get(): BtmMoviesApp = sInstance
    }
}
