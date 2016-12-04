package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.entity.Show;

/**
 * An interface represents facade for shows.
 *
 * @author Vladimir Hromada
 */
public interface ShowFacade {

    /**
     * Creates new data.
     */
    void newData();

    /**
     * Returns shows.
     *
     * @return shows
     */
    List<Show> getShows();

    /**
     * Returns show with ID or null if there isn't such show.
     *
     * @param id ID
     * @return show with ID or null if there isn't such show
     * @throws IllegalArgumentException if ID is null
     */
    Show getShow(Integer id);

    /**
     * Adds show. Sets new ID and position.
     *
     * @param show show
     * @throws IllegalArgumentException                                  if show is null
     *                                                                   or ID isn't null
     *                                                                   or czech name is null
     *                                                                   or czech name is empty string
     *                                                                   or original name is null
     *                                                                   or original name is empty string
     *                                                                   or URL to ČSFD page about show is null
     *                                                                   or IMDB code isn't -1 or between 1 and 9999999
     *                                                                   or URL to english Wikipedia page about show is null
     *                                                                   or URL to czech Wikipedia page about show is null
     *                                                                   or path to file with show picture is null
     *                                                                   or note is null
     *                                                                   or genres are null
     *                                                                   or genres contain null value
     *                                                                   or genre ID is null
     *                                                                   or genre name is null
     *                                                                   or genre name is empty string
     *                                                                   or genre doesn't exist in data storage
     */
    void add(Show show);

    /**
     * Updates show.
     *
     * @param show new value of show
     * @throws IllegalArgumentException                                  if show is null
     *                                                                   or ID is null
     *                                                                   or czech name is null
     *                                                                   or czech name is empty string
     *                                                                   or original name is null
     *                                                                   or original name is empty string
     *                                                                   or URL to ČSFD page about show is null
     *                                                                   or IMDB code isn't -1 or between 1 and 9999999
     *                                                                   or URL to english Wikipedia page about show is null
     *                                                                   or URL to czech Wikipedia page about show is null
     *                                                                   or path to file with show picture is null
     *                                                                   or note is null
     *                                                                   or genres are null
     *                                                                   or genres contain null value
     *                                                                   or genre ID is null
     *                                                                   or genre name is null
     *                                                                   or genre name is empty string
     *                                                                   or show doesn't exist in data storage
     *                                                                   or genre doesn't exist in data storage
     */
    void update(Show show);

    /**
     * Removes show.
     *
     * @param show show
     * @throws IllegalArgumentException                                  if show is null
     *                                                                   or ID is null
     *                                                                   or show doesn't exist in data storage
     */
    void remove(Show show);

    /**
     * Duplicates show.
     *
     * @param show show
     * @throws IllegalArgumentException                                  if show is null
     *                                                                   or ID is null
     *                                                                   or show doesn't exist in data storage
     */
    void duplicate(Show show);

    /**
     * Moves show in list one position up.
     *
     * @param show show
     * @throws IllegalArgumentException                                  if show is null
     *                                                                   or ID is null
     *                                                                   or show can't be moved up
     *                                                                   or show doesn't exist in data storage
     */
    void moveUp(Show show);

    /**
     * Moves show in list one position down.
     *
     * @param show show
     * @throws IllegalArgumentException                                  if show is null
     *                                                                   or ID is null
     *                                                                   or show can't be moved down
     *                                                                   or show doesn't exist in data storage
     */
    void moveDown(Show show);

    /**
     * Updates positions.
     */
    void updatePositions();

    /**
     * Returns total length of all shows.
     *
     * @return total length of all shows
     */
    Time getTotalLength();

    /**
     * Returns count of seasons from all shows.
     *
     * @return count of seasons from all shows
     */
    int getSeasonsCount();

    /**
     * Returns count of episodes from all shows.
     *
     * @return count of episodes from all shows
     */
    int getEpisodesCount();

}
