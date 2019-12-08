package cz.vhromada.catalog.service

import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import cz.vhromada.catalog.domain.Picture
import cz.vhromada.catalog.repository.PictureRepository
import cz.vhromada.catalog.utils.PictureUtils
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.test.service.MovableServiceTest
import org.mockito.Mock
import org.springframework.data.jpa.repository.JpaRepository

/**
 * A class represents test for class [PictureService].
 *
 * @author Vladimir Hromada
 */
class PictureServiceTest : MovableServiceTest<Picture>() {

    /**
     * Instance of [PictureRepository]
     */
    @Mock
    private lateinit var repository: PictureRepository

    override fun getRepository(): JpaRepository<Picture, Int> {
        return repository
    }

    override fun getService(): MovableService<Picture> {
        return PictureService(repository, cache)
    }

    override fun getCacheKey(): String {
        return "pictures"
    }

    override fun getItem1(): Picture {
        return PictureUtils.newPictureDomain(1)
    }

    override fun getItem2(): Picture {
        return PictureUtils.newPictureDomain(2)
    }

    override fun getAddItem(): Picture {
        return PictureUtils.newPictureDomain(null)
    }

    override fun getCopyItem(): Picture {
        return PictureUtils.newPictureDomain(null)
                .copy(position = 0)
    }

    override fun anyItem(): Picture {
        return any()
    }

    override fun argumentCaptorItem(): KArgumentCaptor<Picture> {
        return argumentCaptor()
    }

    override fun assertDataDeepEquals(expected: Picture, actual: Picture) {
        PictureUtils.assertPictureDeepEquals(expected, actual)
    }

}
