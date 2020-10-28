package com.github.vhromada.catalog.mapper

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.entity.Game
import com.github.vhromada.catalog.utils.GameUtils
import com.github.vhromada.common.mapper.Mapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [com.github.vhromada.catalog.domain.Game] and [Game].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
class GameMapperIntegrationTest {

    /**
     * Instance of [GameMapper]
     */
    @Autowired
    private lateinit var mapper: Mapper<Game, com.github.vhromada.catalog.domain.Game>

    /**
     * Test method for [GameMapper.map].
     */
    @Test
    fun map() {
        val game = GameUtils.newGame(1)
        val gameDomain = mapper.map(game)

        GameUtils.assertGameDeepEquals(game, gameDomain)
    }

    /**
     * Test method for [GameMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val gameDomain = GameUtils.newGameDomain(1)
        val game = mapper.mapBack(gameDomain)

        GameUtils.assertGameDeepEquals(game, gameDomain)
    }

}
