package cz.vhromada.catalog.service.impl;

import cz.vhromada.catalog.commons.GenreUtils;
import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.repository.GenreRepository;
import cz.vhromada.catalog.service.CatalogService;

import org.junit.Test;
import org.mockito.Mock;
import org.springframework.cache.Cache;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A class represents test for class {@link GenreServiceImpl}.
 *
 * @author Vladimir Hromada
 */
public class GenreServiceImplTest extends AbstractServiceTest<Genre> {

    /**
     * Instance of {@link GenreRepository}
     */
    @Mock
    private GenreRepository genreRepository;

    /**
     * Test method for {@link GenreServiceImpl#GenreServiceImpl(GenreRepository, Cache)} with null repository for genres.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullGenreDAO() {
        new GenreServiceImpl(null, getCache());
    }

    /**
     * Test method for {@link GenreServiceImpl#GenreServiceImpl(GenreRepository, Cache)} with null cache for genres.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullGenreCache() {
        new GenreServiceImpl(genreRepository, null);
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
        return GenreUtils.newGenre(1);
    }

    @Override
    protected Genre getItem2() {
        return GenreUtils.newGenre(2);
    }

    @Override
    protected Genre getAddItem() {
        return GenreUtils.newGenre(null);
    }

    @Override
    protected Genre getCopyItem() {
        final Genre genre = GenreUtils.newGenre(null);
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
