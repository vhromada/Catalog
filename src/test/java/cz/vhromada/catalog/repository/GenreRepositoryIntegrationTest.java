package cz.vhromada.catalog.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.domain.Genre;
import cz.vhromada.catalog.utils.GenreUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration test for class {@link GenreRepository}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testCatalogContext.xml")
@Transactional
@Rollback
public class GenreRepositoryIntegrationTest {

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
    public void testGetGenres() {
        final List<Genre> genres = genreRepository.findAll(new Sort("position", "id"));

        GenreUtils.assertGenresDeepEquals(GenreUtils.getGenres(), genres);

        assertEquals(GenreUtils.GENRES_COUNT, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for get genre.
     */
    @Test
    public void testGetGenre() {
        for (int i = 1; i <= GenreUtils.GENRES_COUNT; i++) {
            final Genre genre = genreRepository.findOne(i);

            GenreUtils.assertGenreDeepEquals(GenreUtils.getGenreDomain(i), genre);
        }

        assertNull(genreRepository.findOne(Integer.MAX_VALUE));

        assertEquals(GenreUtils.GENRES_COUNT, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for add genre.
     */
    @Test
    public void testAdd() {
        final Genre genre = GenreUtils.newGenreDomain(null);

        genreRepository.saveAndFlush(genre);

        assertNotNull(genre.getId());
        assertEquals(GenreUtils.GENRES_COUNT + 1, genre.getId().intValue());

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
    public void testUpdate() {
        final Genre genre = GenreUtils.updateGenre(entityManager, 1);

        genreRepository.saveAndFlush(genre);

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
    public void testRemove() {
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
    public void testRemoveAll() {
        entityManager.createNativeQuery("DELETE FROM movie_genres").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM tv_show_genres").executeUpdate();

        genreRepository.deleteAll();

        assertEquals(0, GenreUtils.getGenresCount(entityManager));
    }

}
