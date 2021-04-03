package com.github.vhromada.catalog.entity

import com.github.vhromada.catalog.common.Format
import com.github.vhromada.common.entity.Movable
import java.util.Objects

/**
 * A class represents program.
 *
 * @author Vladimir Hromada
 */
data class Program(
    /**
     * ID
     */
    override var id: Int?,

    /**
     * Name
     */
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
    val mediaCount: Int?,

    /**
     * Format
     */
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
    override var position: Int?
) : Movable {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other !is Program || id == null) {
            false
        } else {
            id == other.id
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
