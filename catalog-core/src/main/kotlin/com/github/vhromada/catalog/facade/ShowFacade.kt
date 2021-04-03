package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Show
import com.github.vhromada.common.entity.Time
import com.github.vhromada.common.facade.ParentFacade
import com.github.vhromada.common.result.Result

/**
 * An interface represents facade for shows.
 *
 * @author Vladimir Hromada
 */
interface ShowFacade : ParentFacade<Show> {

    /**
     * Adds show. Sets new ID and position.
     * <br></br>
     * Validation errors:
     *
     *  * ID isn't null
     *  * Position isn't null
     *  * Czech name is null
     *  * Czech name is empty string
     *  * Original name is null
     *  * Original name is empty string
     *  * URL to ČSFD page about show is null
     *  * IMDB code is null
     *  * IMDB code isn't -1 or between 1 and 9999999
     *  * URL to english Wikipedia page about show is null
     *  * URL to czech Wikipedia page about show is null
     *  * Note is null
     *  * Picture doesn't exist
     *  * Genres are null
     *  * Genres contain null value
     *  * Genre ID is null
     *  * Genre name is null
     *  * Genre name is empty string
     *  * Genre doesn't exist
     *
     * @param data show
     * @return result with validation errors
     */
    override fun add(data: Show): Result<Unit>

    /**
     * Updates show.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Position is null
     *  * Czech name is null
     *  * Czech name is empty string
     *  * Original name is null
     *  * Original name is empty string
     *  * URL to ČSFD page about show is null
     *  * IMDB code is null
     *  * IMDB code isn't -1 or between 1 and 9999999
     *  * URL to english Wikipedia page about show is null
     *  * URL to czech Wikipedia page about show is null
     *  * Note is null
     *  * Picture doesn't exist
     *  * Genres are null
     *  * Genres contain null value
     *  * Genre ID is null
     *  * Genre name is null
     *  * Genre name is empty string
     *  * Genre doesn't exist
     *  * Show doesn't exist in data storage
     *
     * @param data new value of show
     * @return result with validation errors
     */
    override fun update(data: Show): Result<Unit>

    /**
     * Returns total length of all shows.
     *
     * @return result with total length of all shows
     */
    fun getTotalLength(): Result<Time>

    /**
     * Returns count of seasons from all shows.
     *
     * @return result with count of seasons from all shows
     */
    fun getSeasonsCount(): Result<Int>

    /**
     * Returns count of episodes from all shows.
     *
     * @return result with count of episodes from all shows
     */
    fun getEpisodesCount(): Result<Int>

}
