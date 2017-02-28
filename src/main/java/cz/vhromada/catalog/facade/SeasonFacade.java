package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.result.Result;

/**
 * An interface represents facade for seasons.
 *
 * @author Vladimir Hromada
 */
public interface SeasonFacade {

    /**
     * Returns season with ID or null if there isn't such season.
     * <br>
     * Validation errors:
     * <ul>
     * <li>ID is null</li>
     * </ul>
     *
     * @param id ID
     * @return result with season or validation errors
     */
    Result<Season> get(Integer id);

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
     * @param show   show
     * @param season season
     * @return result with validation errors
     */
    Result<Void> add(Show show, Season season);

    /**
     * Updates season.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Season is null</li>
     * <li>ID is null</li>
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
     * @param season new value of season
     * @return result with validation errors
     */
    Result<Void> update(Season season);

    /**
     * Removes season.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Season is null</li>
     * <li>ID is null</li>
     * <li>Season doesn't exist in data storage</li>
     *
     * @param season season
     * @return result with validation errors
     */
    Result<Void> remove(Season season);

    /**
     * Duplicates season.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Season is null</li>
     * <li>ID is null</li>
     * <li>Season doesn't exist in data storage</li>
     *
     * @param season season
     * @return result with validation errors
     */
    Result<Void> duplicate(Season season);

    /**
     * Moves season in list one position up.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Season is null</li>
     * <li>ID is null</li>
     * <li>Season can't be moved up</li>
     * <li>Season doesn't exist in data storage</li>
     *
     * @param season season
     * @return result with validation errors
     */
    Result<Void> moveUp(Season season);

    /**
     * Moves season in list one position down.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Season is null</li>
     * <li>ID is null</li>
     * <li>Season can't be moved down</li>
     * <li>Season doesn't exist in data storage</li>
     *
     * @param season season
     * @return result with validation errors
     */
    Result<Void> moveDown(Season season);

    /**
     * Returns seasons for specified show.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Show is null</li>
     * <li>ID is null</li>
     * <li>Show doesn't exist in data storage</li>
     *
     * @param show show
     * @return result with seasons or validation errors
     */
    Result<List<Season>> find(Show show);

}
