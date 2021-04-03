package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.CheatData
import com.github.vhromada.catalog.web.CatalogMapperTestConfiguration
import com.github.vhromada.catalog.web.fo.CheatDataFO
import com.github.vhromada.catalog.web.utils.CheatDataUtils
import com.github.vhromada.common.mapper.Mapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [CheatData] and [CheatDataFO].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogMapperTestConfiguration::class])
class CheatDataMapperIntegrationTest {

    /**
     * Instance of [CheatDataMapper]
     */
    @Autowired
    private lateinit var mapper: Mapper<CheatData, CheatDataFO>

    /**
     * Test method for [CheatDataMapper.map].
     */
    @Test
    fun map() {
        val cheatData = CheatDataUtils.getCheatData()

        val cheatDataFO = mapper.map(source = cheatData)

        CheatDataUtils.assertCheatDataDeepEquals(expected = cheatData, actual = cheatDataFO)
    }

    /**
     * Test method for [CheatDataMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val cheatDataFO = CheatDataUtils.getCheatDataFO()

        val cheatData = mapper.mapBack(source = cheatDataFO)

        CheatDataUtils.assertCheatDataDeepEquals(expected = cheatDataFO, actual = cheatData)
    }

}
