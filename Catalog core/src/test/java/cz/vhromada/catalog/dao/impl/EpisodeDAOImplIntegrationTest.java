package cz.vhromada.catalog.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.EpisodeUtils;
import cz.vhromada.catalog.commons.SeasonUtils;
import cz.vhromada.catalog.commons.ShowUtils;
import cz.vhromada.catalog.dao.EpisodeDAO;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Show;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration integration test for class {@link EpisodeDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testDAOContext.xml")
@Transactional
public class EpisodeDAOImplIntegrationTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * Instance of {@link EpisodeDAO}
     */
    @Autowired
    private EpisodeDAO episodeDAO;

    /**
     * Test method for {@link EpisodeDAO#getEpisode(Integer)}.
     */
    @Test
    public void testGetEpisode() {
        for (int i = 0; i < EpisodeUtils.EPISODES_COUNT; i++) {
            final int showNumber = i / EpisodeUtils.EPISODES_PER_SHOW_COUNT + 1;
            final int seasonNumber = i % EpisodeUtils.EPISODES_PER_SHOW_COUNT / EpisodeUtils.EPISODES_PER_SEASON_COUNT + 1;
            final int episodeNumber = i % EpisodeUtils.EPISODES_PER_SEASON_COUNT + 1;
            final Episode episode = episodeDAO.getEpisode(i + 1);

            EpisodeUtils.assertEpisodeDeepEquals(EpisodeUtils.getEpisode(showNumber, seasonNumber, episodeNumber), episode);
        }

        assertNull(episodeDAO.getEpisode(Integer.MAX_VALUE));

        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link EpisodeDAO#add(Episode)}.
     */
    @Test
    public void testAdd() {
        final Episode episode = EpisodeUtils.newEpisode(null);
        episode.setSeason(1);

        episodeDAO.add(episode);

        assertNotNull(episode.getId());
        assertEquals(EpisodeUtils.EPISODES_COUNT + 1, episode.getId().intValue());
        assertEquals(EpisodeUtils.EPISODES_COUNT, episode.getPosition());

        final Episode addedEpisode = EpisodeUtils.getEpisode(entityManager, EpisodeUtils.EPISODES_COUNT + 1);
        final Episode expectedAddedEpisode = EpisodeUtils.newEpisode(EpisodeUtils.EPISODES_COUNT + 1);
        expectedAddedEpisode.setSeason(1);
        EpisodeUtils.assertEpisodeDeepEquals(expectedAddedEpisode, addedEpisode);

        assertEquals(EpisodeUtils.EPISODES_COUNT + 1, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link EpisodeDAO#update(Episode)}.
     */
    @Test
    public void testUpdate() {
        final Episode episode = EpisodeUtils.updateEpisode(1, entityManager);

        episodeDAO.update(episode);

        final Episode updatedEpisode = EpisodeUtils.getEpisode(entityManager, 1);
        final Episode expectedUpdatedEpisode = EpisodeUtils.getEpisode(1, 1, 1);
        EpisodeUtils.updateEpisode(expectedUpdatedEpisode);
        expectedUpdatedEpisode.setPosition(EpisodeUtils.POSITION);
        EpisodeUtils.assertEpisodeDeepEquals(expectedUpdatedEpisode, updatedEpisode);

        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link EpisodeDAO#remove(Episode)}.
     */
    @Test
    public void testRemove() {
        episodeDAO.remove(EpisodeUtils.getEpisode(entityManager, 1));

        assertNull(EpisodeUtils.getEpisode(entityManager, 1));

        assertEquals(EpisodeUtils.EPISODES_COUNT - 1, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link EpisodeDAO#findEpisodesBySeason(Season)}.
     */
    @Test
    public void testFindEpisodesBySeason() {
        for (int i = 1; i <= SeasonUtils.SEASONS_COUNT; i++) {
            final Season season = SeasonUtils.getSeason(entityManager, i);
            final Show show = ShowUtils.getShow(entityManager, season.getShow());
            final int seasonNumber = (i - 1) % SeasonUtils.SEASONS_PER_SHOW_COUNT + 1;

            final List<Episode> episodes = episodeDAO.findEpisodesBySeason(season);

            EpisodeUtils.assertEpisodesDeepEquals(EpisodeUtils.getEpisodes(show.getId(), seasonNumber), episodes);
        }

        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

}
