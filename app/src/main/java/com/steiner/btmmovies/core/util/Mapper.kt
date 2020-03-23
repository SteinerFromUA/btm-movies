package com.steiner.btmmovies.core.util

/**
 *
 */
interface Mapper<F, T> {

    suspend fun map(from: F): T
}
