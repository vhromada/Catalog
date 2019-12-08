package cz.vhromada.catalog.facade.impl

import cz.vhromada.catalog.entity.Show
import cz.vhromada.catalog.facade.ShowFacade
import cz.vhromada.common.Time
import cz.vhromada.common.facade.AbstractMovableParentFacade
import cz.vhromada.common.mapper.Mapper
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.validator.MovableValidator
import cz.vhromada.validation.result.Result
import org.springframework.stereotype.Component

/**
 * A class represents implementation of facade for shows.
 *
 * @author Vladimir Hromada
 */
@Component("showFacade")
class ShowFacadeImpl(
        showService: MovableService<cz.vhromada.catalog.domain.Show>,
        mapper: Mapper<Show, cz.vhromada.catalog.domain.Show>,
        showValidator: MovableValidator<Show>) : AbstractMovableParentFacade<Show, cz.vhromada.catalog.domain.Show>(showService, mapper, showValidator), ShowFacade {

    override fun getTotalLength(): Result<Time> {
        return Result.of(Time(service.getAll().sumBy { it.seasons.sumBy { s -> s.episodes.sumBy { e -> e.length } } }))
    }

    override fun getSeasonsCount(): Result<Int> {
        return Result.of(service.getAll().sumBy { it.seasons.size })
    }

    override fun getEpisodesCount(): Result<Int> {
        return Result.of(service.getAll().sumBy { it.seasons.sumBy { s -> s.episodes.size } })
    }

    override fun getDataForUpdate(data: Show): cz.vhromada.catalog.domain.Show {
        return super.getDataForUpdate(data).copy(seasons = service.get(data.id!!)!!.seasons)
    }

}
