package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Program
import com.github.vhromada.catalog.repository.ProgramRepository
import com.github.vhromada.common.entity.Account
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.service.AbstractParentService
import org.springframework.stereotype.Component
import java.util.Optional

/**
 * A class represents service for programs.
 *
 * @author Vladimir Hromada
 */
@Component("programService")
class ProgramService(
    private val programRepository: ProgramRepository,
    accountProvider: AccountProvider
) : AbstractParentService<Program>(repository = programRepository, accountProvider = accountProvider) {

    override fun getCopy(data: Program): Program {
        return data.copy(id = null)
    }

    override fun getAccountData(account: Account, id: Int): Optional<Program> {
        return programRepository.findByIdAndCreatedUser(id = id, user = account.uuid!!)
    }

    override fun getAccountDataList(account: Account): List<Program> {
        return programRepository.findByCreatedUser(user = account.uuid!!)
    }

}
