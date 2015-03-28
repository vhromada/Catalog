package cz.vhromada.catalog.service;

import java.util.List;

import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Show;

/**
 * An interface represents service for shows.
 *
 * @author Vladimir Hromada
 */
public interface SeasonService {

    /**
     * Returns season with ID or null if there isn't such season.
     *
     * @param id ID
     * @return season with ID or null if there isn't such season
     * @throws IllegalArgumentException                                         if ID is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    Season getSeason(Integer id);

    /**
     * Adds season. Sets new ID and position.
     *
     * @param season season
     * @throws IllegalArgumentException                                         if season is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void add(Season season);

    /**
     * Updates season.
     *
     * @param season new value of season
     * @throws IllegalArgumentException                                         if season is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void update(Season season);

    /**
     * Removes season.
     *
     * @param season season
     * @throws IllegalArgumentException                                         if season is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void remove(Season season);

    /**
     * Duplicates season.
     *
     * @param season season
     * @throws IllegalArgumentException                                         if season is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void duplicate(Season season);

    /**
     * Moves season in list one position up.
     *
     * @param season season
     * @throws IllegalArgumentException                                         if season is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void moveUp(Season season);

    /**
     * Moves season in list one position down.
     *
     * @param season season
     * @throws IllegalArgumentException                                         if season is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void moveDown(Season season);

    /**
     * Returns true if season exists.
     *
     * @param season season
     * @return true if season exists
     * @throws IllegalArgumentException                                         if season is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    boolean exists(Season season);

    /**
     * Returns list of season for specified show.
     *
     * @param show show
     * @return list of seasons for specified show
     * @throws IllegalArgumentException                                         if show is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    List<Season> findSeasonsByShow(Show show);

}
