package cz.vhromada.catalog.facade.impl;

import cz.vhromada.catalog.entity.Program;
import cz.vhromada.catalog.facade.ProgramFacade;
import cz.vhromada.common.facade.AbstractMovableParentFacade;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.validator.MovableValidator;
import cz.vhromada.converter.Converter;
import cz.vhromada.result.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of facade for programs.
 *
 * @author Vladimir Hromada
 */
@Component("programFacade")
public class ProgramFacadeImpl extends AbstractMovableParentFacade<Program, cz.vhromada.catalog.domain.Program> implements ProgramFacade {

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
    public ProgramFacadeImpl(final MovableService<cz.vhromada.catalog.domain.Program> programService, final Converter converter,
        final MovableValidator<Program> programValidator) {
        super(programService, converter, programValidator);
    }

    @Override
    public Result<Integer> getTotalMediaCount() {
        int totalMedia = 0;
        for (final cz.vhromada.catalog.domain.Program program : getMovableService().getAll()) {
            totalMedia += program.getMediaCount();
        }

        return Result.of(totalMedia);
    }

    @Override
    protected Class<Program> getEntityClass() {
        return Program.class;
    }

    @Override
    protected Class<cz.vhromada.catalog.domain.Program> getDomainClass() {
        return cz.vhromada.catalog.domain.Program.class;
    }

}
