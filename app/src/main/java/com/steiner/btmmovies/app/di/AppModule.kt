package com.steiner.btmmovies.app.di

import android.app.Application
import android.content.Context
import com.squareup.moshi.Moshi
import com.steiner.btmmovies.app.BtmMoviesApp
import com.steiner.btmmovies.app.BuildConfig
import com.steiner.btmmovies.core.ApplicationContext
import com.steiner.btmmovies.core.ViewModelInstantiationModule
import com.steiner.btmmovies.core.util.CoroutineDispatcherProvider
import com.steiner.btmmovies.core.util.DefaultCoroutineDispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Named
import javax.inject.Singleton

/**
 *
 */
@Module(
    includes = [
        AppModuleBinds::class,
        AppModuleProvides::class,
        //
        ViewModelInstantiationModule::class
    ],
    subcomponents = []
)
class AppModule

/**
 *
 */
@Module(includes = [], subcomponents = [])
internal abstract class AppModuleBinds {

    /**
     *
     */
    @Binds
    @Singleton
    abstract fun bindCoroutineDispatcherProvider(
        impl: DefaultCoroutineDispatcherProvider
    ): CoroutineDispatcherProvider
}

/**
 *
 */
@Module(includes = [], subcomponents = [])
internal class AppModuleProvides {

    /**
     *
     */
    @Singleton
    @Provides
    fun provideApp(application: BtmMoviesApp): Application = application

    /**
     *
     */
    @Singleton
    @Provides
    @ApplicationContext
    fun provideAppContext(application: BtmMoviesApp): Context = application.applicationContext

    @Singleton
    @Provides
    @Named("tmdb-api-key")
    fun provideTmdbApiKey(): String = BuildConfig.TMDB_API_KEY

    /**
     *
     */
    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    /**
     *
     */
    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }
    }
}
