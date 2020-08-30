package com.github.vhromada.catalog.entity

import com.github.vhromada.common.entity.Movable
import java.util.Objects

/**
 * A class represents episode.
 *
 * @author Vladimir Hromada
 */
data class Episode(

        /**
         * ID
         */
        override var id: Int?,

        /**
         * Number of episode
         */
        val number: Int?,

        /**
         * Name
         */
        val name: String?,

        /**
         * Length
         */
        val length: Int?,

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
        return if (other !is Episode || id == null) {
            false
        } else {
            id == other.id
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
