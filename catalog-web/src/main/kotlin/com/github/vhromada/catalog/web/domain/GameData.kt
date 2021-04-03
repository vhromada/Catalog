package com.github.vhromada.catalog.web.domain

import com.github.vhromada.catalog.entity.Cheat
import com.github.vhromada.catalog.entity.Game
import java.io.Serializable
import java.util.Objects

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
    val cheat: Cheat?
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other !is GameData) {
            false
        } else game == other.game
    }

    override fun hashCode(): Int {
        return Objects.hashCode(game)
    }

}
