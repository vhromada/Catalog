package cz.vhromada.catalog.facade.impl;

import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.converters.Converter;

import org.junit.Test;

/**
 * A class represents test for class {@link GenreFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
public class GenreFacadeImplTest extends AbstractParentFacadeTest<Genre, cz.vhromada.catalog.domain.Genre> {

    /**
     * Test method for {@link GenreFacadeImpl#GenreFacadeImpl(CatalogService, Converter, CatalogValidator)} with null service for genres.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullGenreService() {
        new GenreFacadeImpl(null, getConverter(), getCatalogValidator());
    }

    /**
     * Test method for {@link GenreFacadeImpl#GenreFacadeImpl(CatalogService, Converter, CatalogValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullConverter() {
        new GenreFacadeImpl(getCatalogService(), null, getCatalogValidator());
    }

    /**
     * Test method for {@link GenreFacadeImpl#GenreFacadeImpl(CatalogService, Converter, CatalogValidator)} with null validator for genre.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullGenreValidator() {
        new GenreFacadeImpl(getCatalogService(), getConverter(), null);
    }

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
