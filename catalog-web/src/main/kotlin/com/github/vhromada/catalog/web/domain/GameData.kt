package com.github.vhromada.catalog.web.domain

import com.github.vhromada.catalog.entity.Cheat
import com.github.vhromada.catalog.entity.Game

/**
 * A class represents game data.
 *
 * @author Vladimir Hromada
 */
data class GameData(

        /**
         * Game
         */
        val game: Game,

        /**
         * Cheat
         */
        val cheat: Cheat?)