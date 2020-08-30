package com.github.vhromada.catalog.facade.impl

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.facade.GenreFacade
import com.github.vhromada.common.facade.AbstractMovableParentFacade
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.provider.TimeProvider
import com.github.vhromada.common.service.MovableService
import com.github.vhromada.common.validator.MovableValidator
import org.springframework.stereotype.Component

/**
 * A class represents implementation of facade for genres.
 *
 * @author Vladimir Hromada
 */
@Component("genreFacade")
class GenreFacadeImpl(
        genreService: MovableService<com.github.vhromada.catalog.domain.Genre>,
        accountProvider: AccountProvider,
        timeProvider: TimeProvider,
        mapper: Mapper<Genre, com.github.vhromada.catalog.domain.Genre>,
        genreValidator: MovableValidator<Genre>
) : AbstractMovableParentFacade<Genre, com.github.vhromada.catalog.domain.Genre>(genreService, accountProvider, timeProvider, mapper, genreValidator), GenreFacade
