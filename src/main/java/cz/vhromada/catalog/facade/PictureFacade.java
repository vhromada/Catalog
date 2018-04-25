package cz.vhromada.catalog.facade;

import cz.vhromada.catalog.entity.Picture;
import cz.vhromada.common.facade.MovableParentFacade;
import cz.vhromada.result.Result;

/**
 * An interface represents facade for pictures.
 *
 * @author Vladimir Hromada
 */
public interface PictureFacade extends MovableParentFacade<Picture> {

    /**
     * Adds picture. Sets new ID and position.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Picture is null</li>
     * <li>ID isn't null</li>
     * <li>Content is null</li>
     * </ul>
     *
     * @param data picture
     * @return result with validation errors
     */
    @Override
    Result<Void> add(Picture data);

    /**
     * Updates picture.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Picture is null</li>
     * <li>ID is null</li>
     * <li>Content is null</li>
     * <li>Picture doesn't exist in data storage</li>
     * </ul>
     *
     * @param data new value of picture
     * @return result with validation errors
     */
    @Override
    Result<Void> update(Picture data);

}
