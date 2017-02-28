package cz.vhromada.catalog.facade;

import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.result.Result;

/**
 * An interface represents facade for genres.
 *
 * @author Vladimir Hromada
 */
public interface GenreFacade extends CatalogParentFacade<Genre> {

    /**
     * Adds genre. Sets new ID and position.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Genre is null</li>
     * <li>ID isn't null</li>
     * <li>Name is null</li>
     * <li>Name is empty string</li>
     * </ul>
     *
     * @param genre genre
     * @return result with validation errors
     */
    Result<Void> add(Genre genre);

    /**
     * Updates genre.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Genre is null</li>
     * <li>ID is null</li>
     * <li>Name is null</li>
     * <li>Name is empty string</li>
     * <li>Genre doesn't exist in data storage</li>
     * </ul>
     *
     * @param genre genre
     * @return result with validation errors
     */
    Result<Void> update(Genre genre);

}
