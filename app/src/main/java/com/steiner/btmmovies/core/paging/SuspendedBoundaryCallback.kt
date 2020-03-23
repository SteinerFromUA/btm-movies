package com.steiner.btmmovies.core.paging

import androidx.paging.PagedList.BoundaryCallback
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 *
 */
open class SuspendedBoundaryCallback<T>(
    private val scope: CoroutineScope,
    private val dispatcher: CoroutineDispatcher
) : BoundaryCallback<T>() {

    // Используем Main Dispatcher для retry так как корутина
    // и так запускает свое тело в другом Dispatcher
    protected val helper = PagingRequestHelper(Dispatchers.Main.immediate.asExecutor())

    final override fun onZeroItemsLoaded() {
        if (!isOnZeroItemsLoadedRequestNeed()) return

        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) { callback ->
            scope.launch(dispatcher) {
                try {
                    doOnZeroItemsLoaded()
                    callback.recordSuccess()
                } catch (e: CancellationException) {
                    Timber.v(e, "Received Cancellation for onZeroItemsLoaded()")
                    callback.recordSuccess()
                } catch (e: Exception) {
                    Timber.w(e, "Received for onZeroItemsLoaded()")
                    callback.recordFailure(e)
                }
            }
        }
    }

    final override fun onItemAtFrontLoaded(itemAtFront: T) {
        if (!isOnItemAtFrontLoadedRequestNeed(itemAtFront)) return

        helper.runIfNotRunning(PagingRequestHelper.RequestType.BEFORE) { callback ->
            scope.launch(dispatcher) {
                try {
                    doOnItemAtFrontLoaded(itemAtFront)
                    callback.recordSuccess()
                } catch (e: CancellationException) {
                    Timber.v(e, "Received Cancellation for onItemAtFrontLoaded($itemAtFront)")
                    callback.recordSuccess()
                } catch (e: Exception) {
                    Timber.w(e, "Received for onItemAtFrontLoaded($itemAtFront)")
                    callback.recordFailure(e)
                }
            }
        }
    }

    final override fun onItemAtEndLoaded(itemAtEnd: T) {
        if (!isOnItemAtEndLoadedRequestNeed(itemAtEnd)) return

        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) { callback ->
            scope.launch(dispatcher) {
                try {
                    doOnItemAtEndLoaded(itemAtEnd)
                    callback.recordSuccess()
                } catch (e: CancellationException) {
                    Timber.v(e, "Received Cancellation for onItemAtEndLoaded($itemAtEnd)")
                    callback.recordSuccess()
                } catch (e: Exception) {
                    Timber.w(e, "Received for onItemAtEndLoaded($itemAtEnd)")
                    callback.recordFailure(e)
                }
            }
        }
    }

    open fun isOnZeroItemsLoadedRequestNeed(): Boolean = false

    open suspend fun doOnZeroItemsLoaded() {}

    open fun isOnItemAtFrontLoadedRequestNeed(itemAtFront: T): Boolean = false

    open suspend fun doOnItemAtFrontLoaded(itemAtFront: T) {}

    open fun isOnItemAtEndLoadedRequestNeed(itemAtEnd: T): Boolean = false

    open suspend fun doOnItemAtEndLoaded(itemAtEnd: T) {}
}
