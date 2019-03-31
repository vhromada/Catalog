package cz.vhromada.catalog.facade.impl;

import cz.vhromada.catalog.entity.Program;
import cz.vhromada.catalog.facade.ProgramFacade;
import cz.vhromada.common.converter.MovableConverter;
import cz.vhromada.common.facade.AbstractMovableParentFacade;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.validator.MovableValidator;
import cz.vhromada.validation.result.Result;

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
     * @param converter        converter for programs
     * @param programValidator validator for program
     * @throws IllegalArgumentException if service for programs is null
     *                                  or converter for programs is null
     *                                  or validator for program is null
     */
    @Autowired
    public ProgramFacadeImpl(final MovableService<cz.vhromada.catalog.domain.Program> programService,
        final MovableConverter<Program, cz.vhromada.catalog.domain.Program> converter, final MovableValidator<Program> programValidator) {
        super(programService, converter, programValidator);
    }

    @Override
    public Result<Integer> getTotalMediaCount() {
        int totalMedia = 0;
        for (final cz.vhromada.catalog.domain.Program program : getService().getAll()) {
            totalMedia += program.getMediaCount();
        }

        return Result.of(totalMedia);
    }

}
