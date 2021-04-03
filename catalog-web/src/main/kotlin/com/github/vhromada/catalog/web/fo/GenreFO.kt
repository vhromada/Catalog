package com.github.vhromada.catalog.web.fo

import java.io.Serializable
import java.util.Objects
import javax.validation.constraints.NotBlank

/**
 * A class represents FO for genre.
 *
 * @author Vladimir Hromada
 */
data class GenreFO(
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
     * Position
     */
    val position: Int?
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other !is GenreFO || id == null) {
            false
        } else id == other.id
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
