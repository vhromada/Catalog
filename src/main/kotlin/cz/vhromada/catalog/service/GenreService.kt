package cz.vhromada.catalog.service

import cz.vhromada.catalog.domain.Genre
import cz.vhromada.catalog.repository.GenreRepository
import cz.vhromada.common.entity.Account
import cz.vhromada.common.provider.AccountProvider
import cz.vhromada.common.provider.TimeProvider
import cz.vhromada.common.service.AbstractMovableService
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.Cache
import org.springframework.stereotype.Component

/**
 * A class represents service for genres.
 *
 * @author Vladimir Hromada
 */
@Component("genreService")
class GenreService(
        private val genreRepository: GenreRepository,
        accountProvider: AccountProvider,
        timeProvider: TimeProvider,
        @Value("#{cacheManager.getCache('catalogCache')}") cache: Cache) : AbstractMovableService<Genre>(genreRepository, accountProvider, timeProvider, cache, "genres") {

    override fun getAccountData(account: Account): List<Genre> {
        return genreRepository.findByAuditCreatedUser(account.id)
    }

    override fun getCopy(data: Genre): Genre {
        return data.copy(id = null)
    }

}
