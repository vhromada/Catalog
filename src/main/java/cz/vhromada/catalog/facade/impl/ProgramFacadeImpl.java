package cz.vhromada.catalog.facade.impl;

import java.util.List;

import cz.vhromada.catalog.entity.Program;
import cz.vhromada.catalog.facade.ProgramFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.validator.ProgramValidator;
import cz.vhromada.converters.Converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * A class represents implementation of facade for programs.
 *
 * @author Vladimir Hromada
 */
@Component("programFacade")
public class ProgramFacadeImpl implements ProgramFacade {

    /**
     * Message for not existing program
     */
    private static final String NOT_EXISTING_PROGRAM_MESSAGE = "Program doesn't exist.";

    /**
     * Message for not movable program
     */
    private static final String NOT_MOVABLE_PROGRAM_MESSAGE = "ID isn't valid - program can't be moved.";

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
        Assert.notNull(programService, "Service for programs mustn't be null.");
        Assert.notNull(converter, "Converter mustn't be null.");
        Assert.notNull(programValidator, "Validator for program mustn't be null.");

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
        Assert.notNull(id, "ID mustn't be null.");

        return converter.convert(programService.get(id), Program.class);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void add(final Program program) {
        programValidator.validateNewProgram(program);

        programService.add(converter.convert(program, cz.vhromada.catalog.domain.Program.class));
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void update(final Program program) {
        programValidator.validateExistingProgram(program);
        if (programService.get(program.getId()) == null) {
            throw new IllegalArgumentException(NOT_EXISTING_PROGRAM_MESSAGE);
        }

        programService.update(converter.convert(program, cz.vhromada.catalog.domain.Program.class));
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void remove(final Program program) {
        programValidator.validateProgramWithId(program);
        final cz.vhromada.catalog.domain.Program programDomain = programService.get(program.getId());
        if (programDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_PROGRAM_MESSAGE);
        }

        programService.remove(programDomain);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void duplicate(final Program program) {
        programValidator.validateProgramWithId(program);
        final cz.vhromada.catalog.domain.Program programDomain = programService.get(program.getId());
        if (programDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_PROGRAM_MESSAGE);
        }

        programService.duplicate(programDomain);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void moveUp(final Program program) {
        programValidator.validateProgramWithId(program);
        final cz.vhromada.catalog.domain.Program programDomain = programService.get(program.getId());
        if (programDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_PROGRAM_MESSAGE);
        }
        final List<cz.vhromada.catalog.domain.Program> programs = programService.getAll();
        if (programs.indexOf(programDomain) <= 0) {
            throw new IllegalArgumentException(NOT_MOVABLE_PROGRAM_MESSAGE);
        }

        programService.moveUp(programDomain);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void moveDown(final Program program) {
        programValidator.validateProgramWithId(program);
        final cz.vhromada.catalog.domain.Program programDomain = programService.get(program.getId());
        if (programDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_PROGRAM_MESSAGE);
        }
        final List<cz.vhromada.catalog.domain.Program> programs = programService.getAll();
        if (programs.indexOf(programDomain) >= programs.size() - 1) {
            throw new IllegalArgumentException(NOT_MOVABLE_PROGRAM_MESSAGE);
        }

        programService.moveDown(programDomain);
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
