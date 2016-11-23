package cz.vhromada.catalog.validators;

import cz.vhromada.catalog.entity.ProgramTO;

/**
 * An interface represents validator for TO for program.
 *
 * @author Vladimir Hromada
 */
public interface ProgramTOValidator {

    /**
     * Validates new TO for program.
     *
     * @param program validating TO for program
     * @throws IllegalArgumentException                              if TO for program is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID isn't null
     *                                                               or name is null
     *                                                               or name is empty string
     *                                                               or URL to english Wikipedia page about program is null
     *                                                               or URL to czech Wikipedia page about program is null
     *                                                               or count of media isn't positive number
     *                                                               or other data is null
     *                                                               or note is null
     */
    void validateNewProgramTO(ProgramTO program);

    /**
     * Validates existing TO for program.
     *
     * @param program validating TO for program
     * @throws IllegalArgumentException                              if TO for program is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID is null
     *                                                               or name is null
     *                                                               or name is empty string
     *                                                               or URL to english Wikipedia page about program is null
     *                                                               or URL to czech Wikipedia page about program is null
     *                                                               or count of media isn't positive number
     *                                                               or other data is null
     *                                                               or note is null
     */
    void validateExistingProgramTO(ProgramTO program);

    /**
     * Validates TO for program with ID.
     *
     * @param program validating TO for program
     * @throws IllegalArgumentException                              if TO for program is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID is null
     */
    void validateProgramTOWithId(ProgramTO program);

}
