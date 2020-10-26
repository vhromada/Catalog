package com.github.vhromada.catalog.entity

import com.github.vhromada.common.entity.Movable
import java.util.Objects

/**
 * A class represents cheat.
 *
 * @author Vladimir Hromada
 */
data class Cheat(

        /**
         * ID
         */
        override var id: Int?,

        /**
         * Setting for game
         */
        val gameSetting: String?,

        /**
         * Setting for cheat
         */
        val cheatSetting: String?,

        /**
         * Data
         */
        val data: List<CheatData?>?,

        /**
         * Position
         */
        override var position: Int?) : Movable {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other !is Cheat || id == null) {
            false
        } else {
            id == other.id
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
