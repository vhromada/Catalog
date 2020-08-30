package com.github.vhromada.catalog.entity

import com.github.vhromada.common.entity.Movable
import java.util.Objects

/**
 * A class represents picture.
 *
 * @author Vladimir Hromada
 */
data class Picture(

        /**
         * ID
         */
        override var id: Int?,

        /**
         * Picture
         */
        val content: ByteArray?,

        /**
         * Position
         */
        override var position: Int?) : Movable {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other !is Picture || id == null) {
            false
        } else {
            id == other.id
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
