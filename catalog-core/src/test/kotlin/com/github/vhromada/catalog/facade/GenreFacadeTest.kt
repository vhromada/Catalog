package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.facade.impl.GenreFacadeImpl
import com.github.vhromada.catalog.utils.GenreUtils
import com.github.vhromada.common.facade.MovableParentFacade
import com.github.vhromada.common.test.facade.MovableParentFacadeTest
import com.nhaarman.mockitokotlin2.any

/**
 * A class represents test for class [GenreFacade].
 *
 * @author Vladimir Hromada
 */
class GenreFacadeTest : MovableParentFacadeTest<Genre, com.github.vhromada.catalog.domain.Genre>() {

    override fun getFacade(): MovableParentFacade<Genre> {
        return GenreFacadeImpl(service, accountProvider, timeProvider, mapper, validator)
    }

    override fun newEntity(id: Int?): Genre {
        return GenreUtils.newGenre(id)
    }

    override fun newDomain(id: Int?): com.github.vhromada.catalog.domain.Genre {
        return GenreUtils.newGenreDomain(id)
    }

    override fun anyDomain(): com.github.vhromada.catalog.domain.Genre {
        return any()
    }

    override fun anyEntity(): Genre {
        return any()
    }

}
