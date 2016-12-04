package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.entity.Show;

/**
 * An interface represents facade for shows.
 *
 * @author Vladimir Hromada
 */
public interface SeasonFacade {

    /**
     * Returns season with ID or null if there isn't such season.
     *
     * @param id ID
     * @return season with ID or null if there isn't such season
     * @throws IllegalArgumentException if ID is null
     */
    Season getSeason(Integer id);

    /**
     * Adds season. Sets new ID and position.
     *
     * @param show   show
     * @param season season
     * @throws IllegalArgumentException                                  if show is null
     *                                                                   or season is null
     *                                                                   or show ID is null
     *                                                                   or season ID isn't null
     *                                                                   or number of season isn't positive number
     *                                                                   or starting year isn't between 1940 and current year
     *                                                                   or ending year isn't between 1940 and current year
     *                                                                   or starting year is greater than ending year
     *                                                                   or language is null
     *                                                                   or subtitles are null
     *                                                                   or subtitles contain null value
     *                                                                   or note is null
     *                                                                   or show doesn't exist in data storage
     */
    void add(Show show, Season season);

    /**
     * Updates season.
     *
     * @param season new value of season
     * @throws IllegalArgumentException                                  if season is null
     *                                                                   or ID is null
     *                                                                   or number of season isn't positive number
     *                                                                   or starting year isn't between 1940 and current year
     *                                                                   or ending year isn't between 1940 and current year
     *                                                                   or starting year is greater than ending year
     *                                                                   or language is null
     *                                                                   or subtitles are null
     *                                                                   or subtitles contain null value
     *                                                                   or note is null
     *                                                                   or season doesn't exist in data storage
     */
    void update(Season season);

    /**
     * Removes season.
     *
     * @param season season
     * @throws IllegalArgumentException                                  if season is null
     *                                                                   or ID is null
     *                                                                   or season doesn't exist in data storage
     */
    void remove(Season season);

    /**
     * Duplicates season.
     *
     * @param season season
     * @throws IllegalArgumentException                                  if season is null
     *                                                                   or ID is null
     *                                                                   or season doesn't exist in data storage
     */
    void duplicate(Season season);

    /**
     * Moves season in list one position up.
     *
     * @param season season
     * @throws IllegalArgumentException                                  if season is null
     *                                                                   or ID is null
     *                                                                   or season can't be moved up
     *                                                                   or season doesn't exist in data storage
     */
    void moveUp(Season season);

    /**
     * Moves season in list one position down.
     *
     * @param season season
     * @throws IllegalArgumentException                                  if season is null
     *                                                                   or ID is null
     *                                                                   or season can't be moved down
     *                                                                   or season doesn't exist in data storage
     */
    void moveDown(Season season);

    /**
     * Returns seasons for specified show.
     *
     * @param show show
     * @return seasons for specified show
     * @throws IllegalArgumentException                                  if show is null
     *                                                                   or ID is null
     *                                                                   or show doesn't exist in data storage
     */
    List<Season> findSeasonsByShow(Show show);

}
