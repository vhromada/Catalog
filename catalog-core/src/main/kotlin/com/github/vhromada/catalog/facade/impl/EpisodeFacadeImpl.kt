package com.github.vhromada.catalog.facade.impl

import com.github.vhromada.catalog.entity.Episode
import com.github.vhromada.catalog.entity.Season
import com.github.vhromada.catalog.facade.EpisodeFacade
import com.github.vhromada.common.facade.AbstractChildFacade
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.service.ChildService
import com.github.vhromada.common.validator.Validator
import org.springframework.stereotype.Component

/**
 * A class represents implementation of service for episodes.
 *
 * @author Vladimir Hromada
 */
@Component("episodeFacade")
class EpisodeFacadeImpl(
    episodeService: ChildService<com.github.vhromada.catalog.domain.Episode>,
    seasonService: ChildService<com.github.vhromada.catalog.domain.Season>,
    mapper: Mapper<Episode, com.github.vhromada.catalog.domain.Episode>,
    episodeValidator: Validator<Episode, com.github.vhromada.catalog.domain.Episode>,
    seasonValidator: Validator<Season, com.github.vhromada.catalog.domain.Season>
) : AbstractChildFacade<Episode, com.github.vhromada.catalog.domain.Episode, Season, com.github.vhromada.catalog.domain.Season>(
    childService = episodeService,
    parentService = seasonService,
    mapper = mapper,
    childValidator = episodeValidator,
    parentValidator = seasonValidator
), EpisodeFacade {

    @Suppress("DuplicatedCode")
    override fun updateData(data: Episode): Result<Unit> {
        val storedEpisode = service.get(id = data.id!!)
        val validationResult = validator.validateExists(data = storedEpisode)
        if (validationResult.isOk()) {
            val episode = mapper.map(source = data)
            episode.createdUser = storedEpisode.get().createdUser
            episode.createdTime = storedEpisode.get().createdTime
            episode.season = storedEpisode.get().season
            service.update(data = episode)
        }
        return validationResult
    }

    override fun addData(parent: com.github.vhromada.catalog.domain.Season, data: Episode): Result<Unit> {
        val episode = mapper.map(source = data)
        episode.season = parent
        service.add(data = episode)
        return Result()
    }

    override fun getParent(data: com.github.vhromada.catalog.domain.Episode): com.github.vhromada.catalog.domain.Season {
        return data.season!!
    }

}
