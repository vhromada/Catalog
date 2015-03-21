package cz.vhromada.catalog.facade.validators.impl;

import cz.vhromada.catalog.facade.to.ProgramTO;
import cz.vhromada.catalog.facade.validators.ProgramTOValidator;
import cz.vhromada.validators.Validators;

import org.springframework.stereotype.Component;

/**
 * A class represents implementation of validator for TO for program.
 *
 * @author Vladimir Hromada
 */
@Component("programTOValidator")
public class ProgramTOValidatorImpl implements ProgramTOValidator {

    /**
     * TO for program argument
     */
    private static final String PROGRAM_TO_ARGUMENT = "TO for program";

    /**
     * Field ID
     */
    private static final String ID_FIELD = "ID";

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void validateNewProgramTO(final ProgramTO program) {
        validateProgramTO(program);
        Validators.validateNull(program.getId(), ID_FIELD);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void validateExistingProgramTO(final ProgramTO program) {
        validateProgramTO(program);
        Validators.validateNotNull(program.getId(), ID_FIELD);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void validateProgramTOWithId(final ProgramTO program) {
        Validators.validateArgumentNotNull(program, PROGRAM_TO_ARGUMENT);
        Validators.validateNotNull(program.getId(), ID_FIELD);
    }

    /**
     * Validates TO for program.
     *
     * @param program TO for program
     * @throws IllegalArgumentException                              if TO for program is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if name is null
     *                                                               or name is empty string
     *                                                               or URL to english Wikipedia page about program is null
     *                                                               or URL to czech Wikipedia page about program is null
     *                                                               or count of media isn't positive number
     *                                                               or other data is null
     *                                                               or note is null
     */
    private static void validateProgramTO(final ProgramTO program) {
        Validators.validateArgumentNotNull(program, PROGRAM_TO_ARGUMENT);
        Validators.validateNotNull(program.getName(), "Name");
        Validators.validateNotEmptyString(program.getName(), "Name");
        Validators.validateNotNull(program.getWikiEn(), "URL to english Wikipedia page about program");
        Validators.validateNotNull(program.getWikiCz(), "URL to czech Wikipedia page about program");
        Validators.validatePositiveNumber(program.getMediaCount(), "Count of media");
        Validators.validateNotNull(program.getOtherData(), "Other data");
        Validators.validateNotNull(program.getNote(), "Note");
    }

}
