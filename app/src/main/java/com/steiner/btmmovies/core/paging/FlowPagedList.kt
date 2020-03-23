package com.steiner.btmmovies.core.paging

import android.annotation.SuppressLint
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.paging.Config
import androidx.paging.DataSource
import androidx.paging.PagedList
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.Executor

/* Adapted from LiveDataPagedList */

/**
 * Constructs a `FlowData<PagedList>`, from this `DataSource.Factory`, convenience for
 * [FlowPagedListBuilder].
 *
 * No work (such as loading) is done immediately, the creation of the first PagedList is is
 * deferred until the Flow is collected.
 *
 * @param config Paging configuration.
 * @param initialLoadKey Initial load key passed to the first PagedList/DataSource.
 * @param boundaryCallback The boundary callback for listening to PagedList load state.
 * @param fetchDispatcher CoroutineDispatcher for fetching data from DataSources.
 *
 * @see FlowPagedListBuilder
 */
@SuppressLint("RestrictedApi")
fun <Key, Value> DataSource.Factory<Key, Value>.toFlow(
    config: PagedList.Config,
    initialLoadKey: Key? = null,
    boundaryCallback: PagedList.BoundaryCallback<Value>? = null,
    fetchExecutor: Executor = ArchTaskExecutor.getIOThreadExecutor()
): Flow<PagedList<Value>> {
    return FlowPagedListBuilder(
        dataSourceFactory = this,
        config = config,
        initialLoadKey = initialLoadKey,
        boundaryCallback = boundaryCallback,
        fetchExecutor = fetchExecutor
    ).buildFlow()
}

/**
 * Constructs a `Flow<PagedList>`, from this `DataSource.Factory`, convenience for
 * [FlowPagedListBuilder].
 *
 * No work (such as loading) is done immediately, the creation of the first PagedList is is
 * deferred until the Flow is collected.
 *
 * @param pageSize Page size.
 * @param initialLoadKey Initial load key passed to the first PagedList/DataSource.
 * @param boundaryCallback The boundary callback for listening to PagedList load state.
 * @param fetchDispatcher CoroutineDispatcher for fetching data from DataSources.
 *
 * @see FlowPagedListBuilder
 */
@SuppressLint("RestrictedApi")
fun <Key, Value> DataSource.Factory<Key, Value>.toFlow(
    pageSize: Int,
    initialLoadKey: Key? = null,
    boundaryCallback: PagedList.BoundaryCallback<Value>? = null,
    fetchExecutor: Executor = ArchTaskExecutor.getIOThreadExecutor()
): Flow<PagedList<Value>> {
    return FlowPagedListBuilder(
        dataSourceFactory = this,
        config = Config(pageSize),
        initialLoadKey = initialLoadKey,
        boundaryCallback = boundaryCallback,
        fetchExecutor = fetchExecutor
    ).buildFlow()
}
