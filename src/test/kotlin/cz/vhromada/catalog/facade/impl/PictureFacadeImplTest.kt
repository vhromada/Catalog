package cz.vhromada.catalog.facade.impl

import com.nhaarman.mockitokotlin2.any
import cz.vhromada.catalog.entity.Picture
import cz.vhromada.catalog.utils.PictureUtils
import cz.vhromada.common.facade.MovableParentFacade
import cz.vhromada.common.test.facade.MovableParentFacadeTest

/**
 * A class represents test for class [PictureFacadeImpl].
 *
 * @author Vladimir Hromada
 */
class PictureFacadeImplTest : MovableParentFacadeTest<Picture, cz.vhromada.catalog.domain.Picture>() {

    override fun getFacade(): MovableParentFacade<Picture> {
        return PictureFacadeImpl(service, mapper, validator)
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
