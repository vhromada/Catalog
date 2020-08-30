package com.github.vhromada.catalog.mapper

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.entity.Show
import com.github.vhromada.catalog.utils.ShowUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [com.github.vhromada.catalog.domain.Show] and [Show].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
class ShowMapperIntegrationTest {

    /**
     * Instance of [ShowMapper]
     */
    @Autowired
    private lateinit var mapper: ShowMapper

    /**
     * Test method for [ShowMapper.map].
     */
    @Test
    fun map() {
        val show = ShowUtils.newShow(1)
        val showDomain = mapper.map(show)

        ShowUtils.assertShowDeepEquals(show, showDomain)
    }

    /**
     * Test method for [ShowMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val showDomain = ShowUtils.newShowDomain(1)
        val show = mapper.mapBack(showDomain)

        ShowUtils.assertShowDeepEquals(show, showDomain)
    }

}
