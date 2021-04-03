package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.Show
import com.github.vhromada.catalog.web.CatalogMapperTestConfiguration
import com.github.vhromada.catalog.web.fo.ShowFO
import com.github.vhromada.catalog.web.utils.ShowUtils
import com.github.vhromada.common.mapper.Mapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [Show] and [ShowFO].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogMapperTestConfiguration::class])
class ShowMapperIntegrationTest {

    /**
     * Instance of [ShowMapper]
     */
    @Autowired
    private lateinit var mapper: Mapper<Show, ShowFO>

    /**
     * Test method for [ShowMapper.map].
     */
    @Test
    fun map() {
        val show = ShowUtils.getShow()
        val showFO = mapper.map(source = show)

        ShowUtils.assertShowDeepEquals(expected = show, actual = showFO)
    }

    /**
     * Test method for [ShowMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val showFO = ShowUtils.getShowFO()
        val show = mapper.mapBack(source = showFO)

        ShowUtils.assertShowDeepEquals(expected = showFO, actual = show)
    }

}
