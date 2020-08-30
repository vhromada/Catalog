package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.common.facade.MovableParentFacade
import com.github.vhromada.common.result.Result

/**
 * An interface represents facade for genres.
 *
 * @author Vladimir Hromada
 */
interface GenreFacade : MovableParentFacade<Genre> {

    /**
     * Adds genre. Sets new ID and position.
     * <br></br>
     * Validation errors:
     *
     *  * ID isn't null
     *  * Position isn't null
     *  * Name is null
     *  * Name is empty string
     *
     * @param data genre
     * @return result with validation errors
     */
    override fun add(data: Genre): Result<Unit>

    /**
     * Updates genre.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Position is null
     *  * Name is null
     *  * Name is empty string
     *  * Genre doesn't exist in data storage
     *
     * @param data genre
     * @return result with validation errors
     */
    override fun update(data: Genre): Result<Unit>

}
