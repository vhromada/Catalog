package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Episode
import com.github.vhromada.catalog.entity.Season
import com.github.vhromada.common.facade.ChildFacade
import com.github.vhromada.common.result.Result

/**
 * An interface represents facade for episodes.
 *
 * @author Vladimir Hromada
 */
interface EpisodeFacade : ChildFacade<Episode, Season> {

    /**
     * Adds episode. Sets new ID and position.
     * <br></br>
     * Validation errors:
     *
     *  * Season doesn't exist in data storage
     *  * Episode ID isn't null
     *  * Episode position isn't null
     *  * Number of episode is null
     *  * Number of episode isn't positive number
     *  * Name is null
     *  * Name is empty string
     *  * Length of episode is null
     *  * Length of episode is negative value
     *  * Note is null
     *
     * @param parent season ID
     * @param data   episode
     * @return result with validation errors
     */
    override fun add(parent: Int, data: Episode): Result<Unit>

    /**
     * Updates episode.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Position is null
     *  * Number of episode is null
     *  * Number of episode isn't positive number
     *  * Name is null
     *  * Name is empty string
     *  * Length of episode is null
     *  * Length of episode is negative value
     *  * Note is null
     *  * Episode doesn't exist in data storage
     *
     * @param data new value of episode
     * @return result with validation errors
     */
    override fun update(data: Episode): Result<Unit>

}
