package cz.vhromada.catalog.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.SeasonUtils;
import cz.vhromada.catalog.commons.ShowUtils;
import cz.vhromada.catalog.dao.SeasonDAO;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Show;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration test for class {@link SeasonDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testDAOContext.xml")
@Transactional
@Rollback
public class SeasonDAOImplIntegrationTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * Instance of {@link SeasonDAO}
     */
    @Autowired
    private SeasonDAO seasonDAO;

    /**
     * Test method for {@link SeasonDAO#getSeason(Integer)}.
     */
    @Test
    public void testGetSeason() {
        for (int i = 0; i < SeasonUtils.SEASONS_COUNT; i++) {
            final int showNumber = i / SeasonUtils.SEASONS_PER_SHOW_COUNT + 1;
            final int seasonNumber = i % SeasonUtils.SEASONS_PER_SHOW_COUNT + 1;
            final Season season = seasonDAO.getSeason(i + 1);

            SeasonUtils.assertSeasonDeepEquals(SeasonUtils.getSeason(showNumber, seasonNumber), season);
        }

        assertNull(seasonDAO.getSeason(Integer.MAX_VALUE));

        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
    }

    /**
     * Test method for {@link SeasonDAO#add(Season)}.
     */
    @Test
    public void testAdd() {
        final Season season = SeasonUtils.newSeason(null);
        season.setShow(1);

        seasonDAO.add(season);

        assertNotNull(season.getId());
        assertEquals(SeasonUtils.SEASONS_COUNT + 1, season.getId().intValue());
        assertEquals(SeasonUtils.SEASONS_COUNT, season.getPosition());

        final Season addedSeason = SeasonUtils.getSeason(entityManager, SeasonUtils.SEASONS_COUNT + 1);
        final Season expectedAddedSeason = SeasonUtils.newSeason(SeasonUtils.SEASONS_COUNT + 1);
        expectedAddedSeason.setShow(1);
        SeasonUtils.assertSeasonDeepEquals(expectedAddedSeason, addedSeason);

        assertEquals(SeasonUtils.SEASONS_COUNT + 1, SeasonUtils.getSeasonsCount(entityManager));
    }

    /**
     * Test method for {@link SeasonDAO#update(Season)}.
     */
    @Test
    public void testUpdate() {
        final Season season = SeasonUtils.updateSeason(1, entityManager);

        seasonDAO.update(season);

        final Season updatedSeason = SeasonUtils.getSeason(entityManager, 1);
        final Season expectedUpdatedSeason = SeasonUtils.getSeason(1, 1);
        SeasonUtils.updateSeason(expectedUpdatedSeason);
        expectedUpdatedSeason.setPosition(SeasonUtils.POSITION);
        SeasonUtils.assertSeasonDeepEquals(expectedUpdatedSeason, updatedSeason);

        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
    }

    /**
     * Test method for {@link SeasonDAO#remove(Season)}.
     */
    @Test
    public void testRemove() {
        final Season season = SeasonUtils.newSeason(null);
        season.setShow(1);
        entityManager.persist(season);
        assertEquals(SeasonUtils.SEASONS_COUNT + 1, SeasonUtils.getSeasonsCount(entityManager));

        seasonDAO.remove(season);

        assertNull(SeasonUtils.getSeason(entityManager, season.getId()));

        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
    }

    /**
     * Test method for {@link SeasonDAO#findSeasonsByShow(Show)}.
     */
    @Test
    public void testFindSeasonsByShow() {
        for (int i = 1; i <= ShowUtils.SHOWS_COUNT; i++) {
            final Show show = ShowUtils.getShow(entityManager, i);

            final List<Season> seasons = seasonDAO.findSeasonsByShow(show);

            SeasonUtils.assertSeasonsDeepEquals(SeasonUtils.getSeasons(show.getId()), seasons);
        }

        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
    }

}
