package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Picture
import com.github.vhromada.catalog.repository.PictureRepository
import com.github.vhromada.common.entity.Account
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.service.AbstractParentService
import org.springframework.stereotype.Component
import java.util.Optional

/**
 * A class represents service for pictures.
 *
 * @author Vladimir Hromada
 */
@Component("pictureService")
class PictureService(
    private val pictureRepository: PictureRepository,
    accountProvider: AccountProvider
) : AbstractParentService<Picture>(repository = pictureRepository, accountProvider = accountProvider) {

    override fun getCopy(data: Picture): Picture {
        return data.copy(id = null)
    }

    override fun getAccountData(account: Account, id: Int): Optional<Picture> {
        return pictureRepository.findByIdAndCreatedUser(id = id, user = account.uuid!!)
    }

    override fun getAccountDataList(account: Account): List<Picture> {
        return pictureRepository.findByCreatedUser(user = account.uuid!!)
    }

}
