package cz.vhromada.catalog.dao;

import java.util.List;

import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Serie;

/**
 * An interface represents DAO for seasons.
 *
 * @author Vladimir Hromada
 */
public interface SeasonDAO {

    /**
     * Returns season with ID or null if there isn't such season.
     *
     * @param id ID
     * @return season with ID or null if there isn't such season
     * @throws IllegalArgumentException                                if ID is null
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException if there was error with working with data storage
     */
    Season getSeason(Integer id);

    /**
     * Adds season. Sets new ID and position.
     *
     * @param season season
     * @throws IllegalArgumentException                                if season is null
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException if there was error with working with data storage
     */
    void add(Season season);

    /**
     * Updates season.
     *
     * @param season season
     * @throws IllegalArgumentException                                if season is null
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException if there was error with working with data storage
     */
    void update(Season season);

    /**
     * Removes season.
     *
     * @param season season
     * @throws IllegalArgumentException                                if season is null
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException if there was error with working with data storage
     */
    void remove(Season season);

    /**
     * Returns list of seasons for specified serie.
     *
     * @param serie serie
     * @return list of seasons for specified serie
     * @throws IllegalArgumentException                                if serie is null
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException if there was error with working with data storage
     */
    List<Season> findSeasonsBySerie(Serie serie);

}
