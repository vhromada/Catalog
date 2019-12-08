package cz.vhromada.catalog.entity

import cz.vhromada.common.Language
import cz.vhromada.common.Movable
import java.util.Objects

/**
 * A class represents season.
 *
 * @author Vladimir Hromada
 */
data class Season(

        /**
         * ID
         */
        override var id: Int?,

        /**
         * Number of season
         */
        val number: Int?,

        /**
         * Starting year
         */
        val startYear: Int?,

        /**
         * Ending year
         */
        val endYear: Int?,

        /**
         * Language
         */
        val language: Language?,

        /**
         * Subtitles
         */
        val subtitles: List<Language?>?,

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
        return if (other !is Season || id == null) {
            false
        } else {
            id == other.id
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
