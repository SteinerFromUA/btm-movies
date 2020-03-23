package com.steiner.btmmovies.fetcher

import com.steiner.btmmovies.data.DataModule
import com.steiner.btmmovies.fetcher.store.UnitsModule
import dagger.Module

/**
 *
 */
@Module(
    includes = [
        //
        DataModule::class,
        UnitsModule::class,
        //
        FetcherModuleBinds::class,
        FetcherModuleProvides::class
    ],
    subcomponents = []
)
class FetcherModule

/**
 *
 */
@Module(includes = [], subcomponents = [])
internal abstract class FetcherModuleBinds

/**
 *
 */
@Module(includes = [], subcomponents = [])
internal class FetcherModuleProvides
