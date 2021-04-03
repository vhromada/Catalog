package com.github.vhromada.catalog.web.fo

import com.github.vhromada.catalog.common.Format
import org.hibernate.validator.constraints.Range
import java.io.Serializable
import java.util.Objects
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

/**
 * A class represents FO for game.
 *
 * @author Vladimir Hromada
 */
data class GameFO(
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
     * URL to english Wikipedia page about game
     */
    val wikiEn: String?,

    /**
     * URL to czech Wikipedia page about game
     */
    val wikiCz: String?,

    /**
     * Count of media
     */
    @field:Range(min = 1, max = 100)
    val mediaCount: String?,

    /**
     * Format
     */
    @field:NotNull
    val format: Format?,

    /**
     * True if there is crack
     */
    val crack: Boolean?,

    /**
     * True if there is serial key
     */
    val serialKey: Boolean?,

    /**
     * True if there is patch
     */
    val patch: Boolean?,

    /**
     * True if there is trainer
     */
    val trainer: Boolean?,

    /**
     * True if there is data for trainer
     */
    val trainerData: Boolean?,

    /**
     * True if there is editor
     */
    val editor: Boolean?,

    /**
     * True if there are saves
     */
    val saves: Boolean?,

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
    val position: Int?
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other !is GameFO || id == null) {
            false
        } else id == other.id
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
