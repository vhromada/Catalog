package cz.vhromada.catalog.dao.impl.spring;

import static cz.vhromada.catalog.common.SpringUtils.SERIES_COUNT;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.common.SpringEntitiesUtils;
import cz.vhromada.catalog.common.SpringUtils;
import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.dao.SerieDAO;
import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.dao.impl.SerieDAOImpl;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents test for class {@link SerieDAOImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testDAOContext.xml")
@Transactional
public class SerieDAOImplSpringTest {

	/** Instance of {@link EntityManager} */
	@Autowired
	private EntityManager entityManager;

	/** Instance of {@link SerieDAO} */
	@Autowired
	private SerieDAO serieDAO;

	/** Restarts sequence. */
	@Before
	public void setUp() {
		entityManager.createNativeQuery("ALTER SEQUENCE series_sq RESTART WITH 4").executeUpdate();
	}

	/** Test method for {@link SerieDAO#getSeries()}. */
	@Test
	public void testGetSeries() {
		DeepAsserts.assertEquals(SpringEntitiesUtils.getSeries(), serieDAO.getSeries());
		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieDAO#getSerie(Integer)}. */
	@Test
	public void testGetSerie() {
		for (int i = 1; i <= SERIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getSerie(i), serieDAO.getSerie(i));
		}

		assertNull(serieDAO.getSerie(Integer.MAX_VALUE));

		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieDAO#add(Serie)}. */
	@Test
	public void testAdd() {
		final List<Genre> genres = new ArrayList<>();
		genres.add(SpringUtils.getGenre(entityManager, 4));
		final Serie serie = EntityGenerator.createSerie();
		serie.setGenres(genres);
		final Serie expectedSerie = EntityGenerator.createSerie(SERIES_COUNT + 1);
		expectedSerie.setPosition(SERIES_COUNT);
		expectedSerie.setGenres(genres);

		serieDAO.add(serie);

		DeepAsserts.assertNotNull(serie.getId());
		DeepAsserts.assertEquals(SERIES_COUNT + 1, serie.getId());
		DeepAsserts.assertEquals(SERIES_COUNT, serie.getPosition());
		final Serie addedSerie = SpringUtils.getSerie(entityManager, SERIES_COUNT + 1);
		DeepAsserts.assertEquals(serie, addedSerie);
		DeepAsserts.assertEquals(expectedSerie, addedSerie);
		DeepAsserts.assertEquals(SERIES_COUNT + 1, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieDAO#update(Serie)}. */
	@Test
	public void testUpdate() {
		final List<Genre> genres = new ArrayList<>();
		genres.add(SpringUtils.getGenre(entityManager, 4));
		final Serie serie = SpringEntitiesUtils.updateSerie(SpringUtils.getSerie(entityManager, 1));
		serie.setGenres(genres);
		final Serie expectedSerie = EntityGenerator.createSerie(1);
		expectedSerie.setGenres(genres);

		serieDAO.update(serie);

		final Serie updatedSerie = SpringUtils.getSerie(entityManager, 1);
		DeepAsserts.assertEquals(serie, updatedSerie);
		DeepAsserts.assertEquals(expectedSerie, updatedSerie);
		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieDAO#remove(Serie)}. */
	@Test
	public void testRemove() {
		final Serie serie = EntityGenerator.createSerie();
		serie.setGenres(CollectionUtils.newList(SpringUtils.getGenre(entityManager, 4)));
		entityManager.persist(serie);
		DeepAsserts.assertEquals(SERIES_COUNT + 1, SpringUtils.getSeriesCount(entityManager));

		serieDAO.remove(serie);

		assertNull(SpringUtils.getSerie(entityManager, serie.getId()));
		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
	}

}
