package cz.vhromada.catalog.dao.impl.spring;

import static cz.vhromada.catalog.commons.SpringUtils.SERIES_COUNT;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.dao.SerieDAO;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.dao.impl.SerieDAOImpl;
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

	/** Instance of {@link ObjectGenerator} */
	@Autowired
	private ObjectGenerator objectGenerator;

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
		final Serie serie = objectGenerator.generate(Serie.class);
		serie.setId(null);
		serie.setGenres(CollectionUtils.newList(SpringUtils.getGenre(entityManager, 4)));

		serieDAO.add(serie);

		DeepAsserts.assertNotNull(serie.getId());
		DeepAsserts.assertEquals(SERIES_COUNT + 1, serie.getId());
		DeepAsserts.assertEquals(SERIES_COUNT, serie.getPosition());
		final Serie addedSerie = SpringUtils.getSerie(entityManager, SERIES_COUNT + 1);
		DeepAsserts.assertEquals(serie, addedSerie);
		DeepAsserts.assertEquals(SERIES_COUNT + 1, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieDAO#update(Serie)}. */
	@Test
	public void testUpdate() {
		final Serie serie = SpringEntitiesUtils.updateSerie(SpringUtils.getSerie(entityManager, 1), objectGenerator);
		serie.setGenres(CollectionUtils.newList(SpringUtils.getGenre(entityManager, 4)));

		serieDAO.update(serie);

		final Serie updatedSerie = SpringUtils.getSerie(entityManager, 1);
		DeepAsserts.assertEquals(serie, updatedSerie);
		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieDAO#remove(Serie)}. */
	@Test
	public void testRemove() {
		final Serie serie = objectGenerator.generate(Serie.class);
		serie.setId(null);
		serie.setGenres(CollectionUtils.newList(SpringUtils.getGenre(entityManager, 4)));
		entityManager.persist(serie);
		DeepAsserts.assertEquals(SERIES_COUNT + 1, SpringUtils.getSeriesCount(entityManager));

		serieDAO.remove(serie);

		assertNull(SpringUtils.getSerie(entityManager, serie.getId()));
		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
	}

}
