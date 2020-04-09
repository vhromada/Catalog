package cz.vhromada.catalog.facade

import com.nhaarman.mockitokotlin2.any
import cz.vhromada.catalog.entity.Genre
import cz.vhromada.catalog.facade.impl.GenreFacadeImpl
import cz.vhromada.catalog.utils.GenreUtils
import cz.vhromada.common.facade.MovableParentFacade
import cz.vhromada.common.test.facade.MovableParentFacadeTest

/**
 * A class represents test for class [GenreFacade].
 *
 * @author Vladimir Hromada
 */
class GenreFacadeTest : MovableParentFacadeTest<Genre, cz.vhromada.catalog.domain.Genre>() {

    override fun getFacade(): MovableParentFacade<Genre> {
        return GenreFacadeImpl(service, accountProvider, timeProvider, mapper, validator)
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
