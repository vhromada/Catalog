package cz.vhromada.catalog.service

import cz.vhromada.catalog.domain.Picture
import cz.vhromada.catalog.repository.PictureRepository
import cz.vhromada.common.entity.Account
import cz.vhromada.common.provider.AccountProvider
import cz.vhromada.common.provider.TimeProvider
import cz.vhromada.common.service.AbstractMovableService
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.Cache
import org.springframework.stereotype.Component

/**
 * A class represents service for pictures.
 *
 * @author Vladimir Hromada
 */
@Component("pictureService")
class PictureService(
        private val pictureRepository: PictureRepository,
        accountProvider: AccountProvider,
        timeProvider: TimeProvider,
        @Value("#{cacheManager.getCache('catalogCache')}") cache: Cache) : AbstractMovableService<Picture>(pictureRepository, accountProvider, timeProvider, cache, "pictures") {

    override fun getAccountData(account: Account): List<Picture> {
        return pictureRepository.findByAuditCreatedUser(account.id)
    }

    override fun getCopy(data: Picture): Picture {
        return data.copy(id = null)
    }

}
