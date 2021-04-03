package com.github.vhromada.catalog.facade.impl

import com.github.vhromada.catalog.entity.Season
import com.github.vhromada.catalog.entity.Show
import com.github.vhromada.catalog.facade.SeasonFacade
import com.github.vhromada.common.facade.AbstractChildFacade
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.service.ChildService
import com.github.vhromada.common.service.ParentService
import com.github.vhromada.common.validator.Validator
import org.springframework.stereotype.Component

/**
 * A class represents implementation of service for seasons.
 *
 * @author Vladimir Hromada
 */
@Component("seasonFacade")
class SeasonFacadeImpl(
    seasonService: ChildService<com.github.vhromada.catalog.domain.Season>,
    showService: ParentService<com.github.vhromada.catalog.domain.Show>,
    mapper: Mapper<Season, com.github.vhromada.catalog.domain.Season>,
    seasonValidator: Validator<Season, com.github.vhromada.catalog.domain.Season>,
    showValidator: Validator<Show, com.github.vhromada.catalog.domain.Show>
) : AbstractChildFacade<Season, com.github.vhromada.catalog.domain.Season, Show, com.github.vhromada.catalog.domain.Show>(
    childService = seasonService,
    parentService = showService,
    mapper = mapper,
    childValidator = seasonValidator,
    parentValidator = showValidator
), SeasonFacade {

    override fun updateData(data: Season): Result<Unit> {
        val storedSeason = service.get(id = data.id!!)
        val validationResult = validator.validateExists(data = storedSeason)
        if (validationResult.isOk()) {
            val season = mapper.map(source = data)
            season.createdUser = storedSeason.get().createdUser
            season.createdTime = storedSeason.get().createdTime
            season.show = storedSeason.get().show
            season.episodes.addAll(storedSeason.get().episodes)
            service.update(data = season)
        }
        return validationResult
    }

    override fun addData(parent: com.github.vhromada.catalog.domain.Show, data: Season): Result<Unit> {
        val season = mapper.map(source = data)
        season.show = parent
        service.add(data = season)
        return Result()
    }

    override fun getParent(data: com.github.vhromada.catalog.domain.Season): com.github.vhromada.catalog.domain.Show {
        return data.show!!
    }

}
