package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.entity.EpisodeTO;
import cz.vhromada.catalog.entity.SeasonTO;

/**
 * An interface represents facade for episodes.
 *
 * @author Vladimir Hromada
 */
public interface EpisodeFacade {

    /**
     * Returns TO for episode with ID or null if there isn't such TO for episode.
     *
     * @param id ID
     * @return TO for episode with ID or null if there isn't such TO for episode
     * @throws IllegalArgumentException if ID is null
     */
    EpisodeTO getEpisode(Integer id);

    /**
     * Adds TO for episode. Sets new ID and position.
     *
     * @param season  TO for season
     * @param episode TO for episode
     * @throws IllegalArgumentException                                  if TO for season is null
     *                                                                   or TO for episode is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if season ID is null
     *                                                                   or episode ID isn't null
     *                                                                   or number of episode isn't positive number
     *                                                                   or name is null
     *                                                                   or name is empty string
     *                                                                   or length of episode is negative value
     *                                                                   or note is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException TO for season doesn't exist in data storage
     */
    void add(SeasonTO season, EpisodeTO episode);

    /**
     * Updates TO for episode.
     *
     * @param episode new value of TO for episode
     * @throws IllegalArgumentException                                  if TO for episode is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or number of episode isn't positive number
     *                                                                   or name is null
     *                                                                   or name is empty string
     *                                                                   or length of episode is negative value
     *                                                                   or note is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if TO for episode doesn't exist in data storage
     */
    void update(EpisodeTO episode);

    /**
     * Removes TO for episode.
     *
     * @param episode TO for episode
     * @throws IllegalArgumentException                                  if TO for episode is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if TO for episode doesn't exist in data storage
     */
    void remove(EpisodeTO episode);

    /**
     * Duplicates TO for episode.
     *
     * @param episode TO for episode
     * @throws IllegalArgumentException                                  if TO for episode is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if TO for episode doesn't exist in data storage
     */
    void duplicate(EpisodeTO episode);

    /**
     * Moves TO for episode in list one position up.
     *
     * @param episode TO for episode
     * @throws IllegalArgumentException                                  if TO for episode is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or TO for episode can't be moved up
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if TO for episode doesn't exist in data storage
     */
    void moveUp(EpisodeTO episode);

    /**
     * Moves TO for episode in list one position down.
     *
     * @param episode TO for episode
     * @throws IllegalArgumentException                                  if TO for episode is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or TO for episode can't be moved down
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if TO for episode doesn't exist in data storage
     */
    void moveDown(EpisodeTO episode);

    /**
     * Returns list of TO for episode for specified TO for season.
     *
     * @param season TO for season
     * @return list of TO for episode for specified TO for season
     * @throws IllegalArgumentException                                  if TO for season is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if TO for season doesn't exist in data storage
     */
    List<EpisodeTO> findEpisodesBySeason(SeasonTO season);

}
