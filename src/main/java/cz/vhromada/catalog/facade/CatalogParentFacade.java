package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.result.Result;

/**
 * An interface represents facade for catalog for parent data.
 *
 * @param <T> type of data
 * @author Vladimir Hromada
 */
public interface CatalogParentFacade<T extends Movable> extends CatalogFacade<T> {

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
    Result<List<T>> getAll();

    /**
     * Adds data. Sets new ID and position.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Data is null</li>
     * <li>ID isn't null</li>
     * <li>Deep data validation errors</li>
     * </ul>
     *
     * @param data data
     * @return result with validation errors
     */
    Result<Void> add(T data);

    /**
     * Updates positions.
     *
     * @return result
     */
    Result<Void> updatePositions();

}
