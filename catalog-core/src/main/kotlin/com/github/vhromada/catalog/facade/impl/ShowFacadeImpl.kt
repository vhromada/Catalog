package com.github.vhromada.catalog.facade.impl

import com.github.vhromada.catalog.entity.Show
import com.github.vhromada.catalog.facade.ShowFacade
import com.github.vhromada.common.entity.Time
import com.github.vhromada.common.facade.AbstractMovableParentFacade
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.provider.TimeProvider
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.service.MovableService
import com.github.vhromada.common.validator.MovableValidator
import org.springframework.stereotype.Component

/**
 * A class represents implementation of facade for shows.
 *
 * @author Vladimir Hromada
 */
@Component("showFacade")
class ShowFacadeImpl(
        showService: MovableService<com.github.vhromada.catalog.domain.Show>,
        accountProvider: AccountProvider,
        timeProvider: TimeProvider,
        mapper: Mapper<Show, com.github.vhromada.catalog.domain.Show>,
        showValidator: MovableValidator<Show>) : AbstractMovableParentFacade<Show, com.github.vhromada.catalog.domain.Show>(showService, accountProvider, timeProvider, mapper, showValidator), ShowFacade {

    override fun getTotalLength(): Result<Time> {
        return Result.of(Time(service.getAll().sumBy { it.seasons.sumBy { s -> s.episodes.sumBy { e -> e.length } } }))
    }

    override fun getSeasonsCount(): Result<Int> {
        return Result.of(service.getAll().sumBy { it.seasons.size })
    }

    override fun getEpisodesCount(): Result<Int> {
        return Result.of(service.getAll().sumBy { it.seasons.sumBy { s -> s.episodes.size } })
    }

    override fun getDataForUpdate(data: Show): com.github.vhromada.catalog.domain.Show {
        return super.getDataForUpdate(data).copy(seasons = service.get(data.id!!).get().seasons)
    }

}
