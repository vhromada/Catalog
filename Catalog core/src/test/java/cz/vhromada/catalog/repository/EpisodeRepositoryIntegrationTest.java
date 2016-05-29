package cz.vhromada.catalog.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.EpisodeUtils;
import cz.vhromada.catalog.dao.entities.Episode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration test for class {@link EpisodeRepository}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testRepositoryContext.xml")
@Transactional
@Rollback
public class EpisodeRepositoryIntegrationTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private EntityManager entityManager;

    /**
     * Instance of {@link EpisodeRepository}
     */
    @Autowired
    private EpisodeRepository episodeRepository;

    /**
     * Test method for get episode.
     */
    @Test
    public void testGetEpisode() {
        for (int i = 1; i <= EpisodeUtils.EPISODES_COUNT; i++) {
            final Episode episode = episodeRepository.findOne(i);

            EpisodeUtils.assertEpisodeDeepEquals(EpisodeUtils.getEpisode(i), episode);
        }

        assertNull(episodeRepository.findOne(Integer.MAX_VALUE));

        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for update episode.
     */
    @Test
    public void testUpdate() {
        final Episode episode = EpisodeUtils.updateEpisode(entityManager, 1);

        episodeRepository.saveAndFlush(episode);

        final Episode updatedEpisode = EpisodeUtils.getEpisode(entityManager, 1);
        final Episode expectedUpdatedEpisode = EpisodeUtils.getEpisode(1);
        EpisodeUtils.updateEpisode(expectedUpdatedEpisode);
        expectedUpdatedEpisode.setPosition(EpisodeUtils.POSITION);
        EpisodeUtils.assertEpisodeDeepEquals(expectedUpdatedEpisode, updatedEpisode);

        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for remove episode.
     */
    @Test
    public void testRemove() {
        episodeRepository.delete(EpisodeUtils.getEpisode(entityManager, 1));

        assertNull(EpisodeUtils.getEpisode(entityManager, 1));

        assertEquals(EpisodeUtils.EPISODES_COUNT - 1, EpisodeUtils.getEpisodesCount(entityManager));
    }

}
