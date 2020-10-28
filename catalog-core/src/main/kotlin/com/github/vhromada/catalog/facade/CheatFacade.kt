package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Cheat
import com.github.vhromada.catalog.entity.Game
import com.github.vhromada.common.facade.MovableChildFacade
import com.github.vhromada.common.result.Result

/**
 * An interface represents facade for cheats.
 *
 * @author Vladimir Hromada
 */
interface CheatFacade : MovableChildFacade<Cheat, Game> {

    /**
     * Adds cheat. Sets new ID.
     * <br></br>
     * Validation errors:
     *
     *  * Game ID is null
     *  * Game doesn't exist in data storage
     *  * Cheat ID isn't null
     *  * Setting for game is null
     *  * Setting for cheat is null
     *  * Cheat's data are null
     *  * Cheat's data contain null value
     *  * Action is null
     *  * Action is empty string
     *  * Description is null
     *  * Description is empty string
     *  * Game has already cheat in data storage
     *
     * @param parent  game
     * @param data    cheat
     * @return result with validation errors
     */
    override fun add(parent: Game, data: Cheat): Result<Unit>

    /**
     * Updates cheat.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Setting for game is null
     *  * Setting for cheat is null
     *  * Cheat's data are null
     *  * Cheat's data contain null value
     *  * Action is null
     *  * Action is empty string
     *  * Description is null
     *  * Description is empty string
     *  * Cheat doesn't exist in data storage
     *
     * @param data new value of cheat
     * @return result with validation errors
     */
    override fun update(data: Cheat): Result<Unit>

    /**
     * Duplicates cheat.
     * <br></br>
     * Validation errors:
     *
     *  * Cheat can't be duplicate
     *
     * @param data cheat
     * @return result with validation errors
     */
    override fun duplicate(data: Cheat): Result<Unit>

    /**
     * Moves cheat one position up.
     * <br></br>
     * Validation errors:
     *
     *  * Cheat can't be moved up
     *
     * @param data cheat
     * @return result with validation errors
     */
    override fun moveUp(data: Cheat): Result<Unit>

    /**
     * Moves cheat one position down.
     * <br></br>
     * Validation errors:
     *
     *  * Cheat can't be moved down
     *
     * @param data cheat
     * @return result with validation errors
     */
    override fun moveDown(data: Cheat): Result<Unit>

}
