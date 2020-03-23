package com.steiner.btmmovies.core.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Executes business logic in its execute method and keep posting updates to the result as
 * [R].
 * Handling an exception is the subclasses's responsibility.
 */
abstract class FlowUseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {

    @ExperimentalCoroutinesApi
    operator fun invoke(parameters: P): Flow<R> {
        return execute(parameters)
            .flowOn(coroutineDispatcher)
    }

    abstract fun execute(parameters: P): Flow<R>
}

/**
 *
 */
operator fun <R> FlowUseCase<Unit, R>.invoke(): Flow<R> = this(Unit)
