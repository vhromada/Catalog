package cz.vhromada.catalog.service

import cz.vhromada.catalog.domain.Picture
import cz.vhromada.catalog.repository.PictureRepository
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
        pictureRepository: PictureRepository,
        @Value("#{cacheManager.getCache('catalogCache')}") cache: Cache) : AbstractMovableService<Picture>(pictureRepository, cache, "pictures") {

    override fun getCopy(data: Picture): Picture {
        return data.copy(id = null)
    }

}
