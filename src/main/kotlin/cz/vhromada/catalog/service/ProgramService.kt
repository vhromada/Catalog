package cz.vhromada.catalog.service

import cz.vhromada.catalog.domain.Program
import cz.vhromada.catalog.repository.ProgramRepository
import cz.vhromada.common.entity.Account
import cz.vhromada.common.provider.AccountProvider
import cz.vhromada.common.provider.TimeProvider
import cz.vhromada.common.service.AbstractMovableService
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.Cache
import org.springframework.stereotype.Component

/**
 * A class represents service for programs.
 *
 * @author Vladimir Hromada
 */
@Component("programService")
class ProgramService(
        private val programRepository: ProgramRepository,
        accountProvider: AccountProvider,
        timeProvider: TimeProvider,
        @Value("#{cacheManager.getCache('catalogCache')}") cache: Cache) : AbstractMovableService<Program>(programRepository, accountProvider, timeProvider, cache, "programs") {

    override fun getAccountData(account: Account): List<Program> {
        return programRepository.findByAuditCreatedUser(account.id)
    }

    override fun getCopy(data: Program): Program {
        return data.copy(id = null)
    }

}
