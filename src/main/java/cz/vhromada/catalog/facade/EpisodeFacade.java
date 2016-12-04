package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.entity.Episode;
import cz.vhromada.catalog.entity.Season;

/**
 * An interface represents facade for episodes.
 *
 * @author Vladimir Hromada
 */
public interface EpisodeFacade {

    /**
     * Returns episode with ID or null if there isn't such episode.
     *
     * @param id ID
     * @return episode with ID or null if there isn't such episode
     * @throws IllegalArgumentException if ID is null
     */
    Episode getEpisode(Integer id);

    /**
     * Adds episode. Sets new ID and position.
     *
     * @param season  season
     * @param episode episode
     * @throws IllegalArgumentException                                  if season is null
     *                                                                   or episode is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if season ID is null
     *                                                                   or episode ID isn't null
     *                                                                   or number of episode isn't positive number
     *                                                                   or name is null
     *                                                                   or name is empty string
     *                                                                   or length of episode is negative value
     *                                                                   or note is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException season doesn't exist in data storage
     */
    void add(Season season, Episode episode);

    /**
     * Updates episode.
     *
     * @param episode new value of episode
     * @throws IllegalArgumentException                                  if episode is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or number of episode isn't positive number
     *                                                                   or name is null
     *                                                                   or name is empty string
     *                                                                   or length of episode is negative value
     *                                                                   or note is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if episode doesn't exist in data storage
     */
    void update(Episode episode);

    /**
     * Removes episode.
     *
     * @param episode episode
     * @throws IllegalArgumentException                                  if episode is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if episode doesn't exist in data storage
     */
    void remove(Episode episode);

    /**
     * Duplicates episode.
     *
     * @param episode episode
     * @throws IllegalArgumentException                                  if episode is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if episode doesn't exist in data storage
     */
    void duplicate(Episode episode);

    /**
     * Moves episode in list one position up.
     *
     * @param episode episode
     * @throws IllegalArgumentException                                  if episode is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or episode can't be moved up
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if episode doesn't exist in data storage
     */
    void moveUp(Episode episode);

    /**
     * Moves episode in list one position down.
     *
     * @param episode episode
     * @throws IllegalArgumentException                                  if episode is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or episode can't be moved down
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if episode doesn't exist in data storage
     */
    void moveDown(Episode episode);

    /**
     * Returns episodes for specified season.
     *
     * @param season season
     * @return episodes for specified season
     * @throws IllegalArgumentException                                  if season is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if season doesn't exist in data storage
     */
    List<Episode> findEpisodesBySeason(Season season);

}
