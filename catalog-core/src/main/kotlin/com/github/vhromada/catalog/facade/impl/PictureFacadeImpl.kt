package com.github.vhromada.catalog.facade.impl

import com.github.vhromada.catalog.entity.Picture
import com.github.vhromada.catalog.facade.PictureFacade
import com.github.vhromada.common.facade.AbstractParentFacade
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.service.ParentService
import com.github.vhromada.common.validator.Validator
import org.springframework.stereotype.Component

/**
 * A class represents implementation of facade for pictures.
 *
 * @author Vladimir Hromada
 */
@Component("pictureFacade")
class PictureFacadeImpl(
    pictureService: ParentService<com.github.vhromada.catalog.domain.Picture>,
    mapper: Mapper<Picture, com.github.vhromada.catalog.domain.Picture>,
    pictureValidator: Validator<Picture, com.github.vhromada.catalog.domain.Picture>
) : AbstractParentFacade<Picture, com.github.vhromada.catalog.domain.Picture>(parentService = pictureService, mapper = mapper, validator = pictureValidator), PictureFacade {

    @Suppress("DuplicatedCode")
    override fun updateData(data: Picture): Result<Unit> {
        val storedPicture = service.get(id = data.id!!)
        val validationResult = validator.validateExists(data = storedPicture)
        if (validationResult.isOk()) {
            val picture = mapper.map(source = data)
            picture.createdUser = storedPicture.get().createdUser
            picture.createdTime = storedPicture.get().createdTime
            service.update(data = picture)
        }
        return validationResult
    }

    override fun addData(data: Picture): Result<Unit> {
        service.add(data = mapper.map(source = data))
        return Result()
    }

}
