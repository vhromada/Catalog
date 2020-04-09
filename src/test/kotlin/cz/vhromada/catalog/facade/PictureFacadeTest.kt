package cz.vhromada.catalog.facade

import com.nhaarman.mockitokotlin2.any
import cz.vhromada.catalog.entity.Picture
import cz.vhromada.catalog.facade.impl.PictureFacadeImpl
import cz.vhromada.catalog.utils.PictureUtils
import cz.vhromada.common.facade.MovableParentFacade
import cz.vhromada.common.test.facade.MovableParentFacadeTest

/**
 * A class represents test for class [PictureFacade].
 *
 * @author Vladimir Hromada
 */
class PictureFacadeTest : MovableParentFacadeTest<Picture, cz.vhromada.catalog.domain.Picture>() {

    override fun getFacade(): MovableParentFacade<Picture> {
        return PictureFacadeImpl(service, accountProvider, timeProvider, mapper, validator)
    }

    override fun newEntity(id: Int?): Picture {
        return PictureUtils.newPicture(id)
    }

    override fun newDomain(id: Int?): cz.vhromada.catalog.domain.Picture {
        return PictureUtils.newPictureDomain(id)
    }

    override fun anyDomain(): cz.vhromada.catalog.domain.Picture {
        return any()
    }

    override fun anyEntity(): Picture {
        return any()
    }

}
