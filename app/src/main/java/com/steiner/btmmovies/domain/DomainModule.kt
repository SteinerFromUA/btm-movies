package com.steiner.btmmovies.domain

import com.steiner.btmmovies.fetcher.FetcherModule
import dagger.Module

/**
 *
 */
@Module(
    includes = [
        //
        FetcherModule::class,
        //
        DomainModuleBinds::class,
        DomainModuleProvides::class
    ],
    subcomponents = []
)
class DomainModule

/**
 *
 */
@Module(includes = [], subcomponents = [])
internal abstract class DomainModuleBinds

/**
 *
 */
@Module(includes = [], subcomponents = [])
internal class DomainModuleProvides
