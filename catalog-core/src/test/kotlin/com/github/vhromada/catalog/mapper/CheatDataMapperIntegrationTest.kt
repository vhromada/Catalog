package com.github.vhromada.catalog.mapper

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.entity.CheatData
import com.github.vhromada.catalog.utils.CheatDataUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [com.github.vhromada.catalog.domain.CheatData] and [CheatData].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
class CheatDataMapperIntegrationTest {

    /**
     * Instance of [CheatDataMapper]
     */
    @Autowired
    private lateinit var mapper: CheatDataMapper

    /**
     * Test method for [CheatDataMapper.map].
     */
    @Test
    fun map() {
        val cheatData = CheatDataUtils.newCheatData(1)
        val cheatDataDomain = mapper.map(cheatData)

        CheatDataUtils.assertCheatDataDeepEquals(cheatData, cheatDataDomain)
    }

    /**
     * Test method for [CheatDataMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val cheatDataDomain = CheatDataUtils.newCheatDataDomain(1)
        val cheatData = mapper.mapBack(cheatDataDomain)

        CheatDataUtils.assertCheatDataDeepEquals(cheatData, cheatDataDomain)
    }

}
