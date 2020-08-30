package com.github.vhromada.catalog.entity

import com.github.vhromada.common.entity.Movable
import java.util.Objects

/**
 * A class represents music.
 *
 * @author Vladimir Hromada
 */
data class Music(

        /**
         * ID
         */
        override var id: Int?,

        /**
         * Name
         */
        val name: String?,

        /**
         * URL to english Wikipedia page about music
         */
        val wikiEn: String?,

        /**
         * URL to czech Wikipedia page about music
         */
        val wikiCz: String?,

        /**
         * Count of media
         */
        val mediaCount: Int?,

        /**
         * Note
         */
        val note: String?,

        /**
         * Position
         */
        override var position: Int?) : Movable {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other !is Music || id == null) {
            false
        } else {
            id == other.id
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
