package com.github.vhromada.catalog.entity

import com.github.vhromada.common.entity.Identifiable
import java.util.Objects

/**
 * A class represents cheat's data.
 *
 * @author Vladimir Hromada
 */
data class CheatData(
    /**
     * ID
     */
    override var id: Int?,

    /**
     * Action
     */
    val action: String?,

    /**
     * Description
     */
    val description: String?
) : Identifiable {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other !is CheatData || id == null) {
            false
        } else {
            id == other.id
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
