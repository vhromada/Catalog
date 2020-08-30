package com.github.vhromada.catalog.facade.impl

import com.github.vhromada.catalog.entity.Program
import com.github.vhromada.catalog.facade.ProgramFacade
import com.github.vhromada.common.facade.AbstractMovableParentFacade
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.provider.TimeProvider
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.service.MovableService
import com.github.vhromada.common.validator.MovableValidator
import org.springframework.stereotype.Component

/**
 * A class represents implementation of facade for programs.
 *
 * @author Vladimir Hromada
 */
@Component("programFacade")
class ProgramFacadeImpl(
        programService: MovableService<com.github.vhromada.catalog.domain.Program>,
        accountProvider: AccountProvider,
        timeProvider: TimeProvider,
        mapper: Mapper<Program, com.github.vhromada.catalog.domain.Program>,
        programValidator: MovableValidator<Program>
) : AbstractMovableParentFacade<Program, com.github.vhromada.catalog.domain.Program>(programService, accountProvider, timeProvider, mapper, programValidator), ProgramFacade {

    override fun getTotalMediaCount(): Result<Int> {
        return Result.of(service.getAll().sumBy { it.mediaCount })
    }

}
