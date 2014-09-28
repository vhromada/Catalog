package cz.vhromada.catalog.dao.impl.spring;

import static cz.vhromada.catalog.commons.SpringUtils.SEASONS_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.SEASONS_PER_SERIE_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.SERIES_COUNT;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.EntityGenerator;
import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.dao.SeasonDAO;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.dao.impl.SeasonDAOImpl;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents test for class {@link SeasonDAOImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testDAOContext.xml")
@Transactional
public class SeasonDAOImplSpringTest {

	/** Instance of {@link EntityManager} */
	@Autowired
	private EntityManager entityManager;

	/** Instance of {@link SeasonDAO} */
	@Autowired
	private SeasonDAO seasonDAO;

	/** Restarts sequence. */
	@Before
	public void setUp() {
		entityManager.createNativeQuery("ALTER SEQUENCE seasons_sq RESTART WITH 10").executeUpdate();
	}

	/** Test method for {@link SeasonDAO#getSeason(Integer)}. */
	@Test
	public void testGetSeason() {
		for (int i = 0; i < SEASONS_COUNT; i++) {
			final int serieNumber = i / SEASONS_PER_SERIE_COUNT + 1;
			final int seasonNumber = i % SEASONS_PER_SERIE_COUNT + 1;
			DeepAsserts.assertEquals(SpringEntitiesUtils.getSeason(serieNumber, seasonNumber), seasonDAO.getSeason(i + 1));
		}

		assertNull(seasonDAO.getSeason(Integer.MAX_VALUE));

		DeepAsserts.assertEquals(SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
	}

	/** Test method for {@link SeasonDAO#add(Season)}. */
	@Test
	public void testAdd() {
		final Serie serie = SpringUtils.getSerie(entityManager, 1);
		final Season season = EntityGenerator.createSeason(serie);
		final Season expectedSeason = EntityGenerator.createSeason(SEASONS_COUNT + 1, serie);
		expectedSeason.setPosition(SEASONS_COUNT);

		seasonDAO.add(season);

		DeepAsserts.assertNotNull(season.getId());
		DeepAsserts.assertEquals(SEASONS_COUNT + 1, season.getId());
		DeepAsserts.assertEquals(SEASONS_COUNT, season.getPosition());
		final Season addedSeason = SpringUtils.getSeason(entityManager, SEASONS_COUNT + 1);
		DeepAsserts.assertEquals(season, addedSeason);
		DeepAsserts.assertEquals(expectedSeason, addedSeason);
		DeepAsserts.assertEquals(SEASONS_COUNT + 1, SpringUtils.getSeasonsCount(entityManager));
	}

	/** Test method for {@link SeasonDAO#update(Season)}. */
	@Test
	public void testUpdate() {
		final Season season = SpringEntitiesUtils.updateSeason(SpringUtils.getSeason(entityManager, 1));
		final Season expectedSeason = EntityGenerator.createSeason(1, SpringUtils.getSerie(entityManager, 1));

		seasonDAO.update(season);

		final Season updatedSeason = SpringUtils.getSeason(entityManager, 1);
		DeepAsserts.assertEquals(season, updatedSeason);
		DeepAsserts.assertEquals(expectedSeason, updatedSeason);
		DeepAsserts.assertEquals(SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
	}

	/** Test method for {@link SeasonDAO#remove(Season)}. */
	@Test
	public void testRemove() {
		final Season season = EntityGenerator.createSeason(SpringUtils.getSerie(entityManager, 1));
		entityManager.persist(season);
		DeepAsserts.assertEquals(SEASONS_COUNT + 1, SpringUtils.getSeasonsCount(entityManager));

		seasonDAO.remove(season);

		assertNull(SpringUtils.getSeason(entityManager, season.getId()));
		DeepAsserts.assertEquals(SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
	}

	/** Test method for {@link SeasonDAO#findSeasonsBySerie(Serie)}. */
	@Test
	public void testFindSeasonsBySerie() {
		for (int i = 1; i <= SERIES_COUNT; i++) {
			final Serie serie = SpringUtils.getSerie(entityManager, i);
			DeepAsserts.assertEquals(SpringEntitiesUtils.getSeasons(i), seasonDAO.findSeasonsBySerie(serie));
		}
		DeepAsserts.assertEquals(SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
	}

}
