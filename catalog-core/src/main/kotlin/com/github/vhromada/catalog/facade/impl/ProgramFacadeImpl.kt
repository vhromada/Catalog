package com.github.vhromada.catalog.facade.impl

import com.github.vhromada.catalog.entity.Program
import com.github.vhromada.catalog.facade.ProgramFacade
import com.github.vhromada.common.facade.AbstractParentFacade
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.service.ParentService
import com.github.vhromada.common.validator.Validator
import org.springframework.stereotype.Component

/**
 * A class represents implementation of facade for programs.
 *
 * @author Vladimir Hromada
 */
@Component("programFacade")
class ProgramFacadeImpl(
    private val programService: ParentService<com.github.vhromada.catalog.domain.Program>,
    mapper: Mapper<Program, com.github.vhromada.catalog.domain.Program>,
    programValidator: Validator<Program, com.github.vhromada.catalog.domain.Program>
) : AbstractParentFacade<Program, com.github.vhromada.catalog.domain.Program>(parentService = programService, mapper = mapper, validator = programValidator), ProgramFacade {

    @Suppress("DuplicatedCode")
    override fun updateData(data: Program): Result<Unit> {
        val storedProgram = programService.get(id = data.id!!)
        val validationResult = validator.validateExists(data = storedProgram)
        if (validationResult.isOk()) {
            val program = mapper.map(source = data)
            program.createdUser = storedProgram.get().createdUser
            program.createdTime = storedProgram.get().createdTime
            programService.update(data = program)
        }
        return validationResult
    }

    override fun addData(data: Program): Result<Unit> {
        programService.add(data = mapper.map(source = data))
        return Result()
    }

    override fun getTotalMediaCount(): Result<Int> {
        return Result.of(data = programService.getAll().sumBy { it.mediaCount })
    }

}
