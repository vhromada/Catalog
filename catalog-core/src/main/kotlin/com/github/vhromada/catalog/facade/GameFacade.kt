package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Game
import com.github.vhromada.common.facade.ParentFacade
import com.github.vhromada.common.result.Result

/**
 * An interface represents facade for games.
 *
 * @author Vladimir Hromada
 */
interface GameFacade : ParentFacade<Game> {

    /**
     * Adds game. Sets new ID and position.
     * <br></br>
     * Validation errors:
     *
     *  * ID isn't null
     *  * Position isn't null
     *  * Name is null
     *  * Name is empty string
     *  * URL to english Wikipedia page about game is null
     *  * URL to czech Wikipedia page about game is null
     *  * Count of media is null
     *  * Count of media isn't positive number
     *  * Format is null
     *  * Other data is null
     *  * Note is null
     *
     * @param data game
     * @return result with validation errors
     */
    override fun add(data: Game): Result<Unit>

    /**
     * Updates game.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Position is null
     *  * Name is null
     *  * Name is empty string
     *  * URL to english Wikipedia page about game is null
     *  * URL to czech Wikipedia page about game is null
     *  * Count of media is null
     *  * Count of media isn't positive number
     *  * Format is null
     *  * Other data is null
     *  * Note is null
     *  * Game doesn't exist in data storage
     *
     * @param data new value of game
     * @return result with validation errors
     */
    override fun update(data: Game): Result<Unit>

    /**
     * Returns total count of media.
     *
     * @return result with total count of media
     */
    fun getTotalMediaCount(): Result<Int>

}
