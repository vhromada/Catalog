package com.github.vhromada.catalog.facade.impl

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.facade.GenreFacade
import com.github.vhromada.common.facade.AbstractParentFacade
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.service.ParentService
import com.github.vhromada.common.validator.Validator
import org.springframework.stereotype.Component

/**
 * A class represents implementation of facade for genres.
 *
 * @author Vladimir Hromada
 */
@Component("genreFacade")
class GenreFacadeImpl(
    genreService: ParentService<com.github.vhromada.catalog.domain.Genre>,
    mapper: Mapper<Genre, com.github.vhromada.catalog.domain.Genre>,
    genreValidator: Validator<Genre, com.github.vhromada.catalog.domain.Genre>
) : AbstractParentFacade<Genre, com.github.vhromada.catalog.domain.Genre>(parentService = genreService, mapper = mapper, validator = genreValidator), GenreFacade {

    @Suppress("DuplicatedCode")
    override fun updateData(data: Genre): Result<Unit> {
        val storedGenre = service.get(id = data.id!!)
        val validationResult = validator.validateExists(data = storedGenre)
        if (validationResult.isOk()) {
            val genre = mapper.map(source = data)
            genre.createdUser = storedGenre.get().createdUser
            genre.createdTime = storedGenre.get().createdTime
            service.update(data = genre)
        }
        return validationResult
    }

    override fun addData(data: Genre): Result<Unit> {
        service.add(data = mapper.map(source = data))
        return Result()
    }

}
