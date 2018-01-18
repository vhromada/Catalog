package cz.vhromada.catalog.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.domain.Genre;
import cz.vhromada.catalog.utils.GenreUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration test for class {@link GenreRepository}.
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
@Transactional
@Rollback
class GenreRepositoryIntegrationTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private EntityManager entityManager;

    /**
     * Instance of {@link GenreRepository}
     */
    @Autowired
    private GenreRepository genreRepository;

    /**
     * Test method for get genres.
     */
    @Test
    void getGenres() {
        final List<Genre> genres = genreRepository.findAll();

        GenreUtils.assertGenresDeepEquals(GenreUtils.getGenres(), genres);

        assertEquals(GenreUtils.GENRES_COUNT, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for get genre.
     */
    @Test
    void getGenre() {
        for (int i = 1; i <= GenreUtils.GENRES_COUNT; i++) {
            final Genre genre = genreRepository.findById(i).orElse(null);

            GenreUtils.assertGenreDeepEquals(GenreUtils.getGenreDomain(i), genre);
        }

        assertFalse(genreRepository.findById(Integer.MAX_VALUE).isPresent());

        assertEquals(GenreUtils.GENRES_COUNT, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for add genre.
     */
    @Test
    void add() {
        final Genre genre = GenreUtils.newGenreDomain(null);

        genreRepository.save(genre);

        assertEquals(Integer.valueOf(GenreUtils.GENRES_COUNT + 1), genre.getId());

        final Genre addedGenre = GenreUtils.getGenre(entityManager, GenreUtils.GENRES_COUNT + 1);
        final Genre expectedAddGenre = GenreUtils.newGenreDomain(null);
        expectedAddGenre.setId(GenreUtils.GENRES_COUNT + 1);
        GenreUtils.assertGenreDeepEquals(expectedAddGenre, addedGenre);

        assertEquals(GenreUtils.GENRES_COUNT + 1, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for update genre.
     */
    @Test
    void update() {
        final Genre genre = GenreUtils.updateGenre(entityManager, 1);

        genreRepository.save(genre);

        final Genre updatedGenre = GenreUtils.getGenre(entityManager, 1);
        final Genre expectedUpdatedGenre = GenreUtils.getGenreDomain(1);
        GenreUtils.updateGenre(expectedUpdatedGenre);
        expectedUpdatedGenre.setPosition(GenreUtils.POSITION);
        GenreUtils.assertGenreDeepEquals(expectedUpdatedGenre, updatedGenre);

        assertEquals(GenreUtils.GENRES_COUNT, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for remove genre.
     */
    @Test
    @DirtiesContext
    void remove() {
        final Genre genre = GenreUtils.newGenreDomain(null);
        entityManager.persist(genre);
        assertEquals(GenreUtils.GENRES_COUNT + 1, GenreUtils.getGenresCount(entityManager));

        genreRepository.delete(genre);

        assertNull(GenreUtils.getGenre(entityManager, genre.getId()));

        assertEquals(GenreUtils.GENRES_COUNT, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for remove all genres.
     */
    @Test
    void removeAll() {
        entityManager.createNativeQuery("DELETE FROM movie_genres").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM tv_show_genres").executeUpdate();

        genreRepository.deleteAll();

        assertEquals(0, GenreUtils.getGenresCount(entityManager));
    }

}
