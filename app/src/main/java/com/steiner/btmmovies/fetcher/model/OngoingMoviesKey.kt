package com.steiner.btmmovies.fetcher.model

import com.uwetrottmann.tmdb2.entities.TmdbDate

/**
 *
 */
data class OngoingMoviesKey(
    val lteReleaseDate: TmdbDate,
    val gteReleaseDate: TmdbDate
)
