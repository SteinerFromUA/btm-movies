package com.steiner.btmmovies.fetcher.mapper

import com.steiner.btmmovies.app.di.account.AccountScope
import com.steiner.btmmovies.core.util.Mapper
import com.steiner.btmmovies.model.entity.OngoingTmdbMovie
import com.steiner.btmmovies.model.entity.TmdbMovie
import com.uwetrottmann.tmdb2.entities.MovieResultsPage
import javax.inject.Inject

/**
 *
 */
@AccountScope
class OngoingResultToOngoingInsertion @Inject internal constructor(
) : Mapper<MovieResultsPage, List<Pair<TmdbMovie, OngoingTmdbMovie>>> {

    override suspend fun map(from: MovieResultsPage): List<Pair<TmdbMovie, OngoingTmdbMovie>> {
        return from.results?.mapIndexedNotNull { index, movie ->
            TmdbMovie(
                movieId = requireNotNull(movie.id),
                title = movie.title ?: movie.original_title ?: "<Unknown>",
                description = movie.overview ?: "<No description>",
                posterUrl = movie.poster_path?.let { posterPath ->
                    "https://image.tmdb.org/t/p/w1280$posterPath"
                }
            ) to OngoingTmdbMovie(
                page = from.page ?: 0,
                pagePosition = index,
                movieId = requireNotNull(movie.id)
            )
        } ?: emptyList()
    }
}