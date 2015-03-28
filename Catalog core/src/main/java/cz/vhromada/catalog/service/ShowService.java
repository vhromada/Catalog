package cz.vhromada.catalog.service;

import java.util.List;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.entities.Show;

/**
 * An interface represents service for shows.
 *
 * @author Vladimir Hromada
 */
public interface ShowService {

    /**
     * Creates new data.
     *
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void newData();

    /**
     * Returns list of show.
     *
     * @return list of show
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    List<Show> getShows();

    /**
     * Returns show with ID or null if there isn't such show.
     *
     * @param id ID
     * @return show with ID or null if there isn't such show
     * @throws IllegalArgumentException                                         if ID is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    Show getShow(Integer id);

    /**
     * Adds show. Sets new ID and position.
     *
     * @param show show
     * @throws IllegalArgumentException                                         if show is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void add(Show show);

    /**
     * Updates show.
     *
     * @param show new value of show
     * @throws IllegalArgumentException                                         if show is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void update(Show show);

    /**
     * Removes show.
     *
     * @param show show
     * @throws IllegalArgumentException                                         if show is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void remove(Show show);

    /**
     * Duplicates show.
     *
     * @param show show
     * @throws IllegalArgumentException                                         if show is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void duplicate(Show show);

    /**
     * Moves show in list one position up.
     *
     * @param show show
     * @throws IllegalArgumentException                                         if show is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void moveUp(Show show);

    /**
     * Moves show in list one position down.
     *
     * @param show show
     * @throws IllegalArgumentException                                         if show is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void moveDown(Show show);

    /**
     * Returns true if show exists.
     *
     * @param show show
     * @return true if show exists
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    boolean exists(Show show);

    /**
     * Updates positions.
     *
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void updatePositions();

    /**
     * Returns total length of all shows.
     *
     * @return total length of all shows
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    Time getTotalLength();

    /**
     * Returns count of seasons from all shows.
     *
     * @return count of seasons from all shows
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    int getSeasonsCount();

    /**
     * Returns count of episodes from all shows.
     *
     * @return count of episodes from all shows
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    int getEpisodesCount();

}
