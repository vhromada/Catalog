package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Season
import com.github.vhromada.catalog.entity.Show
import com.github.vhromada.common.facade.MovableChildFacade
import com.github.vhromada.common.result.Result

/**
 * An interface represents facade for seasons.
 *
 * @author Vladimir Hromada
 */
interface SeasonFacade : MovableChildFacade<Season, Show> {

    /**
     * Adds season. Sets new ID and position.
     * <br></br>
     * Validation errors:
     *
     *  * Show ID is null
     *  * Show doesn't exist in data storage
     *  * Season ID isn't null
     *  * Season position isn't null
     *  * Number of season is null
     *  * Number of season isn't positive number
     *  * Starting year is null
     *  * Starting year isn't between 1940 and current year
     *  * Ending year is null
     *  * Ending year isn't between 1940 and current year
     *  * Starting year is greater than ending year
     *  * Language is null
     *  * Subtitles are null
     *  * Subtitles contain null value
     *  * Note is null
     *
     * @param parent show
     * @param data   season
     * @return result with validation errors
     */
    override fun add(parent: Show, data: Season): Result<Unit>

    /**
     * Updates season.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Position is null
     *  * Number of season is null
     *  * Number of season isn't positive number
     *  * Starting year is null
     *  * Starting year isn't between 1940 and current year
     *  * Ending year is null
     *  * Ending year isn't between 1940 and current year
     *  * Starting year is greater than ending year
     *  * Language is null
     *  * Subtitles are null
     *  * Subtitles contain null value
     *  * Note is null
     *  * Season doesn't exist in data storage
     *
     * @param data new value of season
     * @return result with validation errors
     */
    override fun update(data: Season): Result<Unit>

}
