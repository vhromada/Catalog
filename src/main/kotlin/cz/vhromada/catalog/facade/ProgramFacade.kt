package cz.vhromada.catalog.facade

import cz.vhromada.catalog.entity.Program
import cz.vhromada.common.facade.MovableParentFacade
import cz.vhromada.validation.result.Result

/**
 * An interface represents facade for programs.
 *
 * @author Vladimir Hromada
 */
interface ProgramFacade : MovableParentFacade<Program> {

    /**
     * Adds program. Sets new ID and position.
     * <br></br>
     * Validation errors:
     *
     *  * ID isn't null
     *  * Position isn't null
     *  * Name is null
     *  * Name is empty string
     *  * URL to english Wikipedia page about program is null
     *  * URL to czech Wikipedia page about program is null
     *  * Count of media is null
     *  * Count of media isn't positive number
     *  * Other data is null
     *  * Note is null
     *
     * @param data program
     * @return result with validation errors
     */
    override fun add(data: Program): Result<Unit>

    /**
     * Updates program.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Position is null
     *  * Name is null
     *  * Name is empty string
     *  * URL to english Wikipedia page about program is null
     *  * URL to czech Wikipedia page about program is null
     *  * Count of media is null
     *  * Count of media isn't positive number
     *  * Other data is null
     *  * Note is null
     *  * Program doesn't exist in data storage
     *
     * @param data new value of program
     * @return result with validation errors
     */
    override fun update(data: Program): Result<Unit>

    /**
     * Returns total count of media.
     *
     * @return result with total count of media
     */
    fun getTotalMediaCount(): Result<Int>

}
