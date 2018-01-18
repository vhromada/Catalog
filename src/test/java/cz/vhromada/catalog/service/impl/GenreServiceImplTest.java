package cz.vhromada.catalog.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;

import cz.vhromada.catalog.domain.Genre;
import cz.vhromada.catalog.repository.GenreRepository;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.GenreUtils;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.cache.Cache;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A class represents test for class {@link GenreServiceImpl}.
 *
 * @author Vladimir Hromada
 */
class GenreServiceImplTest extends AbstractServiceTest<Genre> {

    /**
     * Instance of {@link GenreRepository}
     */
    @Mock
    private GenreRepository genreRepository;

    /**
     * Test method for {@link GenreServiceImpl#GenreServiceImpl(GenreRepository, Cache)} with null repository for genres.
     */
    @Test
    void constructor_NullGenreRepository() {
        assertThrows(IllegalArgumentException.class, () -> new GenreServiceImpl(null, getCache()));
    }

    /**
     * Test method for {@link GenreServiceImpl#GenreServiceImpl(GenreRepository, Cache)} with null cache.
     */
    @Test
    void constructor_NullCache() {
        assertThrows(IllegalArgumentException.class, () -> new GenreServiceImpl(genreRepository, null));
    }

    @Override
    protected JpaRepository<Genre, Integer> getRepository() {
        return genreRepository;
    }

    @Override
    protected CatalogService<Genre> getCatalogService() {
        return new GenreServiceImpl(genreRepository, getCache());
    }

    @Override
    protected String getCacheKey() {
        return "genres";
    }

    @Override
    protected Genre getItem1() {
        return GenreUtils.newGenreDomain(1);
    }

    @Override
    protected Genre getItem2() {
        return GenreUtils.newGenreDomain(2);
    }

    @Override
    protected Genre getAddItem() {
        return GenreUtils.newGenreDomain(null);
    }

    @Override
    protected Genre getCopyItem() {
        final Genre genre = GenreUtils.newGenreDomain(null);
        genre.setPosition(0);

        return genre;
    }

    @Override
    protected Class<Genre> getItemClass() {
        return Genre.class;
    }

    @Override
    protected void assertDataDeepEquals(final Genre expected, final Genre actual) {
        GenreUtils.assertGenreDeepEquals(expected, actual);
    }

}
