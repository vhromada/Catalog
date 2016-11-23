package cz.vhromada.catalog.facade.impl;

import java.util.List;

import cz.vhromada.catalog.domain.Program;
import cz.vhromada.catalog.entity.ProgramTO;
import cz.vhromada.catalog.facade.ProgramFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.validators.ProgramTOValidator;
import cz.vhromada.converters.Converter;
import cz.vhromada.validators.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of facade for programs.
 *
 * @author Vladimir Hromada
 */
@Component("programFacade")
public class ProgramFacadeImpl implements ProgramFacade {

    /**
     * TO for program argument
     */
    private static final String PROGRAM_TO_ARGUMENT = "TO for program";

    /**
     * Service for programs
     */
    private CatalogService<Program> programService;

    /**
     * Converter
     */
    private Converter converter;

    /**
     * Validator for TO for program
     */
    private ProgramTOValidator programTOValidator;

    /**
     * Creates a new instance of ProgramFacadeImpl.
     *
     * @param programService     service for programs
     * @param converter          converter
     * @param programTOValidator validator for TO for program
     * @throws IllegalArgumentException if service for programs is null
     *                                  or converter is null
     *                                  or validator for TO for program is null
     */
    @Autowired
    public ProgramFacadeImpl(@Qualifier("programService") final CatalogService<Program> programService,
            @Qualifier("catalogDozerConverter") final Converter converter,
            final ProgramTOValidator programTOValidator) {
        Validators.validateArgumentNotNull(programService, "Service for programs");
        Validators.validateArgumentNotNull(converter, "Converter");
        Validators.validateArgumentNotNull(programTOValidator, "Validator for TO for program");

        this.programService = programService;
        this.converter = converter;
        this.programTOValidator = programTOValidator;
    }

    @Override
    public void newData() {
        programService.newData();
    }

    @Override
    public List<ProgramTO> getPrograms() {
        return converter.convertCollection(programService.getAll(), ProgramTO.class);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public ProgramTO getProgram(final Integer id) {
        Validators.validateArgumentNotNull(id, "ID");

        return converter.convert(programService.get(id), ProgramTO.class);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void add(final ProgramTO program) {
        programTOValidator.validateNewProgramTO(program);

        programService.add(converter.convert(program, Program.class));
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void update(final ProgramTO program) {
        programTOValidator.validateExistingProgramTO(program);
        Validators.validateExists(programService.get(program.getId()), PROGRAM_TO_ARGUMENT);

        programService.update(converter.convert(program, Program.class));
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void remove(final ProgramTO program) {
        programTOValidator.validateProgramTOWithId(program);
        final Program programEntity = programService.get(program.getId());
        Validators.validateExists(programEntity, PROGRAM_TO_ARGUMENT);

        programService.remove(programEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void duplicate(final ProgramTO program) {
        programTOValidator.validateProgramTOWithId(program);
        final Program programEntity = programService.get(program.getId());
        Validators.validateExists(programEntity, PROGRAM_TO_ARGUMENT);

        programService.duplicate(programEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveUp(final ProgramTO program) {
        programTOValidator.validateProgramTOWithId(program);
        final Program programEntity = programService.get(program.getId());
        Validators.validateExists(programEntity, PROGRAM_TO_ARGUMENT);
        final List<Program> programs = programService.getAll();
        Validators.validateMoveUp(programs, programEntity, PROGRAM_TO_ARGUMENT);

        programService.moveUp(programEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveDown(final ProgramTO program) {
        programTOValidator.validateProgramTOWithId(program);
        final Program programEntity = programService.get(program.getId());
        Validators.validateExists(programEntity, PROGRAM_TO_ARGUMENT);
        final List<Program> programs = programService.getAll();
        Validators.validateMoveDown(programs, programEntity, PROGRAM_TO_ARGUMENT);

        programService.moveDown(programEntity);
    }

    @Override
    public void updatePositions() {
        programService.updatePositions();
    }

    @Override
    public int getTotalMediaCount() {
        int totalMedia = 0;
        for (final Program program : programService.getAll()) {
            totalMedia += program.getMediaCount();
        }

        return totalMedia;
    }

}
