package com.steiner.btmmovies.core.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 *
 */
interface CoroutineDispatcherProvider {

    /**
     *
     */
    val computation: CoroutineDispatcher

    /**
     *
     */
    val network: CoroutineDispatcher

    /**
     *
     */
    val database: CoroutineDispatcher

    /**
     *
     */
    val disk: CoroutineDispatcher

    /**
     *
     */
    val ui: CoroutineDispatcher
}

/**
 *
 */
@Singleton
class DefaultCoroutineDispatcherProvider @Inject constructor(
) :  CoroutineDispatcherProvider {

    override val computation: CoroutineDispatcher = newSingleThreadContext("Computation")

    override val network: CoroutineDispatcher = Dispatchers.IO

    override val database: CoroutineDispatcher = newSingleThreadContext("Database")

    override val disk: CoroutineDispatcher = newSingleThreadContext("Disk")

    override val ui: CoroutineDispatcher = Dispatchers.Main.immediate
}