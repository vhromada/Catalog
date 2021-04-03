package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.Cheat
import com.github.vhromada.catalog.web.CatalogMapperTestConfiguration
import com.github.vhromada.catalog.web.fo.CheatFO
import com.github.vhromada.catalog.web.utils.CheatUtils
import com.github.vhromada.common.mapper.Mapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [Cheat] and [CheatFO].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogMapperTestConfiguration::class])
class CheatMapperIntegrationTest {

    /**
     * Instance of [CheatMapper]
     */
    @Autowired
    private lateinit var mapper: Mapper<Cheat, CheatFO>

    /**
     * Test method for [CheatMapper.map].
     */
    @Test
    fun map() {
        val cheat = CheatUtils.getCheat()
        val cheatFO = mapper.map(source = cheat)

        CheatUtils.assertCheatDeepEquals(expected = cheat, actual = cheatFO)
    }

    /**
     * Test method for [CheatMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val cheatFO = CheatUtils.getCheatFO()
        val cheat = mapper.mapBack(source = cheatFO)

        CheatUtils.assertCheatDeepEquals(expected = cheatFO, actual = cheat)
    }

}
