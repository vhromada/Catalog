package cz.vhromada.catalog.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cz.vhromada.catalog.domain.Genre;
import cz.vhromada.catalog.repository.GenreRepository;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.test.service.MovableServiceTest;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.cache.Cache;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A class represents test for class {@link GenreService}.
 *
 * @author Vladimir Hromada
 */
class GenreServiceTest extends MovableServiceTest<Genre> {

    /**
     * Instance of {@link GenreRepository}
     */
    @Mock
    private GenreRepository genreRepository;

    /**
     * Test method for {@link GenreService#GenreService(GenreRepository, Cache)} with null repository for genres.
     */
    @Test
    void constructor_NullGenreRepository() {
        assertThatThrownBy(() -> new GenreService(null, getCache())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link GenreService#GenreService(GenreRepository, Cache)} with null cache.
     */
    @Test
    void constructor_NullCache() {
        assertThatThrownBy(() -> new GenreService(genreRepository, null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Override
    protected JpaRepository<Genre, Integer> getRepository() {
        return genreRepository;
    }

    @Override
    protected MovableService<Genre> getMovableService() {
        return new GenreService(genreRepository, getCache());
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
