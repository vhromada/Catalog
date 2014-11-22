package cz.vhromada.catalog.dao;

import java.util.List;

import cz.vhromada.catalog.dao.entities.Serie;

/**
 * An interface represents DAO for series.
 *
 * @author Vladimir Hromada
 */
public interface SerieDAO {

    /**
     * Returns list of series.
     *
     * @return list of series
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException
     *          if there was error with working with data storage
     */
    List<Serie> getSeries();

    /**
     * Returns serie with ID or null if there isn't such serie.
     *
     * @param id ID
     * @return serie with ID or null if there isn't such serie
     * @throws IllegalArgumentException if ID is null
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException
     *                                  if there was error with working with data storage
     */
    Serie getSerie(Integer id);

    /**
     * Adds serie. Sets new ID and position.
     *
     * @param serie serie
     * @throws IllegalArgumentException if serie is null
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException
     *                                  if there was error with working with data storage
     */
    void add(Serie serie);

    /**
     * Updates serie.
     *
     * @param serie serie
     * @throws IllegalArgumentException if serie is null
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException
     *                                  if there was error with working with data storage
     */
    void update(Serie serie);

    /**
     * Removes serie.
     *
     * @param serie serie
     * @throws IllegalArgumentException if serie is null
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException
     *                                  if there was error with working with data storage
     */
    void remove(Serie serie);

}
