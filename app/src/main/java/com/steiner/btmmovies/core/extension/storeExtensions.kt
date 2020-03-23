package com.steiner.btmmovies.core.extension

import com.dropbox.android.external.store4.StoreResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 *
 */
inline fun <T> Flow<StoreResponse<T>>.distinctUntilChangedAndFilterEvents(
    crossinline sameTypeAreEquivalent: (
        old: StoreResponse<T>,
        new: StoreResponse<T>
    ) -> Boolean = { old, new -> old == new },
    crossinline onRefreshing: (
        old: StoreResponse<T>,
        new: StoreResponse.Loading<T>
    ) -> Unit = { _, _ -> },
    crossinline onErrorNotice: (
        old: StoreResponse<T>,
        new: StoreResponse.Error<T>
    ) -> Unit = { _, _ -> }
): Flow<StoreResponse<T>> = distinctUntilChanged { old, new ->
    when (old) {
        is StoreResponse.Data -> when (new) {
            is StoreResponse.Data -> sameTypeAreEquivalent(old, new)
            is StoreResponse.Error -> consume { onErrorNotice(old, new) }
            is StoreResponse.Loading -> consume { onRefreshing(old, new) }
        }.exhaustive
        is StoreResponse.Loading -> when (new) {
            is StoreResponse.Data -> false
            is StoreResponse.Error -> false
            is StoreResponse.Loading -> true
        }.exhaustive
        is StoreResponse.Error -> when (new) {
            is StoreResponse.Data -> false
            is StoreResponse.Error -> sameTypeAreEquivalent(old, new)
            is StoreResponse.Loading -> consume { onRefreshing(old, new) }
        }.exhaustive
    }.exhaustive
}
