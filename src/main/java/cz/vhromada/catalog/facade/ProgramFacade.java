package cz.vhromada.catalog.facade;

import cz.vhromada.catalog.entity.Program;
import cz.vhromada.result.Result;

/**
 * An interface represents facade for programs.
 *
 * @author Vladimir Hromada
 */
public interface ProgramFacade extends CatalogParentFacade<Program> {

    /**
     * Adds program. Sets new ID and position.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Program is null</li>
     * <li>ID isn't null</li>
     * <li>Name is null</li>
     * <li>Name is empty string</li>
     * <li>URL to english Wikipedia page about program is null</li>
     * <li>URL to czech Wikipedia page about program is null</li>
     * <li>Count of media isn't positive number</li>
     * <li>Other data is null</li>
     * <li>Note is null</li>
     * </ul>
     *
     * @param program program
     * @return result with validation errors
     */
    Result<Void> add(Program program);

    /**
     * Updates program.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Program is null</li>
     * <li>ID is null</li>
     * <li>Name is null</li>
     * <li>Name is empty string</li>
     * <li>URL to english Wikipedia page about program is null</li>
     * <li>URL to czech Wikipedia page about program is null</li>
     * <li>Count of media isn't positive number</li>
     * <li>Other data is null</li>
     * <li>Note is null</li>
     * <li>Program doesn't exist in data storage</li>
     * </ul>
     *
     * @param program new value of program
     * @return result with validation errors
     */
    Result<Void> update(Program program);

    /**
     * Returns total count of media.
     *
     * @return result with total count of media
     */
    Result<Integer> getTotalMediaCount();

}
