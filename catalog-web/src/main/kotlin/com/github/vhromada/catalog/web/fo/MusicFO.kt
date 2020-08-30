package com.github.vhromada.catalog.web.fo

import org.hibernate.validator.constraints.Range
import java.io.Serializable
import java.util.Objects
import javax.validation.constraints.NotBlank

/**
 * A class represents FO for music.
 *
 * @author Vladimir Hromada
 */
data class MusicFO(

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
        @field:Range(min = 1, max = 100)
        val mediaCount: String?,

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
        return if (other !is MusicFO || id == null) {
            false
        } else id == other.id
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
