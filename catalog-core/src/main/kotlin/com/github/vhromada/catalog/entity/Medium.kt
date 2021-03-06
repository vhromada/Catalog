package com.github.vhromada.catalog.entity

import com.github.vhromada.common.entity.Identifiable
import java.util.Objects

/**
 * A class represents medium.
 *
 * @author Vladimir Hromada
 */
data class Medium(
    /**
     * ID
     */
    override var id: Int?,

    /**
     * Number
     */
    val number: Int?,

    /**
     * Length
     */
    val length: Int?
) : Identifiable {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other !is Medium || id == null) {
            false
        } else {
            id == other.id
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
