package cz.vhromada.catalog.facade

import cz.vhromada.catalog.entity.Picture
import cz.vhromada.common.facade.MovableParentFacade
import cz.vhromada.validation.result.Result

/**
 * An interface represents facade for pictures.
 *
 * @author Vladimir Hromada
 */
interface PictureFacade : MovableParentFacade<Picture> {

    /**
     * Adds picture. Sets new ID and position.
     * <br></br>
     * Validation errors:
     *
     *  * ID isn't null
     *  * Position isn't null
     *  * Content is null
     *
     * @param data picture
     * @return result with validation errors
     */
    override fun add(data: Picture): Result<Unit>

    /**
     * Updates picture.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Position is null
     *  * Content is null
     *  * Picture doesn't exist in data storage
     *
     * @param data new value of picture
     * @return result with validation errors
     */
    override fun update(data: Picture): Result<Unit>

}
