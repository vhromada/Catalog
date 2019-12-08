package cz.vhromada.catalog.facade.impl

import com.nhaarman.mockitokotlin2.any
import cz.vhromada.catalog.entity.Genre
import cz.vhromada.catalog.utils.GenreUtils
import cz.vhromada.common.facade.MovableParentFacade
import cz.vhromada.common.test.facade.MovableParentFacadeTest

/**
 * A class represents test for class [GenreFacadeImpl].
 *
 * @author Vladimir Hromada
 */
class GenreFacadeImplTest : MovableParentFacadeTest<Genre, cz.vhromada.catalog.domain.Genre>() {

    override fun getFacade(): MovableParentFacade<Genre> {
        return GenreFacadeImpl(service, mapper, validator)
    }

    override fun newEntity(id: Int?): Genre {
        return GenreUtils.newGenre(id)
    }

    override fun newDomain(id: Int?): cz.vhromada.catalog.domain.Genre {
        return GenreUtils.newGenreDomain(id)
    }

    override fun anyDomain(): cz.vhromada.catalog.domain.Genre {
        return any()
    }

    override fun anyEntity(): Genre {
        return any()
    }

}
