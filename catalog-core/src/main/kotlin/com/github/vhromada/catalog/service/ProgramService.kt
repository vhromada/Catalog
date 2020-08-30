package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Program
import com.github.vhromada.catalog.repository.ProgramRepository
import com.github.vhromada.common.entity.Account
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.provider.TimeProvider
import com.github.vhromada.common.service.AbstractMovableService
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.Cache
import org.springframework.stereotype.Component

/**
 * A class represents service for programs.
 *
 * @author Vladimir Hromada
 */
@Component("programService")
@Suppress("SpringElInspection", "ELValidationInJSP")
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
