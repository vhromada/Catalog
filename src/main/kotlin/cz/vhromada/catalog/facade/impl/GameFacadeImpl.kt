package cz.vhromada.catalog.facade.impl

import cz.vhromada.catalog.entity.Game
import cz.vhromada.catalog.facade.GameFacade
import cz.vhromada.common.facade.AbstractMovableParentFacade
import cz.vhromada.common.mapper.Mapper
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.validator.MovableValidator
import cz.vhromada.validation.result.Result
import org.springframework.stereotype.Component

/**
 * A class represents implementation of facade for games.
 *
 * @author Vladimir Hromada
 */
@Component("gameFacade")
class GameFacadeImpl(
        gameService: MovableService<cz.vhromada.catalog.domain.Game>,
        mapper: Mapper<Game, cz.vhromada.catalog.domain.Game>,
        gameValidator: MovableValidator<Game>) : AbstractMovableParentFacade<Game, cz.vhromada.catalog.domain.Game>(gameService, mapper, gameValidator), GameFacade {

    override fun getTotalMediaCount(): Result<Int> {
        return Result.of(service.getAll().sumBy { it.mediaCount })
    }

}