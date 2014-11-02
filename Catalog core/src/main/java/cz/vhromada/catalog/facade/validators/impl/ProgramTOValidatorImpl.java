package cz.vhromada.catalog.facade.validators.impl;

import cz.vhromada.catalog.facade.to.ProgramTO;
import cz.vhromada.catalog.facade.validators.ProgramTOValidator;
import cz.vhromada.validators.Validators;
import cz.vhromada.validators.exceptions.ValidationException;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of validator for TO for program.
 *
 * @author Vladimir Hromada
 */
@Component("programTOValidator")
public class ProgramTOValidatorImpl implements ProgramTOValidator {

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 */
	@Override
	public void validateNewProgramTO(final ProgramTO program) {
		validateProgramTO(program);
		Validators.validateNull(program.getId(), "ID");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 */
	@Override
	public void validateExistingProgramTO(final ProgramTO program) {
		validateProgramTO(program);
		Validators.validateNotNull(program.getId(), "ID");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 */
	@Override
	public void validateProgramTOWithId(final ProgramTO program) {
		Validators.validateArgumentNotNull(program, "TO for program");
		Validators.validateNotNull(program.getId(), "ID");
	}

	/**
	 * Validates TO for program.
	 *
	 * @param program TO for program
	 * @throws IllegalArgumentException if TO for program is null
	 * @throws ValidationException      if name is null
	 *                                  or name is empty string
	 *                                  or URL to english Wikipedia page about program is null
	 *                                  or URL to czech Wikipedia page about program is null
	 *                                  or count of media isn't positive number
	 *                                  or other data is null
	 *                                  or note is null
	 */
	private void validateProgramTO(final ProgramTO program) {
		Validators.validateArgumentNotNull(program, "TO for program");
		Validators.validateNotNull(program.getName(), "Name");
		Validators.validateNotEmptyString(program.getName(), "Name");
		Validators.validateNotNull(program.getWikiEn(), "URL to english Wikipedia page about program");
		Validators.validateNotNull(program.getWikiCz(), "URL to czech Wikipedia page about program");
		Validators.validatePositiveNumber(program.getMediaCount(), "Count of media");
		Validators.validateNotNull(program.getOtherData(), "Other data");
		Validators.validateNotNull(program.getNote(), "Note");
	}

}
