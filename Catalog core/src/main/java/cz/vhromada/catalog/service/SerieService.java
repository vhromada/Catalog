package cz.vhromada.catalog.service;

import java.util.List;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.entities.Serie;

/**
 * An interface represents service for series.
 *
 * @author Vladimir Hromada
 */
public interface SerieService {

    /**
     * Creates new data.
     *
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *          if there was error in working with DAO tier
     */
    void newData();

    /**
     * Returns list of serie.
     *
     * @return list of serie
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *          if there was error in working with DAO tier
     */
    List<Serie> getSeries();

    /**
     * Returns serie with ID or null if there isn't such serie.
     *
     * @param id ID
     * @return serie with ID or null if there isn't such serie
     * @throws IllegalArgumentException if ID is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    Serie getSerie(Integer id);

    /**
     * Adds serie. Sets new ID and position.
     *
     * @param serie serie
     * @throws IllegalArgumentException if serie is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    void add(Serie serie);

    /**
     * Updates serie.
     *
     * @param serie new value of serie
     * @throws IllegalArgumentException if serie is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    void update(Serie serie);

    /**
     * Removes serie.
     *
     * @param serie serie
     * @throws IllegalArgumentException if serie is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    void remove(Serie serie);

    /**
     * Duplicates serie.
     *
     * @param serie serie
     * @throws IllegalArgumentException if serie is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    void duplicate(Serie serie);

    /**
     * Moves serie in list one position up.
     *
     * @param serie serie
     * @throws IllegalArgumentException if serie is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    void moveUp(Serie serie);

    /**
     * Moves serie in list one position down.
     *
     * @param serie serie
     * @throws IllegalArgumentException if serie is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    void moveDown(Serie serie);

    /**
     * Returns true if serie exists.
     *
     * @param serie serie
     * @return true if serie exists
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *          if there was error in working with DAO tier
     */
    boolean exists(Serie serie);

    /**
     * Updates positions.
     *
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *          if there was error in working with DAO tier
     */
    void updatePositions();

    /**
     * Returns total length of all series.
     *
     * @return total length of all series
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *          if there was error in working with DAO tier
     */
    Time getTotalLength();

    /**
     * Returns count of seasons from all series.
     *
     * @return count of seasons from all series
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *          if there was error in working with DAO tier
     */
    int getSeasonsCount();

    /**
     * Returns count of episodes from all series.
     *
     * @return count of episodes from all series
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *          if there was error in working with DAO tier
     */
    int getEpisodesCount();

}
