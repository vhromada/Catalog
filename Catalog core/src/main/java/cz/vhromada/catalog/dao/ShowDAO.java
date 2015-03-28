package cz.vhromada.catalog.dao;

import java.util.List;

import cz.vhromada.catalog.dao.entities.Show;

/**
 * An interface represents DAO for shows.
 *
 * @author Vladimir Hromada
 */
public interface ShowDAO {

    /**
     * Returns list of shows.
     *
     * @return list of shows
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException if there was error with working with data storage
     */
    List<Show> getShows();

    /**
     * Returns show with ID or null if there isn't such show.
     *
     * @param id ID
     * @return show with ID or null if there isn't such show
     * @throws IllegalArgumentException                                if ID is null
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException if there was error with working with data storage
     */
    Show getShow(Integer id);

    /**
     * Adds show. Sets new ID and position.
     *
     * @param show show
     * @throws IllegalArgumentException                                if show is null
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException if there was error with working with data storage
     */
    void add(Show show);

    /**
     * Updates show.
     *
     * @param show show
     * @throws IllegalArgumentException                                if show is null
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException if there was error with working with data storage
     */
    void update(Show show);

    /**
     * Removes show.
     *
     * @param show show
     * @throws IllegalArgumentException                                if show is null
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException if there was error with working with data storage
     */
    void remove(Show show);

}
