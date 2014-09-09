package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.vhromada.catalog.dao.entities.Program;
import cz.vhromada.catalog.facade.ProgramFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.ProgramTO;
import cz.vhromada.catalog.facade.validators.ProgramTOValidator;
import cz.vhromada.catalog.service.ProgramService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.validators.Validators;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents implementation of facade for programs.
 *
 * @author Vladimir Hromada
 */
@Component("programFacade")
@Transactional
public class ProgramFacadeImpl implements ProgramFacade {

	/** Service for programs */
	@Autowired
	private ProgramService programService;

	/** Conversion service */
	@Autowired
	@Qualifier("coreConversionService")
	private ConversionService conversionService;

	/** Validator for TO for program */
	@Autowired
	private ProgramTOValidator programTOValidator;

	/**
	 * Returns service for programs.
	 *
	 * @return service for programs
	 */
	public ProgramService getProgramService() {
		return programService;
	}

	/**
	 * Sets a new value to service for programs.
	 *
	 * @param programService new value
	 */
	public void setProgramService(final ProgramService programService) {
		this.programService = programService;
	}

	/**
	 * Returns conversion service.
	 *
	 * @return conversion service
	 */
	public ConversionService getConversionService() {
		return conversionService;
	}

	/**
	 * Sets a new value to conversion service.
	 *
	 * @param conversionService new value
	 */
	public void setConversionService(final ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	/**
	 * Returns validator for TO for program.
	 *
	 * @return validator for TO for program
	 */
	public ProgramTOValidator getProgramTOValidator() {
		return programTOValidator;
	}

	/**
	 * Sets a new value to validator for TO for program.
	 *
	 * @param programTOValidator new value
	 */
	public void setProgramTOValidator(final ProgramTOValidator programTOValidator) {
		this.programTOValidator = programTOValidator;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for programs isn't set
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void newData() {
		Validators.validateFieldNotNull(programService, "Service for programs");

		try {
			programService.newData();
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for programs isn't set
	 *                                  or conversion service isn't set
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ProgramTO> getPrograms() {
		Validators.validateFieldNotNull(programService, "Service for programs");
		Validators.validateFieldNotNull(conversionService, "Conversion service");

		try {
			final List<ProgramTO> programs = new ArrayList<>();
			for (Program program : programService.getPrograms()) {
				programs.add(conversionService.convert(program, ProgramTO.class));
			}
			Collections.sort(programs);
			return programs;
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for programs isn't set
	 *                                  or conversion service isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public ProgramTO getProgram(Integer id) {
		Validators.validateFieldNotNull(programService, "Service for programs");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateArgumentNotNull(id, "ID");

		try {
			return conversionService.convert(programService.getProgram(id), ProgramTO.class);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for programs isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for program isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void add(final ProgramTO program) {
		Validators.validateFieldNotNull(programService, "Service for programs");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateFieldNotNull(programTOValidator, "Validator for TO for program");
		programTOValidator.validateNewProgramTO(program);

		try {
			final Program programEntity = conversionService.convert(program, Program.class);
			programService.add(programEntity);
			if (programEntity.getId() == null) {
				throw new FacadeOperationException("Service tier doesn't set ID.");
			}
			program.setId(programEntity.getId());
			program.setPosition(programEntity.getPosition());
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for programs isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for program isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void update(final ProgramTO program) {
		Validators.validateFieldNotNull(programService, "Service for programs");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateFieldNotNull(programTOValidator, "Validator for TO for program");
		programTOValidator.validateExistingProgramTO(program);
		try {
			final Program programEntity = conversionService.convert(program, Program.class);
			Validators.validateExists(programService.exists(programEntity), "TO for program");

			programService.update(programEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for programs isn't set
	 *                                  or validator for TO for program isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void remove(final ProgramTO program) {
		Validators.validateFieldNotNull(programService, "Service for programs");
		Validators.validateFieldNotNull(programTOValidator, "Validator for TO for program");
		programTOValidator.validateProgramTOWithId(program);
		try {
			final Program programEntity = programService.getProgram(program.getId());
			Validators.validateExists(programEntity, "TO for program");

			programService.remove(programEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for programs isn't set
	 *                                  or validator for TO for program isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void duplicate(final ProgramTO program) {
		Validators.validateFieldNotNull(programService, "Service for programs");
		Validators.validateFieldNotNull(programTOValidator, "Validator for TO for program");
		programTOValidator.validateProgramTOWithId(program);
		try {
			final Program oldProgram = programService.getProgram(program.getId());
			Validators.validateExists(oldProgram, "TO for program");

			programService.duplicate(oldProgram);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for programs isn't set
	 *                                  or validator for TO for program isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void moveUp(final ProgramTO program) {
		Validators.validateFieldNotNull(programService, "Service for programs");
		Validators.validateFieldNotNull(programTOValidator, "Validator for TO for program");
		programTOValidator.validateProgramTOWithId(program);
		try {
			final Program programEntity = programService.getProgram(program.getId());
			Validators.validateExists(programEntity, "TO for program");
			final List<Program> programs = programService.getPrograms();
			Validators.validateMoveUp(programs, programEntity, "program");

			programService.moveUp(programEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for programs isn't set
	 *                                  or validator for TO for program isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void moveDown(final ProgramTO program) {
		Validators.validateFieldNotNull(programService, "Service for programs");
		Validators.validateFieldNotNull(programTOValidator, "Validator for TO for program");
		programTOValidator.validateProgramTOWithId(program);
		try {
			final Program programEntity = programService.getProgram(program.getId());
			Validators.validateExists(programEntity, "TO for program");
			final List<Program> programs = programService.getPrograms();
			Validators.validateMoveDown(programs, programEntity, "program");

			programService.moveDown(programEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for programs isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for program isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean exists(final ProgramTO program) {
		Validators.validateFieldNotNull(programService, "Service for programs");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateFieldNotNull(programTOValidator, "Validator for TO for program");
		programTOValidator.validateProgramTOWithId(program);
		try {

			return programService.exists(conversionService.convert(program, Program.class));
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for programs isn't set
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void updatePositions() {
		Validators.validateFieldNotNull(programService, "Service for programs");

		try {
			programService.updatePositions();
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for programs isn't set
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public int getTotalMediaCount() {
		Validators.validateFieldNotNull(programService, "Service for programs");

		try {
			return programService.getTotalMediaCount();
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

}
