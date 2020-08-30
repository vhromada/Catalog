package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Picture
import com.github.vhromada.catalog.repository.PictureRepository
import com.github.vhromada.common.entity.Account
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.provider.TimeProvider
import com.github.vhromada.common.service.AbstractMovableService
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.Cache
import org.springframework.stereotype.Component

/**
 * A class represents service for pictures.
 *
 * @author Vladimir Hromada
 */
@Component("pictureService")
@Suppress("SpringElInspection", "ELValidationInJSP")
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
