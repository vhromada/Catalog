package com.github.vhromada.catalog.facade.impl

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.entity.Medium
import com.github.vhromada.catalog.entity.Movie
import com.github.vhromada.catalog.entity.Picture
import com.github.vhromada.catalog.facade.MovieFacade
import com.github.vhromada.common.entity.Time
import com.github.vhromada.common.facade.AbstractParentFacade
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.service.ParentService
import com.github.vhromada.common.validator.Validator
import org.springframework.stereotype.Component
import kotlin.math.min

/**
 * A class represents implementation of facade for movies.
 *
 * @author Vladimir Hromada
 */
@Component("movieFacade")
class MovieFacadeImpl(
    private val movieService: ParentService<com.github.vhromada.catalog.domain.Movie>,
    private val pictureService: ParentService<com.github.vhromada.catalog.domain.Picture>,
    private val genreService: ParentService<com.github.vhromada.catalog.domain.Genre>,
    mapper: Mapper<Movie, com.github.vhromada.catalog.domain.Movie>,
    movieValidator: Validator<Movie, com.github.vhromada.catalog.domain.Movie>,
    private val pictureValidator: Validator<Picture, com.github.vhromada.catalog.domain.Picture>,
    private val genreValidator: Validator<Genre, com.github.vhromada.catalog.domain.Genre>
) : AbstractParentFacade<Movie, com.github.vhromada.catalog.domain.Movie>(parentService = movieService, mapper = mapper, validator = movieValidator), MovieFacade {

    override fun updateData(data: Movie): Result<Unit> {
        val storedMovie = movieService.get(id = data.id!!)
        val validationResult = validator.validateExists(data = storedMovie)
        val pictureValidationResult = getPicture(movie = data)
        val genresValidationResult = getGenres(movie = data)
        if (validationResult.isOk() && pictureValidationResult.isOk() && genresValidationResult.isOk()) {
            val movie = mapper.map(source = data)
                .copy(media = getUpdatedMedia(originalMedia = storedMovie.get().media, updatedMedia = data.media!!))
            movie.createdUser = storedMovie.get().createdUser
            movie.createdTime = storedMovie.get().createdTime
            movieService.update(data = movie)
        }
        return Result.of(validationResult, pictureValidationResult, genresValidationResult)
    }

    @Suppress("DuplicatedCode")
    override fun addData(data: Movie): Result<Unit> {
        val pictureValidationResult = getPicture(movie = data)
        val genresValidationResult = getGenres(movie = data)
        if (pictureValidationResult.isOk() && genresValidationResult.isOk()) {
            val movie = mapper.map(source = data)
                .copy(genres = genresValidationResult.data!!)
            movieService.add(data = movie)
        }
        return Result.of(pictureValidationResult, genresValidationResult)
    }

    override fun getTotalMediaCount(): Result<Int> {
        return Result.of(data = movieService.getAll().sumBy { it.media.size })
    }

    override fun getTotalLength(): Result<Time> {
        return Result.of(data = Time(length = movieService.getAll().sumBy { it.media.sumBy { m -> m.length } }))
    }

    /**
     * Returns result with picture and validation errors.
     *
     * @param movie movie
     * @returns result with picture and validation errors
     */
    @Suppress("DuplicatedCode")
    private fun getPicture(movie: Movie): Result<com.github.vhromada.catalog.domain.Picture> {
        if (movie.picture == null) {
            return Result()
        }
        val picture = pictureService.get(id = movie.picture)
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
     * @param movie movie
     * @returns result with genres and validation errors
     */
    @Suppress("DuplicatedCode")
    private fun getGenres(movie: Movie): Result<MutableList<com.github.vhromada.catalog.domain.Genre>> {
        val genres = mutableListOf<com.github.vhromada.catalog.domain.Genre>()
        val result = Result.of(genres)
        movie.genres!!.filterNotNull()
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

    /**
     * Updates media.
     *
     * @param originalMedia original media
     * @param updatedMedia  updated media
     * @return updated media
     */
    private fun getUpdatedMedia(originalMedia: List<com.github.vhromada.catalog.domain.Medium>, updatedMedia: List<Medium?>): List<com.github.vhromada.catalog.domain.Medium> {
        val result = mutableListOf<com.github.vhromada.catalog.domain.Medium>()

        var index = 0
        val max = min(originalMedia.size, updatedMedia.size)
        while (index < max) {
            val originalMedium = originalMedia[index]
            val updatedMedium = updatedMedia[index]!!
            val medium = com.github.vhromada.catalog.domain.Medium(id = originalMedium.id, number = index + 1, length = updatedMedium.length!!)
            medium.createdUser = originalMedium.createdUser
            medium.createdTime = originalMedium.createdTime
            result.add(medium)
            index++
        }
        while (index < updatedMedia.size) {
            val medium = com.github.vhromada.catalog.domain.Medium(id = null, number = index + 1, length = updatedMedia[index]!!.length!!)
            result.add(medium)
            index++
        }

        return result
    }

}
