package com.github.vhromada.catalog.mapper

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.entity.Cheat
import com.github.vhromada.catalog.utils.CheatUtils
import com.github.vhromada.common.mapper.Mapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [com.github.vhromada.catalog.domain.Cheat] and [Cheat].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
class CheatMapperIntegrationTest {

    /**
     * Instance of [CheatMapper]
     */
    @Autowired
    private lateinit var mapper: Mapper<Cheat, com.github.vhromada.catalog.domain.Cheat>

    /**
     * Test method for [CheatMapper.map].
     */
    @Test
    fun map() {
        val cheat = CheatUtils.newCheat(1)
        val cheatDomain = mapper.map(cheat)

        CheatUtils.assertCheatDeepEquals(cheat, cheatDomain)
    }

    /**
     * Test method for [CheatMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val cheatDomain = CheatUtils.newCheatDomain(1)
        val cheat = mapper.mapBack(cheatDomain)

        CheatUtils.assertCheatDeepEquals(cheat, cheatDomain)
    }

}
