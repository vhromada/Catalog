package cz.vhromada.catalog.dao.impl.spring;

import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.dao.GenreDAO;
import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.generator.ObjectGenerator;
import cz.vhromada.test.DeepAsserts;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents test for class {@link cz.vhromada.catalog.dao.impl.GenreDAOImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testDAOContext.xml")
@Transactional
public class GenreDAOImplSpringTest {

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
     * Instance of {@link ObjectGenerator}
     */
    @Autowired
    private ObjectGenerator objectGenerator;

    /**
     * Restarts sequence.
     */
    @Before
    public void setUp() {
        entityManager.createNativeQuery("ALTER SEQUENCE genres_sq RESTART WITH 5").executeUpdate();
    }

    /**
     * Test method for {@link GenreDAO#getGenres()}.
     */
    @Test
    public void testGetGenres() {
        DeepAsserts.assertEquals(SpringEntitiesUtils.getGenres(), genreDAO.getGenres());
        DeepAsserts.assertEquals(SpringUtils.GENRES_COUNT, SpringUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for {@link GenreDAO#getGenre(Integer)}.
     */
    @Test
    public void testGetGenre() {
        for (int i = 1; i <= SpringUtils.GENRES_COUNT; i++) {
            DeepAsserts.assertEquals(SpringEntitiesUtils.getGenre(i), genreDAO.getGenre(i));
        }

        assertNull(genreDAO.getGenre(Integer.MAX_VALUE));

        DeepAsserts.assertEquals(SpringUtils.GENRES_COUNT, SpringUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for {@link GenreDAO#add(Genre)}.
     */
    @Test
    public void testAdd() {
        final Genre genre = SpringEntitiesUtils.newGenre(objectGenerator);

        genreDAO.add(genre);

        DeepAsserts.assertNotNull(genre.getId());
        DeepAsserts.assertEquals(SpringUtils.GENRES_COUNT + 1, genre.getId());
        final Genre addedGenre = SpringUtils.getGenre(entityManager, SpringUtils.GENRES_COUNT + 1);
        DeepAsserts.assertEquals(genre, addedGenre);
        DeepAsserts.assertEquals(SpringUtils.GENRES_COUNT + 1, SpringUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for {@link GenreDAO#update(Genre)}.
     */
    @Test
    public void testUpdate() {
        final Genre genre = SpringEntitiesUtils.updateGenre(1, objectGenerator, entityManager);

        genreDAO.update(genre);

        final Genre updatedGenre = SpringUtils.getGenre(entityManager, 1);
        DeepAsserts.assertEquals(genre, updatedGenre);
        DeepAsserts.assertEquals(SpringUtils.GENRES_COUNT, SpringUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for {@link GenreDAO#remove(Genre)}.
     */
    @Test
    public void testRemove() {
        final Genre genre = SpringEntitiesUtils.newGenre(objectGenerator);
        entityManager.persist(genre);
        DeepAsserts.assertEquals(SpringUtils.GENRES_COUNT + 1, SpringUtils.getGenresCount(entityManager));

        genreDAO.remove(genre);

        assertNull(SpringUtils.getGenre(entityManager, genre.getId()));
        DeepAsserts.assertEquals(SpringUtils.GENRES_COUNT, SpringUtils.getGenresCount(entityManager));
    }

}
