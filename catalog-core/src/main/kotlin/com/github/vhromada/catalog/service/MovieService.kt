package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Movie
import com.github.vhromada.catalog.repository.MovieRepository
import com.github.vhromada.common.entity.Account
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.service.AbstractParentService
import org.springframework.stereotype.Component
import java.util.Optional

/**
 * A class represents service for movies.
 *
 * @author Vladimir Hromada
 */
@Component("movieService")
class MovieService(
    private val movieRepository: MovieRepository,
    accountProvider: AccountProvider
) : AbstractParentService<Movie>(repository = movieRepository, accountProvider = accountProvider) {

    override fun getCopy(data: Movie): Movie {
        return data.copy(id = null, subtitles = data.subtitles.map { it }, media = data.media.map { it.copy(id = null) }, genres = data.genres.map { it })
    }

    override fun getAccountData(account: Account, id: Int): Optional<Movie> {
        return movieRepository.findByIdAndCreatedUser(id = id, user = account.uuid!!)
    }

    override fun getAccountDataList(account: Account): List<Movie> {
        return movieRepository.findByCreatedUser(user = account.uuid!!)
    }

}
