package cz.vhromada.catalog.service;

import java.util.List;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;

/**
 * An interface represents service for episodes.
 *
 * @author Vladimir Hromada
 */
public interface EpisodeService {

    /**
     * Returns episode with ID or null if there isn't such episode.
     *
     * @param id ID
     * @return episode with ID or null if there isn't such episode
     * @throws IllegalArgumentException if ID is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    Episode getEpisode(Integer id);

    /**
     * Adds episode. Sets new ID and position.
     *
     * @param episode episode
     * @throws IllegalArgumentException if episode is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    void add(Episode episode);

    /**
     * Updates episode.
     *
     * @param episode new value of episode
     * @throws IllegalArgumentException if episode is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    void update(Episode episode);

    /**
     * Removes episode.
     *
     * @param episode episode
     * @throws IllegalArgumentException if episode is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    void remove(Episode episode);

    /**
     * Duplicates episode.
     *
     * @param episode episode
     * @throws IllegalArgumentException if episode is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    void duplicate(Episode episode);

    /**
     * Moves episode in list one position up.
     *
     * @param episode episode
     * @throws IllegalArgumentException if episode is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    void moveUp(Episode episode);

    /**
     * Moves episode in list one position down.
     *
     * @param episode episode
     * @throws IllegalArgumentException if episode is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    void moveDown(Episode episode);

    /**
     * Returns true if episode exists.
     *
     * @param episode episode
     * @return true if episode exists
     * @throws IllegalArgumentException if episode is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    boolean exists(Episode episode);

    /**
     * Returns list of episode for specified season.
     *
     * @param season season
     * @return list of episode for specified season
     * @throws IllegalArgumentException if season is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error with working with DAO tier
     */
    List<Episode> findEpisodesBySeason(Season season);

    /**
     * Returns total length of episodes for specified season.
     *
     * @param season season
     * @return total length of episodes for specified season
     * @throws IllegalArgumentException if season is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error with working with DAO tier
     */
    Time getTotalLengthBySeason(Season season);

}
