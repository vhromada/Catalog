package cz.vhromada.catalog.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.GenreUtils;
import cz.vhromada.catalog.commons.ShowUtils;
import cz.vhromada.catalog.dao.ShowDAO;
import cz.vhromada.catalog.dao.entities.Show;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration test for class {@link ShowDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testDAOContext.xml")
@Transactional
@Rollback
public class ShowDAOImplIntegrationTest {

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
     * Test method for {@link ShowDAO#getShows()}.
     */
    @Test
    public void testGetShows() {
        final List<Show> shows = showDAO.getShows();

        ShowUtils.assertShowsDeepEquals(ShowUtils.getShows(), shows);

        assertEquals(ShowUtils.SHOWS_COUNT, ShowUtils.getShowsCount(entityManager));
    }

    /**
     * Test method for {@link ShowDAO#getShow(Integer)}.
     */
    @Test
    public void testGetShow() {
        for (int i = 1; i <= ShowUtils.SHOWS_COUNT; i++) {
            final Show show = showDAO.getShow(i);

            assertNotNull(show);
            ShowUtils.assertShowDeepEquals(ShowUtils.getShow(i), show);
        }

        assertNull(showDAO.getShow(Integer.MAX_VALUE));

        assertEquals(ShowUtils.SHOWS_COUNT, ShowUtils.getShowsCount(entityManager));
    }

    /**
     * Test method for {@link ShowDAO#add(Show)}.
     */
    @Test
    public void testAdd() {
        final Show show = ShowUtils.newShow(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.getGenre(entityManager, 1)));

        showDAO.add(show);

        assertNotNull(show.getId());
        assertEquals(ShowUtils.SHOWS_COUNT + 1, show.getId().intValue());
        assertEquals(ShowUtils.SHOWS_COUNT, show.getPosition());

        final Show addedShow = ShowUtils.getShow(entityManager, ShowUtils.SHOWS_COUNT + 1);
        final Show expectedAddedShow = ShowUtils.newShow(ShowUtils.SHOWS_COUNT + 1);
        expectedAddedShow.setGenres(CollectionUtils.newList(GenreUtils.getGenre(entityManager, 1)));
        ShowUtils.assertShowDeepEquals(expectedAddedShow, addedShow);

        assertEquals(ShowUtils.SHOWS_COUNT + 1, ShowUtils.getShowsCount(entityManager));
    }

    /**
     * Test method for {@link ShowDAO#update(Show)}.
     */
    @Test
    public void testUpdate() {
        final Show show = ShowUtils.updateShow(1, entityManager);

        showDAO.update(show);

        final Show updatedShow = ShowUtils.getShow(entityManager, 1);
        final Show expectedUpdatedShow = ShowUtils.getShow(1);
        ShowUtils.updateShow(expectedUpdatedShow);
        expectedUpdatedShow.setPosition(ShowUtils.POSITION);
        ShowUtils.assertShowDeepEquals(expectedUpdatedShow, updatedShow);

        assertEquals(ShowUtils.SHOWS_COUNT, ShowUtils.getShowsCount(entityManager));
    }


    /**
     * Test method for {@link ShowDAO#remove(Show)}.
     */
    @Test
    public void testRemove() {
        final Show show = ShowUtils.newShow(null);
        show.setGenres(new ArrayList<>());
        entityManager.persist(show);
        assertEquals(ShowUtils.SHOWS_COUNT + 1, ShowUtils.getShowsCount(entityManager));

        showDAO.remove(show);

        assertNull(ShowUtils.getShow(entityManager, show.getId()));

        assertEquals(ShowUtils.SHOWS_COUNT, ShowUtils.getShowsCount(entityManager));
    }

}
