package cz.vhromada.catalog.facade.impl

import cz.vhromada.catalog.entity.Program
import cz.vhromada.catalog.facade.ProgramFacade
import cz.vhromada.common.facade.AbstractMovableParentFacade
import cz.vhromada.common.mapper.Mapper
import cz.vhromada.common.result.Result
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.validator.MovableValidator
import org.springframework.stereotype.Component

/**
 * A class represents implementation of facade for programs.
 *
 * @author Vladimir Hromada
 */
@Component("programFacade")
class ProgramFacadeImpl(
        programService: MovableService<cz.vhromada.catalog.domain.Program>,
        mapper: Mapper<Program, cz.vhromada.catalog.domain.Program>,
        programValidator: MovableValidator<Program>) : AbstractMovableParentFacade<Program, cz.vhromada.catalog.domain.Program>(programService, mapper, programValidator), ProgramFacade {

    override fun getTotalMediaCount(): Result<Int> {
        return Result.of(service.getAll().sumBy { it.mediaCount })
    }

}
