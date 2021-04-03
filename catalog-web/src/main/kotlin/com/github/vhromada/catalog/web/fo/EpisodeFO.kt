package com.github.vhromada.catalog.web.fo

import org.hibernate.validator.constraints.Range
import java.io.Serializable
import java.util.Objects
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

/**
 * A class represents FO for episode.
 *
 * @author Vladimir Hromada
 */
data class EpisodeFO(
    /**
     * ID
     */
    val id: Int?,

    /**
     * Number of episode
     */
    @field:Range(min = 1, max = 500)
    val number: String?,

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
    val position: Int?
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other !is EpisodeFO || id == null) {
            false
        } else id == other.id
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
