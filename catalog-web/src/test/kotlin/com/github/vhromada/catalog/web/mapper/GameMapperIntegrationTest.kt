package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.Game
import com.github.vhromada.catalog.web.CatalogMapperTestConfiguration
import com.github.vhromada.catalog.web.fo.GameFO
import com.github.vhromada.catalog.web.utils.GameUtils
import com.github.vhromada.common.mapper.Mapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [Game] and [GameFO].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogMapperTestConfiguration::class])
class GameMapperIntegrationTest {

    /**
     * Instance of [GameMapper]
     */
    @Autowired
    private lateinit var mapper: Mapper<Game, GameFO>

    /**
     * Test method for [GameMapper.map].
     */
    @Test
    fun map() {
        val game = GameUtils.getGame()
        val gameFO = mapper.map(source = game)

        GameUtils.assertGameDeepEquals(expected = game, actual = gameFO)
    }

    /**
     * Test method for [GameMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val gameFO = GameUtils.getGameFO()
        val game = mapper.mapBack(source = gameFO)

        GameUtils.assertGameDeepEquals(expected = gameFO, actual = game)
    }

}
