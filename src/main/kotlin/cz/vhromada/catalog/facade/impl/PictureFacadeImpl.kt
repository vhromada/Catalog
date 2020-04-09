package cz.vhromada.catalog.facade.impl

import cz.vhromada.catalog.entity.Picture
import cz.vhromada.catalog.facade.PictureFacade
import cz.vhromada.common.facade.AbstractMovableParentFacade
import cz.vhromada.common.mapper.Mapper
import cz.vhromada.common.provider.AccountProvider
import cz.vhromada.common.provider.TimeProvider
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.validator.MovableValidator
import org.springframework.stereotype.Component

/**
 * A class represents implementation of facade for pictures.
 *
 * @author Vladimir Hromada
 */
@Component("pictureFacade")
class PictureFacadeImpl(
        pictureService: MovableService<cz.vhromada.catalog.domain.Picture>,
        accountProvider: AccountProvider,
        timeProvider: TimeProvider,
        mapper: Mapper<Picture, cz.vhromada.catalog.domain.Picture>,
        pictureValidator: MovableValidator<Picture>
) : AbstractMovableParentFacade<Picture, cz.vhromada.catalog.domain.Picture>(pictureService, accountProvider, timeProvider, mapper, pictureValidator), PictureFacade
