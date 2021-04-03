package com.github.vhromada.catalog.web.domain

import com.github.vhromada.catalog.entity.Show
import com.github.vhromada.common.entity.Time
import java.io.Serializable
import java.util.Objects

/**
 * A class represents show data.
 *
 * @author Vladimir Hromada
 */
data class ShowData(
    /**
     * Show
     */
    val show: Show,

    /**
     * Count of seasons
     */
    val seasonsCount: Int,

    /**
     * Count of episodes
     */
    val episodesCount: Int,

    /**
     * Total length
     */
    val totalLength: Time
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other !is ShowData) {
            false
        } else show == other.show
    }

    override fun hashCode(): Int {
        return Objects.hashCode(show)
    }

}
