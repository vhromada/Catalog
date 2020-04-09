package cz.vhromada.catalog.facade.impl

import cz.vhromada.catalog.entity.Medium
import cz.vhromada.catalog.entity.Movie
import cz.vhromada.catalog.facade.MovieFacade
import cz.vhromada.common.entity.Time
import cz.vhromada.common.facade.AbstractMovableParentFacade
import cz.vhromada.common.mapper.Mapper
import cz.vhromada.common.provider.AccountProvider
import cz.vhromada.common.provider.TimeProvider
import cz.vhromada.common.result.Result
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.validator.MovableValidator
import org.springframework.stereotype.Component
import kotlin.math.min

/**
 * A class represents implementation of facade for movies.
 *
 * @author Vladimir Hromada
 */
@Component("movieFacade")
class MovieFacadeImpl(
        movieService: MovableService<cz.vhromada.catalog.domain.Movie>,
        accountProvider: AccountProvider,
        timeProvider: TimeProvider,
        mapper: Mapper<Movie, cz.vhromada.catalog.domain.Movie>,
        movieValidator: MovableValidator<Movie>
) : AbstractMovableParentFacade<Movie, cz.vhromada.catalog.domain.Movie>(movieService, accountProvider, timeProvider, mapper, movieValidator), MovieFacade {

    override fun getTotalMediaCount(): Result<Int> {
        return Result.of(service.getAll().sumBy { it.media.size })
    }

    override fun getTotalLength(): Result<Time> {
        return Result.of(Time(service.getAll().sumBy { it.media.sumBy { m -> m.length } }))
    }

    override fun getDataForAdd(data: Movie): cz.vhromada.catalog.domain.Movie {
        val movie = super.getDataForAdd(data)
        for (medium in movie.media) {
            medium.audit = getAudit()
        }

        return movie
    }

    override fun getDataForUpdate(data: Movie): cz.vhromada.catalog.domain.Movie {
        val movie = super.getDataForUpdate(data)
        val media = getUpdatedMedia(service.get(data.id!!)!!.media, data.media!!)

        return movie.copy(media = media)
    }

    /**
     * Updates media.
     *
     * @param originalMedia original media
     * @param updatedMedia  updated media
     * @return updated media
     */
    private fun getUpdatedMedia(originalMedia: List<cz.vhromada.catalog.domain.Medium>, updatedMedia: List<Medium?>): List<cz.vhromada.catalog.domain.Medium> {
        val result = mutableListOf<cz.vhromada.catalog.domain.Medium>()
        val audit = getAudit()

        var index = 0
        val max = min(originalMedia.size, updatedMedia.size)
        while (index < max) {
            val medium = getUpdatedMedium(updatedMedia, index)
            medium.id = originalMedia[index].id
            medium.audit = originalMedia[index].audit
            medium.modify(audit)
            result.add(medium)
            index++
        }
        while (index < updatedMedia.size) {
            val medium = getUpdatedMedium(updatedMedia, index)
            medium.audit = audit
            result.add(medium)
            index++
        }

        return result
    }

    /**
     * Returns updated medium.
     *
     * @param updatedMedia list of updated media
     * @param index        index
     * @return updated medium
     */
    private fun getUpdatedMedium(updatedMedia: List<Medium?>, index: Int): cz.vhromada.catalog.domain.Medium {
        return cz.vhromada.catalog.domain.Medium(id = null, number = index + 1, length = updatedMedia[index]!!.length!!, audit = null)
    }

}
