package com.steiner.btmmovies.app.di.guest

import dagger.Module

/**
 *
 */
@Module(
    includes = [
        GuestModuleBinds::class,
        GuestModuleProvides::class
    ],
    subcomponents = []
)
class GuestModule

/**
 *
 */
@Module(includes = [], subcomponents = [])
internal abstract class GuestModuleBinds

/**
 *
 */
@Module(includes = [], subcomponents = [])
internal class GuestModuleProvides
