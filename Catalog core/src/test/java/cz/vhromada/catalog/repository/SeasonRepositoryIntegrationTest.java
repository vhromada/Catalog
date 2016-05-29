package cz.vhromada.catalog.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.EpisodeUtils;
import cz.vhromada.catalog.commons.SeasonUtils;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration test for class {@link SeasonRepository}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testRepositoryContext.xml")
@Transactional
@Rollback
public class SeasonRepositoryIntegrationTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private EntityManager entityManager;

    /**
     * Instance of {@link SeasonRepository}
     */
    @Autowired
    private SeasonRepository seasonRepository;

    /**
     * Test method for get season.
     */
    @Test
    public void testGetSeason() {
        for (int i = 1; i <= SeasonUtils.SEASONS_COUNT; i++) {
            final Season season = seasonRepository.findOne(i);

            SeasonUtils.assertSeasonDeepEquals(SeasonUtils.getSeason(i), season);
        }

        assertNull(seasonRepository.findOne(Integer.MAX_VALUE));

        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for update season with updated data.
     */
    @Test
    public void testUpdate_Data() {
        final Season season = SeasonUtils.updateSeason(entityManager, 1);

        seasonRepository.saveAndFlush(season);

        final Season updatedSeason = SeasonUtils.getSeason(entityManager, 1);
        final Season expectedUpdatedSeason = SeasonUtils.getSeason(1);
        SeasonUtils.updateSeason(expectedUpdatedSeason);
        expectedUpdatedSeason.setPosition(SeasonUtils.POSITION);
        SeasonUtils.assertSeasonDeepEquals(expectedUpdatedSeason, updatedSeason);

        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for update season with added episode.
     */
    @Test
    public void testUpdate_Episode() {
        final Episode episode = EpisodeUtils.newEpisode(null);
        entityManager.persist(episode);

        final Season season = SeasonUtils.getSeason(entityManager, 1);
        season.getEpisodes().add(episode);

        seasonRepository.saveAndFlush(season);

        final Season updatedSeason = SeasonUtils.getSeason(entityManager, 1);
        final Episode expectedEpisode = EpisodeUtils.newEpisode(null);
        expectedEpisode.setId(EpisodeUtils.EPISODES_COUNT + 1);
        final Season expectedUpdatedSeason = SeasonUtils.getSeason(1);
        expectedUpdatedSeason.getEpisodes().add(expectedEpisode);
        SeasonUtils.assertSeasonDeepEquals(expectedUpdatedSeason, updatedSeason);

        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT + 1, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for remove season.
     */
    @Test
    public void testRemove() {
        final int episodesCount = SeasonUtils.getSeason(1).getEpisodes().size();

        seasonRepository.delete(SeasonUtils.getSeason(entityManager, 1));

        assertNull(SeasonUtils.getSeason(entityManager, 1));

        assertEquals(SeasonUtils.SEASONS_COUNT - 1, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT - episodesCount, EpisodeUtils.getEpisodesCount(entityManager));
    }

}
