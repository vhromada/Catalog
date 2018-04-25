package cz.vhromada.catalog.facade.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.common.facade.MovableParentFacade;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.test.facade.MovableParentFacadeTest;
import cz.vhromada.common.validator.MovableValidator;
import cz.vhromada.converter.Converter;

import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link GenreFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
class GenreFacadeImplTest extends MovableParentFacadeTest<Genre, cz.vhromada.catalog.domain.Genre> {

    /**
     * Test method for {@link GenreFacadeImpl#GenreFacadeImpl(MovableService, Converter, MovableValidator)} with null service for genres.
     */
    @Test
    void constructor_NullGenreService() {
        assertThatThrownBy(() -> new GenreFacadeImpl(null, getConverter(), getMovableValidator())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link GenreFacadeImpl#GenreFacadeImpl(MovableService, Converter, MovableValidator)} with null converter.
     */
    @Test
    void constructor_NullConverter() {
        assertThatThrownBy(() -> new GenreFacadeImpl(getMovableService(), null, getMovableValidator())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link GenreFacadeImpl#GenreFacadeImpl(MovableService, Converter, MovableValidator)} with null validator for genre.
     */
    @Test
    void constructor_NullGenreValidator() {
        assertThatThrownBy(() -> new GenreFacadeImpl(getMovableService(), getConverter(), null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Override
    protected MovableParentFacade<Genre> getMovableParentFacade() {
        return new GenreFacadeImpl(getMovableService(), getConverter(), getMovableValidator());
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
