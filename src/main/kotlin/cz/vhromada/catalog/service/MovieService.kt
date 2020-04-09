package cz.vhromada.catalog.service

import cz.vhromada.catalog.domain.Movie
import cz.vhromada.catalog.repository.MovieRepository
import cz.vhromada.common.entity.Account
import cz.vhromada.common.provider.AccountProvider
import cz.vhromada.common.provider.TimeProvider
import cz.vhromada.common.service.AbstractMovableService
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.Cache
import org.springframework.stereotype.Component

/**
 * A class represents service for movies.
 *
 * @author Vladimir Hromada
 */
@Component("movieService")
class MovieService(
        private val movieRepository: MovieRepository,
        accountProvider: AccountProvider,
        timeProvider: TimeProvider,
        @Value("#{cacheManager.getCache('catalogCache')}") cache: Cache) : AbstractMovableService<Movie>(movieRepository, accountProvider, timeProvider, cache, "movies") {

    override fun getAccountData(account: Account): List<Movie> {
        return movieRepository.findByAuditCreatedUser(account.id)
    }

    override fun getCopy(data: Movie): Movie {
        return data.copy(id = null, subtitles = data.subtitles.map { it }, media = data.media.map { it.copy(id = null) }, genres = data.genres.map { it })
    }

}
