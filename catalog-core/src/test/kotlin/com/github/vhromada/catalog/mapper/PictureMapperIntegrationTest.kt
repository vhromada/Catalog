package com.github.vhromada.catalog.mapper

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.entity.Picture
import com.github.vhromada.catalog.utils.PictureUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [com.github.vhromada.catalog.domain.Picture] and [Picture].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
class PictureMapperIntegrationTest {

    /**
     * Instance of [PictureMapper]
     */
    @Autowired
    private lateinit var mapper: PictureMapper

    /**
     * Test method for [PictureMapper.map].
     */
    @Test
    fun map() {
        val picture = PictureUtils.newPicture(1)
        val pictureDomain = mapper.map(picture)

        PictureUtils.assertPictureDeepEquals(picture, pictureDomain)
    }

    /**
     * Test method for [PictureMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val pictureDomain = PictureUtils.newPictureDomain(1)
        val picture = mapper.mapBack(pictureDomain)

        PictureUtils.assertPictureDeepEquals(picture, pictureDomain)
    }

}
