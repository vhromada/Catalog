package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.entity.Program;
import cz.vhromada.result.Result;

/**
 * An interface represents facade for programs.
 *
 * @author Vladimir Hromada
 */
public interface ProgramFacade {

    /**
     * Creates new data.
     *
     * @return result
     */
    Result<Void> newData();

    /**
     * Returns programs.
     *
     * @return result with list of programs
     */
    Result<List<Program>> getAll();

    /**
     * Returns program with ID or null if there isn't such program.
     * <br>
     * Validation errors:
     * <ul>
     * <li>ID is null</li>
     * </ul>
     *
     * @param id ID
     * @return result with program or validation errors
     */
    Result<Program> get(Integer id);

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
     * Removes program.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Program is null</li>
     * <li>ID is null</li>
     * <li>Program doesn't exist in data storage</li>
     *
     * @param program program
     * @return result with validation errors
     */
    Result<Void> remove(Program program);

    /**
     * Duplicates program.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Program is null</li>
     * <li>ID is null</li>
     * <li>Program doesn't exist in data storage</li>
     *
     * @param program program
     * @return result with validation errors
     */
    Result<Void> duplicate(Program program);

    /**
     * Moves program in list one position up.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Program is null</li>
     * <li>ID is null</li>
     * <li>Program can't be moved up</li>
     * <li>Program doesn't exist in data storage</li>
     *
     * @param program program
     * @return result with validation errors
     */
    Result<Void> moveUp(Program program);

    /**
     * Moves program in list one position down.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Program is null</li>
     * <li>ID is null</li>
     * <li>Program can't be moved down</li>
     * <li>Program doesn't exist in data storage</li>
     *
     * @param program program
     * @return result with validation errors
     */
    Result<Void> moveDown(Program program);

    /**
     * Updates positions.
     *
     * @return result
     */
    Result<Void> updatePositions();

    /**
     * Returns total count of media.
     *
     * @return result with total count of media
     */
    Result<Integer> getTotalMediaCount();

}
