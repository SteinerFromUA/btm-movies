package com.steiner.btmmovies.app.di.account

import com.uwetrottmann.tmdb2.Tmdb
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Named

/**
 *
 */
@Module(includes = [], subcomponents = [])
class TmdbModule {

    @AccountScope
    @Provides
    fun provideTmdb(
        @Named("tmdb-api-key") apiKey: String,
        interceptor: HttpLoggingInterceptor
    ): Tmdb = object : Tmdb(apiKey) {
        override fun setOkHttpClientDefaults(builder: OkHttpClient.Builder) {
            super.setOkHttpClientDefaults(builder)
            builder.apply {
                addInterceptor(interceptor)
            }
        }
    }
}