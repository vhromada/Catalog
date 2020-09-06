package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Genre
import com.github.vhromada.catalog.repository.GenreRepository
import com.github.vhromada.common.entity.Account
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.provider.TimeProvider
import com.github.vhromada.common.service.AbstractMovableService
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.Cache
import org.springframework.stereotype.Component

/**
 * A class represents service for genres.
 *
 * @author Vladimir Hromada
 */
@Component("genreService")
@Suppress("SpringElInspection", "ELValidationInJSP")
class GenreService(
        private val genreRepository: GenreRepository,
        accountProvider: AccountProvider,
        timeProvider: TimeProvider,
        @Value("#{cacheManager.getCache('catalogCache')}") cache: Cache) : AbstractMovableService<Genre>(genreRepository, accountProvider, timeProvider, cache, "genres") {

    override fun getAccountData(account: Account): List<Genre> {
        return genreRepository.findByAuditCreatedUser(account.uuid!!)
    }

    override fun getCopy(data: Genre): Genre {
        return data.copy(id = null)
    }

}
