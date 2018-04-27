package cz.vhromada.catalog.facade;

import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.common.facade.MovableChildFacade;
import cz.vhromada.result.Result;

/**
 * An interface represents facade for seasons.
 *
 * @author Vladimir Hromada
 */
public interface SeasonFacade extends MovableChildFacade<Season, Show> {

    /**
     * Adds season. Sets new ID and position.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Show is null</li>
     * <li>Show ID is null</li>
     * <li>Show doesn't exist in data storage</li>
     * <li>Season is null</li>
     * <li>Season ID isn't null</li>
     * <li>Season position isn't null</li>
     * <li>Number of season isn't positive number</li>
     * <li>Starting year isn't between 1940 and current year</li>
     * <li>Ending year isn't between 1940 and current year</li>
     * <li>Starting year is greater than ending year</li>
     * <li>Language is null</li>
     * <li>Subtitles are null</li>
     * <li>Subtitles contain null value</li>
     * <li>Note is null</li>
     * </ul>
     *
     * @param parent show
     * @param data   season
     * @return result with validation errors
     */
    @Override
    Result<Void> add(Show parent, Season data);

    /**
     * Updates season.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Season is null</li>
     * <li>ID is null</li>
     * <li>Position is null</li>
     * <li>Number of season isn't positive number</li>
     * <li>Starting year isn't between 1940 and current year</li>
     * <li>Ending year isn't between 1940 and current year</li>
     * <li>Starting year is greater than ending year</li>
     * <li>Language is null</li>
     * <li>Subtitles are null</li>
     * <li>Subtitles contain null value</li>
     * <li>Note is null</li>
     * <li>Season doesn't exist in data storage</li>
     * </ul>
     *
     * @param data new value of season
     * @return result with validation errors
     */
    @Override
    Result<Void> update(Season data);

}
