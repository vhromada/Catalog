package cz.vhromada.catalog.entity

import cz.vhromada.common.entity.Movable
import java.util.Objects

/**
 * A class represents song.
 *
 * @author Vladimir Hromada
 */
data class Song(

        /**
         * ID
         */
        override var id: Int?,

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
        return if (other !is Song || id == null) {
            false
        } else {
            id == other.id
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
