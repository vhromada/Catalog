package cz.vhromada.catalog.validator;

import cz.vhromada.catalog.entity.Program;

/**
 * An interface represents validator for program.
 *
 * @author Vladimir Hromada
 */
public interface ProgramValidator {

    /**
     * Validates new program.
     *
     * @param program validating program
     * @throws IllegalArgumentException if program is null
     *                                  or ID isn't null
     *                                  or name is null
     *                                  or name is empty string
     *                                  or URL to english Wikipedia page about program is null
     *                                  or URL to czech Wikipedia page about program is null
     *                                  or count of media isn't positive number
     *                                  or other data is null
     *                                  or note is null
     */
    void validateNewProgram(Program program);

    /**
     * Validates existing program.
     *
     * @param program validating program
     * @throws IllegalArgumentException if program is null
     *                                  or ID is null
     *                                  or name is null
     *                                  or name is empty string
     *                                  or URL to english Wikipedia page about program is null
     *                                  or URL to czech Wikipedia page about program is null
     *                                  or count of media isn't positive number
     *                                  or other data is null
     *                                  or note is null
     */
    void validateExistingProgram(Program program);

    /**
     * Validates program with ID.
     *
     * @param program validating program
     * @throws IllegalArgumentException if program is null
     *                                  or ID is null
     */
    void validateProgramWithId(Program program);

}
