package cz.vhromada.catalog.facade.impl;

import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.utils.GenreUtils;

/**
 * A class represents test for class {@link GenreFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
public class GenreFacadeImplTest extends AbstractParentFacadeTest<Genre, cz.vhromada.catalog.domain.Genre> {

    @Override
    protected AbstractParentCatalogFacade<Genre, cz.vhromada.catalog.domain.Genre> getParentCatalogFacade() {
        return new GenreFacadeImpl(getCatalogService(), getConverter(), getCatalogValidator());
    }

    @Override
    protected Genre newEntity(final Integer id) {
        return GenreUtils.newGenre(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Genre newDomain(final Integer id) {
        return GenreUtils.newGenreDomain(id);
    }

    @Override
    protected Class<Genre> getEntityClass() {
        return Genre.class;
    }

    @Override
    protected Class<cz.vhromada.catalog.domain.Genre> getDomainClass() {
        return cz.vhromada.catalog.domain.Genre.class;
    }

}
