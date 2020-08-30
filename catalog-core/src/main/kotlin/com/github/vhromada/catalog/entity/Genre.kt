package com.github.vhromada.catalog.entity

import com.github.vhromada.common.entity.Movable
import java.util.Objects

/**
 * A class represents genre.
 *
 * @author Vladimir Hromada
 */
data class Genre(

        /**
         * ID
         */
        override var id: Int?,

        /**
         * Name
         */
        val name: String?,

        /**
         * Position
         */
        override var position: Int?) : Movable {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other !is Genre || id == null) {
            false
        } else {
            id == other.id
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
