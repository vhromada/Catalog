package cz.vhromada.catalog.service

import cz.vhromada.catalog.domain.Program
import cz.vhromada.catalog.repository.ProgramRepository
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
        programRepository: ProgramRepository,
        @Value("#{cacheManager.getCache('catalogCache')}") cache: Cache) : AbstractMovableService<Program>(programRepository, cache, "programs") {

    override fun getCopy(data: Program): Program {
        return data.copy(id = null)
    }

}
