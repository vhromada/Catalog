package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Genre
import com.github.vhromada.catalog.repository.GenreRepository
import com.github.vhromada.common.entity.Account
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.service.AbstractParentService
import org.springframework.stereotype.Component
import java.util.Optional

/**
 * A class represents service for genres.
 *
 * @author Vladimir Hromada
 */
@Component("genreService")
class GenreService(
    private val genreRepository: GenreRepository,
    accountProvider: AccountProvider
) : AbstractParentService<Genre>(repository = genreRepository, accountProvider = accountProvider) {

    override fun getCopy(data: Genre): Genre {
        return data.copy(id = null)
    }

    override fun getAccountData(account: Account, id: Int): Optional<Genre> {
        return genreRepository.findByIdAndCreatedUser(id = id, user = account.uuid!!)
    }

    override fun getAccountDataList(account: Account): List<Genre> {
        return genreRepository.findByCreatedUser(user = account.uuid!!)
    }

}
