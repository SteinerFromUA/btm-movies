package com.steiner.btmmovies.core.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * Executes business logic synchronously or asynchronously using a [CoroutineDispatcher].
 */
abstract class ResultSuspendUseCase<in P, out R>(
    private val coroutineDispatcher: CoroutineDispatcher
) {

    /** Executes the use case asynchronously and returns a [Result].
     *
     * @return a [Result].
     *
     * @param parameters the input parameters to run the use case with
     */
    suspend operator fun invoke(
        parameters: P
    ): Result<R> = coroutineScope {
        runCatching { withContext(coroutineDispatcher) { execute(parameters) } }
    }

    /** Executes the use case synchronously  */
    fun invokeNow(parameters: P): Result<R> = runCatching {
        runBlocking(coroutineDispatcher) { execute(parameters) }
    }

    /**
     * Override this to set the code to be executed.
     */
    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(parameters: P): R
}

/**
 *
 */
suspend operator fun <R> ResultSuspendUseCase<Unit, R>.invoke(): Result<R> = this(Unit)

/**
 *
 */
typealias OpResultSuspendUseCase<P> = ResultSuspendUseCase<P, Unit>

/**
 *
 */
typealias OpResult = Result<Unit>
