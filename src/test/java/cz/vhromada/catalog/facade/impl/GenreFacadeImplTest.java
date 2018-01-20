package cz.vhromada.catalog.facade.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.facade.CatalogParentFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.converter.Converter;

import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link GenreFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
class GenreFacadeImplTest extends AbstractParentFacadeTest<Genre, cz.vhromada.catalog.domain.Genre> {

    /**
     * Test method for {@link GenreFacadeImpl#GenreFacadeImpl(CatalogService, Converter, CatalogValidator)} with null service for genres.
     */
    @Test
    void constructor_NullGenreService() {
        assertThatThrownBy(() -> new GenreFacadeImpl(null, getConverter(), getCatalogValidator())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link GenreFacadeImpl#GenreFacadeImpl(CatalogService, Converter, CatalogValidator)} with null converter.
     */
    @Test
    void constructor_NullConverter() {
        assertThatThrownBy(() -> new GenreFacadeImpl(getCatalogService(), null, getCatalogValidator())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link GenreFacadeImpl#GenreFacadeImpl(CatalogService, Converter, CatalogValidator)} with null validator for genre.
     */
    @Test
    void constructor_NullGenreValidator() {
        assertThatThrownBy(() -> new GenreFacadeImpl(getCatalogService(), getConverter(), null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Override
    protected CatalogParentFacade<Genre> getCatalogParentFacade() {
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
