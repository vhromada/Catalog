package cz.vhromada.catalog.dao.impl.spring;

import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.dao.ShowDAO;
import cz.vhromada.catalog.dao.entities.Show;
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
 * A class represents test for class {@link cz.vhromada.catalog.dao.impl.ShowDAOImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testDAOContext.xml")
@Transactional
public class ShowDAOImplSpringTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * Instance of {@link ShowDAO}
     */
    @Autowired
    private ShowDAO showDAO;

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
        entityManager.createNativeQuery("ALTER SEQUENCE tv_shows_sq RESTART WITH 4").executeUpdate();
    }

    /**
     * Test method for {@link ShowDAO#getShows()}.
     */
    @Test
    public void testGetShows() {
        DeepAsserts.assertEquals(SpringEntitiesUtils.getShows(), showDAO.getShows());
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
    }

    /**
     * Test method for {@link ShowDAO#getShow(Integer)}.
     */
    @Test
    public void testGetShow() {
        for (int i = 1; i <= SpringUtils.SHOWS_COUNT; i++) {
            DeepAsserts.assertEquals(SpringEntitiesUtils.getShow(i), showDAO.getShow(i));
        }

        assertNull(showDAO.getShow(Integer.MAX_VALUE));

        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
    }

    /**
     * Test method for {@link ShowDAO#add(Show)}.
     */
    @Test
    public void testAdd() {
        final Show show = SpringEntitiesUtils.newShow(objectGenerator, entityManager);

        showDAO.add(show);

        DeepAsserts.assertNotNull(show.getId());
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT + 1, show.getId());
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, show.getPosition());
        final Show addedShow = SpringUtils.getShow(entityManager, SpringUtils.SHOWS_COUNT + 1);
        DeepAsserts.assertEquals(show, addedShow);
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT + 1, SpringUtils.getShowsCount(entityManager));
    }

    /**
     * Test method for {@link ShowDAO#update(Show)}.
     */
    @Test
    public void testUpdate() {
        final Show show = SpringEntitiesUtils.updateShow(1, objectGenerator, entityManager);

        showDAO.update(show);

        final Show updatedShow = SpringUtils.getShow(entityManager, 1);
        DeepAsserts.assertEquals(show, updatedShow);
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
    }

    /**
     * Test method for {@link ShowDAO#remove(Show)}.
     */
    @Test
    public void testRemove() {
        final Show show = SpringEntitiesUtils.newShow(objectGenerator, entityManager);
        entityManager.persist(show);
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT + 1, SpringUtils.getShowsCount(entityManager));

        showDAO.remove(show);

        assertNull(SpringUtils.getShow(entityManager, show.getId()));
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
    }

}
