package cz.vhromada.catalog.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.GenreUtils;
import cz.vhromada.catalog.dao.GenreDAO;
import cz.vhromada.catalog.dao.entities.Genre;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration test for class {@link GenreDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testDAOContext.xml")
@Transactional
@Rollback
public class GenreDAOImplIntegrationTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * Instance of {@link GenreDAO}
     */
    @Autowired
    private GenreDAO genreDAO;

    /**
     * Test method for {@link GenreDAO#getGenres()}.
     */
    @Test
    public void testGetGenres() {
        final List<Genre> genres = genreDAO.getGenres();

        GenreUtils.assertGenresDeepEquals(GenreUtils.getGenres(), genres);

        assertEquals(GenreUtils.GENRES_COUNT, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for {@link GenreDAO#getGenre(Integer)}.
     */
    @Test
    public void testGetGenre() {
        for (int i = 1; i <= GenreUtils.GENRES_COUNT; i++) {
            final Genre genre = genreDAO.getGenre(i);

            assertNotNull(genre);
            GenreUtils.assertGenreDeepEquals(GenreUtils.getGenre(i), genre);
        }

        assertNull(genreDAO.getGenre(Integer.MAX_VALUE));

        assertEquals(GenreUtils.GENRES_COUNT, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for {@link GenreDAO#add(Genre)}.
     */
    @Test
    public void testAdd() {
        final Genre genre = GenreUtils.newGenre(null);

        genreDAO.add(genre);

        assertNotNull(genre.getId());
        assertEquals(GenreUtils.GENRES_COUNT + 1, genre.getId().intValue());
        assertEquals(GenreUtils.GENRES_COUNT, genre.getPosition());

        final Genre addedGenre = GenreUtils.getGenre(entityManager, GenreUtils.GENRES_COUNT + 1);
        GenreUtils.assertGenreDeepEquals(GenreUtils.newGenre(GenreUtils.GENRES_COUNT + 1), addedGenre);

        assertEquals(GenreUtils.GENRES_COUNT + 1, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for {@link GenreDAO#update(Genre)}.
     */
    @Test
    public void testUpdate() {
        final Genre genre = GenreUtils.updateGenre(1, entityManager);

        genreDAO.update(genre);

        final Genre updatedGenre = GenreUtils.getGenre(entityManager, 1);
        final Genre expectedUpdatedGenre = GenreUtils.getGenre(1);
        GenreUtils.updateGenre(expectedUpdatedGenre);
        expectedUpdatedGenre.setPosition(GenreUtils.POSITION);
        GenreUtils.assertGenreDeepEquals(expectedUpdatedGenre, updatedGenre);

        assertEquals(GenreUtils.GENRES_COUNT, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for {@link GenreDAO#remove(Genre)}.
     */
    @Test
    public void testRemove() {
        final Genre genre = GenreUtils.newGenre(null);
        entityManager.persist(genre);
        assertEquals(GenreUtils.GENRES_COUNT + 1, GenreUtils.getGenresCount(entityManager));

        genreDAO.remove(genre);

        assertNull(GenreUtils.getGenre(entityManager, genre.getId()));
        assertEquals(GenreUtils.GENRES_COUNT, GenreUtils.getGenresCount(entityManager));
    }

}
