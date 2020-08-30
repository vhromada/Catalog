package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.Game
import com.github.vhromada.catalog.web.CatalogMapperTestConfiguration
import com.github.vhromada.catalog.web.common.GameUtils
import com.github.vhromada.catalog.web.fo.GameFO
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
class GameMapperTest {

    /**
     * Mapper for games
     */
    @Autowired
    private lateinit var mapper: GameMapper

    /**
     * Test method for [GameMapper.map].
     */
    @Test
    fun map() {
        val game = GameUtils.getGame()

        val gameFO = mapper.map(game)

        GameUtils.assertGameDeepEquals(gameFO, game)
    }

    /**
     * Test method for [GameMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val gameFO = GameUtils.getGameFO()

        val game = mapper.mapBack(gameFO)

        GameUtils.assertGameDeepEquals(gameFO, game)
    }

}
