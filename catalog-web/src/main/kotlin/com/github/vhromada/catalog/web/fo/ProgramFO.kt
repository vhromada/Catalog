package com.github.vhromada.catalog.web.fo

import org.hibernate.validator.constraints.Range
import java.io.Serializable
import java.util.Objects
import javax.validation.constraints.NotBlank

/**
 * A class represents FO for program.
 *
 * @author Vladimir Hromada
 */
data class ProgramFO(

        /**
         * ID
         */
        val id: Int?,

        /**
         * Name
         */
        @field:NotBlank
        val name: String?,

        /**
         * URL to english Wikipedia page about program
         */
        val wikiEn: String?,

        /**
         * URL to czech Wikipedia page about program
         */
        val wikiCz: String?,

        /**
         * Count of media
         */
        @field:Range(min = 1, max = 100)
        val mediaCount: String?,

        /**
         * True if there is crack
         */
        val crack: Boolean?,

        /**
         * True if there is serial key
         */
        val serialKey: Boolean?,

        /**
         * Other data
         */
        val otherData: String?,

        /**
         * Note
         */
        val note: String?,

        /**
         * Position
         */
        val position: Int?) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other !is ProgramFO || id == null) {
            false
        } else id == other.id
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
