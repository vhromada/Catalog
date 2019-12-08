package cz.vhromada.catalog.facade.impl

import cz.vhromada.catalog.entity.Genre
import cz.vhromada.catalog.facade.GenreFacade
import cz.vhromada.common.facade.AbstractMovableParentFacade
import cz.vhromada.common.mapper.Mapper
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.validator.MovableValidator
import org.springframework.stereotype.Component

/**
 * A class represents implementation of facade for genres.
 *
 * @author Vladimir Hromada
 */
@Component("genreFacade")
class GenreFacadeImpl(
        genreService: MovableService<cz.vhromada.catalog.domain.Genre>,
        mapper: Mapper<Genre, cz.vhromada.catalog.domain.Genre>,
        genreValidator: MovableValidator<Genre>) : AbstractMovableParentFacade<Genre, cz.vhromada.catalog.domain.Genre>(genreService, mapper, genreValidator), GenreFacade
