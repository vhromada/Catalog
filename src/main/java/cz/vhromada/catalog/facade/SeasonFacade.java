package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.entity.SeasonTO;
import cz.vhromada.catalog.entity.ShowTO;

/**
 * An interface represents facade for shows.
 *
 * @author Vladimir Hromada
 */
public interface SeasonFacade {

    /**
     * Returns TO for season with ID or null if there isn't such TO for season.
     *
     * @param id ID
     * @return TO for season with ID or null if there isn't such TO for season
     * @throws IllegalArgumentException if ID is null
     */
    SeasonTO getSeason(Integer id);

    /**
     * Adds TO for season. Sets new ID and position.
     *
     * @param show   TO for show
     * @param season TO for season
     * @throws IllegalArgumentException                                  if TO for show is null
     *                                                                   or TO for season is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if show ID is null
     *                                                                   or season ID isn't null
     *                                                                   or number of season isn't positive number
     *                                                                   or starting year isn't between 1940 and current year
     *                                                                   or ending year isn't between 1940 and current year
     *                                                                   or starting year is greater than ending year
     *                                                                   or language is null
     *                                                                   or subtitles are null
     *                                                                   or subtitles contain null value
     *                                                                   or note is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if TO for show doesn't exist in data storage
     */
    void add(ShowTO show, SeasonTO season);

    /**
     * Updates TO for season.
     *
     * @param season new value of TO for season
     * @throws IllegalArgumentException                                  if TO for season is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or number of season isn't positive number
     *                                                                   or starting year isn't between 1940 and current year
     *                                                                   or ending year isn't between 1940 and current year
     *                                                                   or starting year is greater than ending year
     *                                                                   or language is null
     *                                                                   or subtitles are null
     *                                                                   or subtitles contain null value
     *                                                                   or note is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if TO for season doesn't exist in data storage
     */
    void update(SeasonTO season);

    /**
     * Removes TO for season.
     *
     * @param season TO for season
     * @throws IllegalArgumentException                                  if TO for season is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if TO for season doesn't exist in data storage
     */
    void remove(SeasonTO season);

    /**
     * Duplicates TO for season.
     *
     * @param season TO for season
     * @throws IllegalArgumentException                                  if TO for season is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if TO for season doesn't exist in data storage
     */
    void duplicate(SeasonTO season);

    /**
     * Moves TO for season in list one position up.
     *
     * @param season TO for season
     * @throws IllegalArgumentException                                  if TO for season is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or TO for season can't be moved up
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if TO for season doesn't exist in data storage
     */
    void moveUp(SeasonTO season);

    /**
     * Moves TO for season in list one position down.
     *
     * @param season TO for season
     * @throws IllegalArgumentException                                  if TO for season is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or TO for season can't be moved down
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if TO for season doesn't exist in data storage
     */
    void moveDown(SeasonTO season);

    /**
     * Returns list of TO for season for specified TO for show.
     *
     * @param show TO for show
     * @return list of TO for seasons for specified TO for show
     * @throws IllegalArgumentException                                  if TO for show is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if TO for show doesn't exist in data storage
     */
    List<SeasonTO> findSeasonsByShow(ShowTO show);

}
