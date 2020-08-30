package com.github.vhromada.catalog.facade.impl

import com.github.vhromada.catalog.entity.Medium
import com.github.vhromada.catalog.entity.Movie
import com.github.vhromada.catalog.facade.MovieFacade
import com.github.vhromada.common.entity.Time
import com.github.vhromada.common.facade.AbstractMovableParentFacade
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.provider.TimeProvider
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.service.MovableService
import com.github.vhromada.common.validator.MovableValidator
import org.springframework.stereotype.Component
import kotlin.math.min

/**
 * A class represents implementation of facade for movies.
 *
 * @author Vladimir Hromada
 */
@Component("movieFacade")
class MovieFacadeImpl(
        movieService: MovableService<com.github.vhromada.catalog.domain.Movie>,
        accountProvider: AccountProvider,
        timeProvider: TimeProvider,
        mapper: Mapper<Movie, com.github.vhromada.catalog.domain.Movie>,
        movieValidator: MovableValidator<Movie>
) : AbstractMovableParentFacade<Movie, com.github.vhromada.catalog.domain.Movie>(movieService, accountProvider, timeProvider, mapper, movieValidator), MovieFacade {

    override fun getTotalMediaCount(): Result<Int> {
        return Result.of(service.getAll().sumBy { it.media.size })
    }

    override fun getTotalLength(): Result<Time> {
        return Result.of(Time(service.getAll().sumBy { it.media.sumBy { m -> m.length } }))
    }

    override fun getDataForAdd(data: Movie): com.github.vhromada.catalog.domain.Movie {
        val movie = super.getDataForAdd(data)
        for (medium in movie.media) {
            medium.audit = getAudit()
        }

        return movie
    }

    override fun getDataForUpdate(data: Movie): com.github.vhromada.catalog.domain.Movie {
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
    private fun getUpdatedMedia(originalMedia: List<com.github.vhromada.catalog.domain.Medium>, updatedMedia: List<Medium?>): List<com.github.vhromada.catalog.domain.Medium> {
        val result = mutableListOf<com.github.vhromada.catalog.domain.Medium>()
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
    private fun getUpdatedMedium(updatedMedia: List<Medium?>, index: Int): com.github.vhromada.catalog.domain.Medium {
        return com.github.vhromada.catalog.domain.Medium(id = null, number = index + 1, length = updatedMedia[index]!!.length!!, audit = null)
    }

}
