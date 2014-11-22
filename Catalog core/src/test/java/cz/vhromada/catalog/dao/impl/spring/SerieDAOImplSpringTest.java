package cz.vhromada.catalog.dao.impl.spring;

import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.dao.SerieDAO;
import cz.vhromada.catalog.dao.entities.Serie;
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
 * A class represents test for class {@link cz.vhromada.catalog.dao.impl.SerieDAOImpl} with Spring framework.
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
        DeepAsserts.assertEquals(SpringUtils.SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
    }

    /** Test method for {@link SerieDAO#getSerie(Integer)}. */
    @Test
    public void testGetSerie() {
        for (int i = 1; i <= SpringUtils.SERIES_COUNT; i++) {
            DeepAsserts.assertEquals(SpringEntitiesUtils.getSerie(i), serieDAO.getSerie(i));
        }

        assertNull(serieDAO.getSerie(Integer.MAX_VALUE));

        DeepAsserts.assertEquals(SpringUtils.SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
    }

    /** Test method for {@link SerieDAO#add(Serie)}. */
    @Test
    public void testAdd() {
        final Serie serie = SpringEntitiesUtils.newSerie(objectGenerator, entityManager);

        serieDAO.add(serie);

        DeepAsserts.assertNotNull(serie.getId());
        DeepAsserts.assertEquals(SpringUtils.SERIES_COUNT + 1, serie.getId());
        DeepAsserts.assertEquals(SpringUtils.SERIES_COUNT, serie.getPosition());
        final Serie addedSerie = SpringUtils.getSerie(entityManager, SpringUtils.SERIES_COUNT + 1);
        DeepAsserts.assertEquals(serie, addedSerie);
        DeepAsserts.assertEquals(SpringUtils.SERIES_COUNT + 1, SpringUtils.getSeriesCount(entityManager));
    }

    /** Test method for {@link SerieDAO#update(Serie)}. */
    @Test
    public void testUpdate() {
        final Serie serie = SpringEntitiesUtils.updateSerie(1, objectGenerator, entityManager);

        serieDAO.update(serie);

        final Serie updatedSerie = SpringUtils.getSerie(entityManager, 1);
        DeepAsserts.assertEquals(serie, updatedSerie);
        DeepAsserts.assertEquals(SpringUtils.SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
    }

    /** Test method for {@link SerieDAO#remove(Serie)}. */
    @Test
    public void testRemove() {
        final Serie serie = SpringEntitiesUtils.newSerie(objectGenerator, entityManager);
        entityManager.persist(serie);
        DeepAsserts.assertEquals(SpringUtils.SERIES_COUNT + 1, SpringUtils.getSeriesCount(entityManager));

        serieDAO.remove(serie);

        assertNull(SpringUtils.getSerie(entityManager, serie.getId()));
        DeepAsserts.assertEquals(SpringUtils.SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
    }

}
