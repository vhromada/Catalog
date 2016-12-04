package cz.vhromada.catalog.facade.impl;

import java.util.List;

import cz.vhromada.catalog.entity.Program;
import cz.vhromada.catalog.facade.ProgramFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.validator.ProgramValidator;
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
     * Program argument
     */
    private static final String PROGRAM_ARGUMENT = "program";

    /**
     * Service for programs
     */
    private CatalogService<cz.vhromada.catalog.domain.Program> programService;

    /**
     * Converter
     */
    private Converter converter;

    /**
     * Validator for program
     */
    private ProgramValidator programValidator;

    /**
     * Creates a new instance of ProgramFacadeImpl.
     *
     * @param programService   service for programs
     * @param converter        converter
     * @param programValidator validator for program
     * @throws IllegalArgumentException if service for programs is null
     *                                  or converter is null
     *                                  or validator for program is null
     */
    @Autowired
    public ProgramFacadeImpl(@Qualifier("programService") final CatalogService<cz.vhromada.catalog.domain.Program> programService,
            @Qualifier("catalogDozerConverter") final Converter converter,
            final ProgramValidator programValidator) {
        Validators.validateArgumentNotNull(programService, "Service for programs");
        Validators.validateArgumentNotNull(converter, "Converter");
        Validators.validateArgumentNotNull(programValidator, "Validator for program");

        this.programService = programService;
        this.converter = converter;
        this.programValidator = programValidator;
    }

    @Override
    public void newData() {
        programService.newData();
    }

    @Override
    public List<Program> getPrograms() {
        return converter.convertCollection(programService.getAll(), Program.class);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public Program getProgram(final Integer id) {
        Validators.validateArgumentNotNull(id, "ID");

        return converter.convert(programService.get(id), Program.class);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void add(final Program program) {
        programValidator.validateNewProgram(program);

        programService.add(converter.convert(program, cz.vhromada.catalog.domain.Program.class));
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void update(final Program program) {
        programValidator.validateExistingProgram(program);
        Validators.validateExists(programService.get(program.getId()), PROGRAM_ARGUMENT);

        programService.update(converter.convert(program, cz.vhromada.catalog.domain.Program.class));
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void remove(final Program program) {
        programValidator.validateProgramWithId(program);
        final cz.vhromada.catalog.domain.Program programEntity = programService.get(program.getId());
        Validators.validateExists(programEntity, PROGRAM_ARGUMENT);

        programService.remove(programEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void duplicate(final Program program) {
        programValidator.validateProgramWithId(program);
        final cz.vhromada.catalog.domain.Program programEntity = programService.get(program.getId());
        Validators.validateExists(programEntity, PROGRAM_ARGUMENT);

        programService.duplicate(programEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveUp(final Program program) {
        programValidator.validateProgramWithId(program);
        final cz.vhromada.catalog.domain.Program programEntity = programService.get(program.getId());
        Validators.validateExists(programEntity, PROGRAM_ARGUMENT);
        final List<cz.vhromada.catalog.domain.Program> programs = programService.getAll();
        Validators.validateMoveUp(programs, programEntity, PROGRAM_ARGUMENT);

        programService.moveUp(programEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveDown(final Program program) {
        programValidator.validateProgramWithId(program);
        final cz.vhromada.catalog.domain.Program programEntity = programService.get(program.getId());
        Validators.validateExists(programEntity, PROGRAM_ARGUMENT);
        final List<cz.vhromada.catalog.domain.Program> programs = programService.getAll();
        Validators.validateMoveDown(programs, programEntity, PROGRAM_ARGUMENT);

        programService.moveDown(programEntity);
    }

    @Override
    public void updatePositions() {
        programService.updatePositions();
    }

    @Override
    public int getTotalMediaCount() {
        int totalMedia = 0;
        for (final cz.vhromada.catalog.domain.Program program : programService.getAll()) {
            totalMedia += program.getMediaCount();
        }

        return totalMedia;
    }

}
