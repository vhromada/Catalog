package cz.vhromada.catalog.facade

import cz.vhromada.catalog.entity.Episode
import cz.vhromada.catalog.entity.Season
import cz.vhromada.common.facade.MovableChildFacade
import cz.vhromada.common.result.Result

/**
 * An interface represents facade for episodes.
 *
 * @author Vladimir Hromada
 */
interface EpisodeFacade : MovableChildFacade<Episode, Season> {

    /**
     * Adds episode. Sets new ID and position.
     * <br></br>
     * Validation errors:
     *
     *  * Season ID is null
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
     * @param parent season
     * @param data   episode
     * @return result with validation errors
     */
    override fun add(parent: Season, data: Episode): Result<Unit>

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
