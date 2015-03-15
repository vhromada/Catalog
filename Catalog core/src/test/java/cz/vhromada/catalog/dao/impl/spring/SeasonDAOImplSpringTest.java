package cz.vhromada.catalog.dao.impl.spring;

import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.dao.SeasonDAO;
import cz.vhromada.catalog.dao.entities.Season;
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
 * A class represents test for class {@link cz.vhromada.catalog.dao.impl.SeasonDAOImpl} with Spring framework.
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

    /** Instance of {@link ObjectGenerator} */
    @Autowired
    private ObjectGenerator objectGenerator;

    /** Restarts sequence. */
    @Before
    public void setUp() {
        entityManager.createNativeQuery("ALTER SEQUENCE seasons_sq RESTART WITH 10").executeUpdate();
    }

    /** Test method for {@link SeasonDAO#getSeason(Integer)}. */
    @Test
    public void testGetSeason() {
        for (int i = 0; i < SpringUtils.SEASONS_COUNT; i++) {
            final int serieNumber = i / SpringUtils.SEASONS_PER_SERIE_COUNT + 1;
            final int seasonNumber = i % SpringUtils.SEASONS_PER_SERIE_COUNT + 1;
            DeepAsserts.assertEquals(SpringEntitiesUtils.getSeason(serieNumber, seasonNumber), seasonDAO.getSeason(i + 1));
        }

        assertNull(seasonDAO.getSeason(Integer.MAX_VALUE));

        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
    }

    /** Test method for {@link SeasonDAO#add(Season)}. */
    @Test
    public void testAdd() {
        final Season season = SpringEntitiesUtils.newSeason(objectGenerator, entityManager);

        seasonDAO.add(season);

        DeepAsserts.assertNotNull(season.getId());
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT + 1, season.getId());
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT, season.getPosition());
        final Season addedSeason = SpringUtils.getSeason(entityManager, SpringUtils.SEASONS_COUNT + 1);
        DeepAsserts.assertEquals(season, addedSeason);
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT + 1, SpringUtils.getSeasonsCount(entityManager));
    }

    /** Test method for {@link SeasonDAO#update(Season)}. */
    @Test
    public void testUpdate() {
        final Season season = SpringEntitiesUtils.updateSeason(1, objectGenerator, entityManager);

        seasonDAO.update(season);

        final Season updatedSeason = SpringUtils.getSeason(entityManager, 1);
        DeepAsserts.assertEquals(season, updatedSeason);
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
    }

    /** Test method for {@link SeasonDAO#remove(Season)}. */
    @Test
    public void testRemove() {
        final Season season = SpringEntitiesUtils.newSeason(objectGenerator, entityManager);
        entityManager.persist(season);
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT + 1, SpringUtils.getSeasonsCount(entityManager));

        seasonDAO.remove(season);

        assertNull(SpringUtils.getSeason(entityManager, season.getId()));
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
    }

    /** Test method for {@link SeasonDAO#findSeasonsBySerie(Serie)}. */
    @Test
    public void testFindSeasonsBySerie() {
        for (int i = 1; i <= SpringUtils.SERIES_COUNT; i++) {
            final Serie serie = SpringUtils.getSerie(entityManager, i);
            DeepAsserts.assertEquals(SpringEntitiesUtils.getSeasons(i), seasonDAO.findSeasonsBySerie(serie));
        }
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
    }

}
