package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Cheat
import com.github.vhromada.catalog.entity.CheatData
import com.github.vhromada.common.facade.MovableChildFacade
import com.github.vhromada.common.result.Result

/**
 * An interface represents facade for cheat's data.
 *
 * @author Vladimir Hromada
 */
interface CheatDataFacade : MovableChildFacade<CheatData, Cheat> {

    /**
     * Adds cheat's data. Sets new ID and position.
     * <br></br>
     * Validation errors:
     *
     *  * Cheat ID is null
     *  * Cheat doesn't exist in data storage
     *  * Cheat's data ID isn't null
     *  * Cheat's data position isn't null
     *  * Action is null
     *  * Action is empty string
     *  * Description is null
     *  * Description is empty string
     *
     * @param parent cheat
     * @param data   cheat's data
     * @return result with validation errors
     */
    override fun add(parent: Cheat, data: CheatData): Result<Unit>

    /**
     * Updates cheat's data.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Position is null
     *  * Action is null
     *  * Action is empty string
     *  * Description is null
     *  * Description is empty string
     *  * Cheat's data doesn't exist in data storage
     *
     * @param data new value of cheat's data
     * @return result with validation errors
     */
    override fun update(data: CheatData): Result<Unit>

}
