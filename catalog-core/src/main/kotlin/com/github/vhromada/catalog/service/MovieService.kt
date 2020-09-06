package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Movie
import com.github.vhromada.catalog.repository.MovieRepository
import com.github.vhromada.common.entity.Account
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.provider.TimeProvider
import com.github.vhromada.common.service.AbstractMovableService
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.Cache
import org.springframework.stereotype.Component

/**
 * A class represents service for movies.
 *
 * @author Vladimir Hromada
 */
@Component("movieService")
@Suppress("SpringElInspection", "ELValidationInJSP")
class MovieService(
        private val movieRepository: MovieRepository,
        accountProvider: AccountProvider,
        timeProvider: TimeProvider,
        @Value("#{cacheManager.getCache('catalogCache')}") cache: Cache) : AbstractMovableService<Movie>(movieRepository, accountProvider, timeProvider, cache, "movies") {

    override fun getAccountData(account: Account): List<Movie> {
        return movieRepository.findByAuditCreatedUser(account.uuid!!)
    }

    override fun getCopy(data: Movie): Movie {
        return data.copy(id = null, subtitles = data.subtitles.map { it }, media = data.media.map { it.copy(id = null) }, genres = data.genres.map { it })
    }

}
