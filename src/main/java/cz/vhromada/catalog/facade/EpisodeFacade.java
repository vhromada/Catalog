package cz.vhromada.catalog.facade;

import cz.vhromada.catalog.entity.Episode;
import cz.vhromada.catalog.entity.Season;
import cz.vhromada.result.Result;

/**
 * An interface represents facade for episodes.
 *
 * @author Vladimir Hromada
 */
public interface EpisodeFacade extends CatalogChildFacade<Episode, Season> {

    /**
     * Adds episode. Sets new ID and position.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Season is null</li>
     * <li>Season ID is null</li>
     * <li>Season doesn't exist in data storage</li>
     * <li>Episode is null</li>
     * <li>Episode ID isn't null</li>
     * <li>Number of episode isn't positive number</li>
     * <li>Name is null</li>
     * <li>Name is empty string</li>
     * <li>Length of episode is negative value</li>
     * <li>Note is null</li>
     * </ul>
     *
     * @param season  season
     * @param episode episode
     * @return result with validation errors
     */
    Result<Void> add(Season season, Episode episode);

    /**
     * Updates episode.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Episode is null</li>
     * <li>ID is null</li>
     * <li>Number of episode isn't positive number</li>
     * <li>Name is null</li>
     * <li>Name is empty string</li>
     * <li>Length of episode is negative value</li>
     * <li>Note is null</li>
     * <li>Season doesn't exist in data storage</li>
     * </ul>
     *
     * @param episode new value of episode
     * @return result with validation errors
     */
    Result<Void> update(Episode episode);

}
