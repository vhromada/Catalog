package cz.vhromada.catalog.dao.impl.spring;

import static cz.vhromada.catalog.commons.SpringUtils.EPISODES_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.EPISODES_PER_SEASON_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.EPISODES_PER_SERIE_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.SEASONS_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.SEASONS_PER_SERIE_COUNT;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.dao.EpisodeDAO;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.impl.EpisodeDAOImpl;
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
 * A class represents test for class {@link EpisodeDAOImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testDAOContext.xml")
@Transactional
public class EpisodeDAOImplSpringTest {

	/** Instance of {@link EntityManager} */
	@Autowired
	private EntityManager entityManager;

	/** Instance of {@link EpisodeDAO} */
	@Autowired
	private EpisodeDAO episodeDAO;

	/** Instance of {@link ObjectGenerator} */
	@Autowired
	private ObjectGenerator objectGenerator;

	/** Restarts sequence. */
	@Before
	public void setUp() {
		entityManager.createNativeQuery("ALTER SEQUENCE episodes_sq RESTART WITH 28").executeUpdate();
	}

	/** Test method for {@link EpisodeDAO#getEpisode(Integer)}. */
	@Test
	public void testGetEpisode() {
		for (int i = 0; i < EPISODES_COUNT; i++) {
			final int serieNumber = i / EPISODES_PER_SERIE_COUNT + 1;
			final int seasonNumber = i % EPISODES_PER_SERIE_COUNT / EPISODES_PER_SEASON_COUNT + 1;
			final int episodeNumber = i % EPISODES_PER_SEASON_COUNT + 1;
			DeepAsserts.assertEquals(SpringEntitiesUtils.getEpisode(serieNumber, seasonNumber, episodeNumber), episodeDAO.getEpisode(i + 1));
		}

		assertNull(episodeDAO.getEpisode(Integer.MAX_VALUE));

		DeepAsserts.assertEquals(EPISODES_COUNT, SpringUtils.getEpisodesCount(entityManager));
	}

	/** Test method for {@link EpisodeDAO#add(Episode)}. */
	@Test
	public void testAdd() {
		final Episode episode = SpringEntitiesUtils.newEpisode(objectGenerator, entityManager);

		episodeDAO.add(episode);

		DeepAsserts.assertNotNull(episode.getId());
		DeepAsserts.assertEquals(EPISODES_COUNT + 1, episode.getId());
		DeepAsserts.assertEquals(EPISODES_COUNT, episode.getPosition());
		final Episode addedEpisode = SpringUtils.getEpisode(entityManager, EPISODES_COUNT + 1);
		DeepAsserts.assertEquals(episode, addedEpisode);
		DeepAsserts.assertEquals(EPISODES_COUNT + 1, SpringUtils.getEpisodesCount(entityManager));
	}

	/** Test method for {@link EpisodeDAO#update(Episode)}. */
	@Test
	public void testUpdate() {
		final Episode episode = SpringEntitiesUtils.updateEpisode(1, objectGenerator, entityManager);

		episodeDAO.update(episode);

		final Episode updatedEpisode = SpringUtils.getEpisode(entityManager, 1);
		DeepAsserts.assertEquals(episode, updatedEpisode);
		DeepAsserts.assertEquals(EPISODES_COUNT, SpringUtils.getEpisodesCount(entityManager));
	}

	/** Test method for {@link EpisodeDAO#remove(Episode)}. */
	@Test
	public void testRemove() {
		episodeDAO.remove(SpringUtils.getEpisode(entityManager, 1));

		assertNull(SpringUtils.getEpisode(entityManager, 1));
		DeepAsserts.assertEquals(EPISODES_COUNT - 1, SpringUtils.getEpisodesCount(entityManager));
	}

	/** Test method for {@link EpisodeDAO#findEpisodesBySeason(Season)}. */
	@Test
	public void testFindEpisodesBySeason() {
		for (int i = 1; i <= SEASONS_COUNT; i++) {
			final Season season = SpringUtils.getSeason(entityManager, i);
			final int seasonNumber = (i - 1) % SEASONS_PER_SERIE_COUNT + 1;
			DeepAsserts.assertEquals(SpringEntitiesUtils.getEpisodes(season.getSerie().getId(), seasonNumber),
					episodeDAO.findEpisodesBySeason(season));
		}
		DeepAsserts.assertEquals(EPISODES_COUNT, SpringUtils.getEpisodesCount(entityManager));
	}

}
