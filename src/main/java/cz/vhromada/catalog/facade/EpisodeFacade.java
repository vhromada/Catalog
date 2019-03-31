package cz.vhromada.catalog.facade;

import cz.vhromada.catalog.entity.Episode;
import cz.vhromada.catalog.entity.Season;
import cz.vhromada.common.facade.MovableChildFacade;
import cz.vhromada.validation.result.Result;

/**
 * An interface represents facade for episodes.
 *
 * @author Vladimir Hromada
 */
public interface EpisodeFacade extends MovableChildFacade<Episode, Season> {

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
     * <li>Episode position isn't null</li>
     * <li>Number of episode isn't positive number</li>
     * <li>Name is null</li>
     * <li>Name is empty string</li>
     * <li>Length of episode is negative value</li>
     * <li>Note is null</li>
     * </ul>
     *
     * @param parent season
     * @param data   episode
     * @return result with validation errors
     */
    @Override
    Result<Void> add(Season parent, Episode data);

    /**
     * Updates episode.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Episode is null</li>
     * <li>ID is null</li>
     * <li>Position is null</li>
     * <li>Number of episode isn't positive number</li>
     * <li>Name is null</li>
     * <li>Name is empty string</li>
     * <li>Length of episode is negative value</li>
     * <li>Note is null</li>
     * <li>Season doesn't exist in data storage</li>
     * </ul>
     *
     * @param data new value of episode
     * @return result with validation errors
     */
    @Override
    Result<Void> update(Episode data);

}
