package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.result.Result;

/**
 * An interface represents facade for genres.
 *
 * @author Vladimir Hromada
 */
public interface GenreFacade {

    /**
     * Creates new data.
     *
     * @return result
     */
    Result<Void> newData();

    /**
     * Returns list of data.
     *
     * @return result with list of data
     */
    Result<List<Genre>> getAll();

    /**
     * Returns data with ID or null if there aren't such data.
     * <br>
     * Validation errors:
     * <ul>
     * <li>ID is null</li>
     * </ul>
     *
     * @param id ID
     * @return result with data or validation errors
     */
    Result<Genre> get(Integer id);

    /**
     * Adds data. Sets new ID and position.
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

    /**
     * Removes genre.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Genre is null</li>
     * <li>ID is null</li>
     * <li>Genre doesn't exist in data storage</li>
     * </ul>
     *
     * @param genre genre
     * @return result with validation errors
     */
    Result<Void> remove(Genre genre);

    /**
     * Duplicates genre.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Genre is null</li>
     * <li>ID is null</li>
     * <li>Genre doesn't exist in data storage</li>
     * </ul>
     *
     * @param genre genre
     * @return result with validation errors
     */
    Result<Void> duplicate(Genre genre);

    /**
     * Moves genre in list one position up.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Genre is null</li>
     * <li>ID is null</li>
     * <li>Genre can't be moved up</li>
     * <li>Genre doesn't exist in data storage</li>
     * </ul>
     *
     * @param genre genre
     * @return result with validation errors
     */
    Result<Void> moveUp(Genre genre);

    /**
     * Moves genre in list one position down.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Genre is null</li>
     * <li>ID is null</li>
     * <li>Genre can't be moved up</li>
     * <li>Genre doesn't exist in data storage</li>
     * </ul>
     *
     * @param genre genre
     * @return result with validation errors
     */
    Result<Void> moveDown(Genre genre);

    /**
     * Updates positions.
     *
     * @return result
     */
    Result<Void> updatePositions();

}
