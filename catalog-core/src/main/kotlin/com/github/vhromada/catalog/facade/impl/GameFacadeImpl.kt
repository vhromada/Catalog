package com.github.vhromada.catalog.facade.impl

import com.github.vhromada.catalog.entity.Game
import com.github.vhromada.catalog.facade.GameFacade
import com.github.vhromada.common.facade.AbstractMovableParentFacade
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.provider.TimeProvider
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.service.MovableService
import com.github.vhromada.common.validator.MovableValidator
import org.springframework.stereotype.Component

/**
 * A class represents implementation of facade for games.
 *
 * @author Vladimir Hromada
 */
@Component("gameFacade")
class GameFacadeImpl(
        gameService: MovableService<com.github.vhromada.catalog.domain.Game>,
        accountProvider: AccountProvider,
        timeProvider: TimeProvider,
        mapper: Mapper<Game, com.github.vhromada.catalog.domain.Game>,
        gameValidator: MovableValidator<Game>) : AbstractMovableParentFacade<Game, com.github.vhromada.catalog.domain.Game>(gameService, accountProvider, timeProvider, mapper, gameValidator), GameFacade {

    override fun getTotalMediaCount(): Result<Int> {
        return Result.of(service.getAll().sumBy { it.mediaCount })
    }

}
