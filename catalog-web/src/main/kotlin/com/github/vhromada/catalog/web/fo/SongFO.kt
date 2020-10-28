package com.github.vhromada.catalog.web.fo

import java.io.Serializable
import java.util.Objects

import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

/**
 * A class represents FO for song.
 *
 * @author Vladimir Hromada
 */
data class SongFO(

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
         * Length
         */
        @field:NotNull
        @field:Valid
        var length: TimeFO?,

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
        return if (other !is SongFO || id == null) {
            false
        } else id == other.id
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
