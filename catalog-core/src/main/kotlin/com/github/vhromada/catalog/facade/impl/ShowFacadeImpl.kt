package com.github.vhromada.catalog.facade.impl

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.entity.Picture
import com.github.vhromada.catalog.entity.Show
import com.github.vhromada.catalog.facade.ShowFacade
import com.github.vhromada.common.entity.Time
import com.github.vhromada.common.facade.AbstractParentFacade
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.service.ParentService
import com.github.vhromada.common.validator.Validator
import org.springframework.stereotype.Component

/**
 * A class represents implementation of facade for shows.
 *
 * @author Vladimir Hromada
 */
@Component("showFacade")
class ShowFacadeImpl(
    private val showService: ParentService<com.github.vhromada.catalog.domain.Show>,
    private val pictureService: ParentService<com.github.vhromada.catalog.domain.Picture>,
    private val genreService: ParentService<com.github.vhromada.catalog.domain.Genre>,
    mapper: Mapper<Show, com.github.vhromada.catalog.domain.Show>,
    showValidator: Validator<Show, com.github.vhromada.catalog.domain.Show>,
    private val pictureValidator: Validator<Picture, com.github.vhromada.catalog.domain.Picture>,
    private val genreValidator: Validator<Genre, com.github.vhromada.catalog.domain.Genre>
) : AbstractParentFacade<Show, com.github.vhromada.catalog.domain.Show>(parentService = showService, mapper = mapper, validator = showValidator), ShowFacade {

    override fun updateData(data: Show): Result<Unit> {
        val storedShow = showService.get(id = data.id!!)
        val validationResult = validator.validateExists(data = storedShow)
        val pictureValidationResult = getPicture(show = data)
        val genresValidationResult = getGenres(show = data)
        if (validationResult.isOk() && pictureValidationResult.isOk() && genresValidationResult.isOk()) {
            val show = mapper.map(source = data)
                .copy(seasons = storedShow.get().seasons)
            show.createdUser = storedShow.get().createdUser
            show.createdTime = storedShow.get().createdTime
            showService.update(data = show)
        }
        return Result.of(validationResult, pictureValidationResult, genresValidationResult)
    }

    @Suppress("DuplicatedCode")
    override fun addData(data: Show): Result<Unit> {
        val pictureValidationResult = getPicture(show = data)
        val genresValidationResult = getGenres(show = data)
        if (pictureValidationResult.isOk() && genresValidationResult.isOk()) {
            val show = mapper.map(source = data)
                .copy(genres = genresValidationResult.data!!)
            showService.add(data = show)
        }
        return Result.of(pictureValidationResult, genresValidationResult)
    }

    override fun getTotalLength(): Result<Time> {
        return Result.of(data = Time(length = showService.getAll().sumBy { it.seasons.sumBy { s -> s.episodes.sumBy { e -> e.length } } }))
    }

    override fun getSeasonsCount(): Result<Int> {
        return Result.of(data = showService.getAll().sumBy { it.seasons.size })
    }

    override fun getEpisodesCount(): Result<Int> {
        return Result.of(data = showService.getAll().sumBy { it.seasons.sumBy { s -> s.episodes.size } })
    }

    /**
     * Returns result with picture and validation errors.
     *
     * @param show show
     * @returns result with picture and validation errors
     */
    @Suppress("DuplicatedCode")
    private fun getPicture(show: Show): Result<com.github.vhromada.catalog.domain.Picture> {
        if (show.picture == null) {
            return Result()
        }
        val picture = pictureService.get(id = show.picture)
        val validationResult = pictureValidator.validateExists(data = picture)
        return if (validationResult.isOk()) {
            Result.of(data = picture.get())
        } else {
            Result.of(validationResult)
        }
    }

    /**
     * Returns result with genres and validation errors.
     *
     * @param show show
     * @returns result with genres and validation errors
     */
    @Suppress("DuplicatedCode")
    private fun getGenres(show: Show): Result<MutableList<com.github.vhromada.catalog.domain.Genre>> {
        val genres = mutableListOf<com.github.vhromada.catalog.domain.Genre>()
        val result = Result.of(data = genres)
        show.genres!!.filterNotNull()
            .map { genreService.get(id = it.id!!) }
            .forEach {
                val validationResult = genreValidator.validateExists(data = it)
                if (validationResult.isOk()) {
                    genres.add(it.get())
                } else {
                    result.addEvents(eventList = validationResult.events())
                }
            }
        return result
    }

}
