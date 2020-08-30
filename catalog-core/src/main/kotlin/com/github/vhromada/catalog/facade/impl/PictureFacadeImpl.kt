package com.github.vhromada.catalog.facade.impl

import com.github.vhromada.catalog.entity.Picture
import com.github.vhromada.catalog.facade.PictureFacade
import com.github.vhromada.common.facade.AbstractMovableParentFacade
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.provider.TimeProvider
import com.github.vhromada.common.service.MovableService
import com.github.vhromada.common.validator.MovableValidator
import org.springframework.stereotype.Component

/**
 * A class represents implementation of facade for pictures.
 *
 * @author Vladimir Hromada
 */
@Component("pictureFacade")
class PictureFacadeImpl(
        pictureService: MovableService<com.github.vhromada.catalog.domain.Picture>,
        accountProvider: AccountProvider,
        timeProvider: TimeProvider,
        mapper: Mapper<Picture, com.github.vhromada.catalog.domain.Picture>,
        pictureValidator: MovableValidator<Picture>
) : AbstractMovableParentFacade<Picture, com.github.vhromada.catalog.domain.Picture>(pictureService, accountProvider, timeProvider, mapper, pictureValidator), PictureFacade
