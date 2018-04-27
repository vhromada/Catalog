package cz.vhromada.catalog.facade;

import cz.vhromada.catalog.entity.Program;
import cz.vhromada.common.facade.MovableParentFacade;
import cz.vhromada.result.Result;

/**
 * An interface represents facade for programs.
 *
 * @author Vladimir Hromada
 */
public interface ProgramFacade extends MovableParentFacade<Program> {

    /**
     * Adds program. Sets new ID and position.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Program is null</li>
     * <li>ID isn't null</li>
     * <li>Position isn't null</li>
     * <li>Name is null</li>
     * <li>Name is empty string</li>
     * <li>URL to english Wikipedia page about program is null</li>
     * <li>URL to czech Wikipedia page about program is null</li>
     * <li>Count of media isn't positive number</li>
     * <li>Other data is null</li>
     * <li>Note is null</li>
     * </ul>
     *
     * @param data program
     * @return result with validation errors
     */
    @Override
    Result<Void> add(Program data);

    /**
     * Updates program.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Program is null</li>
     * <li>ID is null</li>
     * <li>Position is null</li>
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
     * @param data new value of program
     * @return result with validation errors
     */
    @Override
    Result<Void> update(Program data);

    /**
     * Returns total count of media.
     *
     * @return result with total count of media
     */
    Result<Integer> getTotalMediaCount();

}
