package com.steiner.btmmovies.data.db

import android.content.Context
import android.os.Debug
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.steiner.btmmovies.core.ApplicationContext
import com.steiner.btmmovies.data.dao.FavouriteTmdbMovieDao
import com.steiner.btmmovies.data.dao.LastRequestDao
import com.steiner.btmmovies.data.dao.OngoingTmdbMovieDao
import com.steiner.btmmovies.data.dao.TmdbMovieDao
import com.steiner.btmmovies.model.entity.FavouriteTmdbMovie
import com.steiner.btmmovies.model.entity.LastRequest
import com.steiner.btmmovies.model.entity.OngoingTmdbMovie
import com.steiner.btmmovies.model.entity.TmdbMovie
import timber.log.Timber

/**
 *
 */
@Database(
    entities = [
        LastRequest::class,
        //
        TmdbMovie::class,
        OngoingTmdbMovie::class,
        FavouriteTmdbMovie::class
    ],
    views = [],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    value = [AccountDbConverters::class]
)
abstract class AccountRoomDatabase : RoomDatabase() {

    /**
     *
     */
    abstract fun lastRequestDao(): LastRequestDao

    /**
     *
     */
    abstract fun tmdbMovieDaoDao(): TmdbMovieDao

    /**
     *
     */
    abstract fun favouriteTmdbMovieDaoDao(): FavouriteTmdbMovieDao

    /**
     *
     */
    abstract fun ongoingTmdbMovieDaoDao(): OngoingTmdbMovieDao

    companion object {
        private const val DB_NAME_FORMAT = "db_account_persisted_%s.db"

        /**
         *
         */
        @JvmStatic
        fun takeDbName(accountId: String): String = String.format(DB_NAME_FORMAT, accountId)

        /**
         *
         */
        @JvmStatic
        fun instance(
            @ApplicationContext context: Context,
            accountId: String,
            memoryOnly: Boolean = true
        ): AccountRoomDatabase {
            Timber.v("Create an instance of AccountRoomDatabase for accountId: $accountId")
            val dbBuilder: Builder<AccountRoomDatabase> = if (memoryOnly) {
                Room.inMemoryDatabaseBuilder(
                    context.applicationContext,
                    AccountRoomDatabase::class.java
                )
            } else {
                Room.databaseBuilder(
                    context.applicationContext,
                    AccountRoomDatabase::class.java,
                    String.format(DB_NAME_FORMAT, accountId)
                )
            }
            if (Debug.isDebuggerConnected()) {
                dbBuilder.allowMainThreadQueries()
            }
            dbBuilder
                .addMigrations()
                .fallbackToDestructiveMigration()
                .addCallback(generateDbCallback(context, accountId, memoryOnly))
            return dbBuilder.build()
        }

        @JvmStatic
        private fun generateDbCallback(
            @ApplicationContext context: Context,
            accountId: String,
            memoryOnly: Boolean = false
        ): Callback {
            return object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    Timber.v("AccountRoomDatabase DB for accountId: $accountId created. Execute onCreate()")
                    db.beginTransaction()
                    try {
                        db.setTransactionSuccessful()
                    } finally {
                        db.endTransaction()
                    }
                }

                override fun onOpen(db: SupportSQLiteDatabase) {
                    Timber.v("AccountRoomDatabase DB for accountId: $accountId opened. Execute onOpen()")
                    db.beginTransaction()
                    try {
                        db.setTransactionSuccessful()
                    } finally {
                        db.endTransaction()
                    }
                }

                override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                    Timber.v("AccountRoomDatabase DB for accountId: $accountId destructive migrated. Execute onDestructiveMigration()")
                    db.beginTransaction()
                    try {
                        db.setTransactionSuccessful()
                    } finally {
                        db.endTransaction()
                    }
                }
            }
        }
    }
}
