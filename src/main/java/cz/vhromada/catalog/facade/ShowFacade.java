package cz.vhromada.catalog.facade;

import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.result.Result;

/**
 * An interface represents facade for shows.
 *
 * @author Vladimir Hromada
 */
public interface ShowFacade extends CatalogParentFacade<Show> {

    /**
     * Adds show. Sets new ID and position.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Show is null</li>
     * <li>ID isn't null</li>
     * <li>Czech name is null</li>
     * <li>Czech name is empty string</li>
     * <li>Original name is null</li>
     * <li>Original name is empty string</li>
     * <li>URL to ČSFD page about show is null</li>
     * <li>IMDB code isn't -1 or between 1 and 9999999</li>
     * <li>URL to english Wikipedia page about show is null</li>
     * <li>URL to czech Wikipedia page about show is null</li>
     * <li>Note is null</li>
     * <li>Picture doesn't exist</li>
     * <li>Genres are null</li>
     * <li>Genres contain null value</li>
     * <li>Genre ID is null</li>
     * <li>Genre name is null</li>
     * <li>Genre name is empty string</li>
     * <li>Genre doesn't exist</li>
     * </ul>
     *
     * @param data show
     * @return result with validation errors
     */
    @Override
    Result<Void> add(Show data);

    /**
     * Updates show.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Show is null</li>
     * <li>ID is null</li>
     * <li>Czech name is null</li>
     * <li>Czech name is empty string</li>
     * <li>Original name is null</li>
     * <li>Original name is empty string</li>
     * <li>URL to ČSFD page about show is null</li>
     * <li>IMDB code isn't -1 or between 1 and 9999999</li>
     * <li>URL to english Wikipedia page about show is null</li>
     * <li>URL to czech Wikipedia page about show is null</li>
     * <li>Note is null</li>
     * <li>Picture doesn't exist</li>
     * <li>Genres are null</li>
     * <li>Genres contain null value</li>
     * <li>Genre ID is null</li>
     * <li>Genre name is null</li>
     * <li>Genre name is empty string</li>
     * <li>Genre doesn't exist</li>
     * <li>Show doesn't exist in data storage</li>
     * </ul>
     *
     * @param data new value of show
     * @return result with validation errors
     */
    @Override
    Result<Void> update(Show data);

    /**
     * Returns total length of all shows.
     *
     * @return result with total length of all shows
     */
    Result<Time> getTotalLength();

    /**
     * Returns count of seasons from all shows.
     *
     * @return result with count of seasons from all shows
     */
    Result<Integer> getSeasonsCount();

    /**
     * Returns count of episodes from all shows.
     *
     * @return result with count of episodes from all shows
     */
    Result<Integer> getEpisodesCount();

}
