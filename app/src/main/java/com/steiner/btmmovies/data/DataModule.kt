package com.steiner.btmmovies.data

import android.content.Context
import com.steiner.btmmovies.app.di.account.AccountScope
import com.steiner.btmmovies.app.di.account.BtmMoviesAccountSubApp
import com.steiner.btmmovies.core.ApplicationContext
import com.steiner.btmmovies.data.dao.FavouriteTmdbMovieDao
import com.steiner.btmmovies.data.dao.LastRequestDao
import com.steiner.btmmovies.data.dao.OngoingTmdbMovieDao
import com.steiner.btmmovies.data.dao.TmdbMovieDao
import com.steiner.btmmovies.data.db.AccountRoomDatabase
import dagger.Module
import dagger.Provides

/**
 *
 */
@Module(
    includes = [
        //
        DataModuleBinds::class,
        DataModuleProvides::class
    ],
    subcomponents = []
)
class DataModule

/**
 *
 */
@Module(includes = [], subcomponents = [])
internal abstract class DataModuleBinds

/**
 *
 */
@Module(includes = [], subcomponents = [])
internal class DataModuleProvides {

    @AccountScope
    @Provides
    internal fun provideAccountRoomDatabase(
        @ApplicationContext appContext: Context,
        accountSubApp: BtmMoviesAccountSubApp
    ): AccountRoomDatabase {
        return AccountRoomDatabase.instance(appContext, accountSubApp.accountId, false)
    }

    @AccountScope
    @Provides
    internal fun provideLastRequestDao(db: AccountRoomDatabase): LastRequestDao {
        return db.lastRequestDao()
    }

    @AccountScope
    @Provides
    internal fun provideTmdbMovieDao(db: AccountRoomDatabase): TmdbMovieDao {
        return db.tmdbMovieDaoDao()
    }

    @AccountScope
    @Provides
    internal fun provideFavouriteTmdbMovieDao(db: AccountRoomDatabase): FavouriteTmdbMovieDao {
        return db.favouriteTmdbMovieDaoDao()
    }

    @AccountScope
    @Provides
    internal fun provideOngoingTmdbMovieDao(db: AccountRoomDatabase): OngoingTmdbMovieDao {
        return db.ongoingTmdbMovieDaoDao()
    }
}
