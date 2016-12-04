package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.entity.Program;

/**
 * An interface represents facade for programs.
 *
 * @author Vladimir Hromada
 */
public interface ProgramFacade {

    /**
     * Creates new data.
     */
    void newData();

    /**
     * Returns programs.
     *
     * @return programs
     */
    List<Program> getPrograms();

    /**
     * Returns program with ID or null if there isn't such program.
     *
     * @param id ID
     * @return program with ID or null if there isn't such program
     * @throws IllegalArgumentException if ID is null
     */
    Program getProgram(Integer id);

    /**
     * Adds program. Sets new ID and position.
     *
     * @param program program
     * @throws IllegalArgumentException                              if program is null
     *                                                               or ID isn't null
     *                                                               or name is null
     *                                                               or name is empty string
     *                                                               or URL to english Wikipedia page about program is null
     *                                                               or URL to czech Wikipedia page about program is null
     *                                                               or count of media isn't positive number
     *                                                               or other data is null
     *                                                               or note is null
     */
    void add(Program program);

    /**
     * Updates program.
     *
     * @param program new value of program
     * @throws IllegalArgumentException                                  if program is null
     *                                                                   or ID is null
     *                                                                   or name is null
     *                                                                   or name is empty string
     *                                                                   or URL to english Wikipedia page about program is null
     *                                                                   or URL to czech Wikipedia page about program is null
     *                                                                   or count of media isn't positive number
     *                                                                   or other data is null
     *                                                                   or note is null
     *                                                                   or program doesn't exist in data storage
     */
    void update(Program program);

    /**
     * Removes program.
     *
     * @param program program
     * @throws IllegalArgumentException                                  if program is null
     *                                                                   or ID is null
     *                                                                   or program doesn't exist in data storage
     */
    void remove(Program program);

    /**
     * Duplicates program.
     *
     * @param program program
     * @throws IllegalArgumentException                                  if program is null
     *                                                                   or ID is null
     *                                                                   or program doesn't exist in data storage
     */
    void duplicate(Program program);

    /**
     * Moves program in list one position up.
     *
     * @param program program
     * @throws IllegalArgumentException                                  if program is null
     *                                                                   or ID is null
     *                                                                   or program can't be moved up
     *                                                                   or program doesn't exist in data storage
     */
    void moveUp(Program program);

    /**
     * Moves program in list one position down.
     *
     * @param program program
     * @throws IllegalArgumentException                                  if program is null
     *                                                                   or ID is null
     *                                                                   or program can't be moved down
     *                                                                   or program doesn't exist in data storage
     */
    void moveDown(Program program);

    /**
     * Updates positions.
     */
    void updatePositions();

    /**
     * Returns total count of media.
     *
     * @return total count of media
     */
    int getTotalMediaCount();

}
