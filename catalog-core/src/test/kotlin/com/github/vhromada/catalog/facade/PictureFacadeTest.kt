package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Picture
import com.github.vhromada.catalog.facade.impl.PictureFacadeImpl
import com.github.vhromada.catalog.utils.PictureUtils
import com.github.vhromada.common.facade.MovableParentFacade
import com.github.vhromada.common.test.facade.MovableParentFacadeTest
import com.nhaarman.mockitokotlin2.any

/**
 * A class represents test for class [PictureFacade].
 *
 * @author Vladimir Hromada
 */
class PictureFacadeTest : MovableParentFacadeTest<Picture, com.github.vhromada.catalog.domain.Picture>() {

    override fun getFacade(): MovableParentFacade<Picture> {
        return PictureFacadeImpl(service, accountProvider, timeProvider, mapper, validator)
    }

    override fun newEntity(id: Int?): Picture {
        return PictureUtils.newPicture(id)
    }

    override fun newDomain(id: Int?): com.github.vhromada.catalog.domain.Picture {
        return PictureUtils.newPictureDomain(id)
    }

    override fun anyDomain(): com.github.vhromada.catalog.domain.Picture {
        return any()
    }

    override fun anyEntity(): Picture {
        return any()
    }

}
