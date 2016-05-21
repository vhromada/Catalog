//package cz.vhromada.catalog.dao.impl.spring;
//
//import static org.junit.Assert.assertNull;
//
//import javax.persistence.EntityManager;
//
//import cz.vhromada.catalog.commons.EntitiesUtils;
//import cz.vhromada.catalog.commons.SpringUtils;
//import cz.vhromada.catalog.dao.EpisodeDAO;
//import cz.vhromada.catalog.dao.entities.Episode;
//import cz.vhromada.catalog.dao.entities.Season;
//import cz.vhromada.generator.ObjectGenerator;
//import cz.vhromada.test.DeepAsserts;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * A class represents test for class {@link cz.vhromada.catalog.dao.impl.EpisodeDAOImpl} with Spring framework.
// *
// * @author Vladimir Hromada
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:testDAOContext.xml")
//@Transactional
//public class EpisodeDAOImplSpringTest {
//
//    /**
//     * Instance of {@link EntityManager}
//     */
//    @Autowired
//    private EntityManager entityManager;
//
//    /**
//     * Instance of {@link EpisodeDAO}
//     */
//    @Autowired
//    private EpisodeDAO episodeDAO;
//
//    /**
//     * Instance of {@link ObjectGenerator}
//     */
//    @Autowired
//    private ObjectGenerator objectGenerator;
//
//    /**
//     * Restarts sequence.
//     */
//    @Before
//    public void setUp() {
//        entityManager.createNativeQuery("ALTER SEQUENCE episodes_sq RESTART WITH 28").executeUpdate();
//    }
//
//    /**
//     * Test method for {@link EpisodeDAO#getEpisode(Integer)}.
//     */
//    @Test
//    public void testGetEpisode() {
//        for (int i = 0; i < SpringUtils.EPISODES_COUNT; i++) {
//            final int showNumber = i / SpringUtils.EPISODES_PER_SHOW_COUNT + 1;
//            final int seasonNumber = i % SpringUtils.EPISODES_PER_SHOW_COUNT / SpringUtils.EPISODES_PER_SEASON_COUNT + 1;
//            final int episodeNumber = i % SpringUtils.EPISODES_PER_SEASON_COUNT + 1;
//            DeepAsserts.assertEquals(EntitiesUtils.getEpisode(showNumber, seasonNumber, episodeNumber), episodeDAO.getEpisode(i + 1));
//        }
//
//        assertNull(episodeDAO.getEpisode(Integer.MAX_VALUE));
//
//        DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT, SpringUtils.getEpisodesCount(entityManager));
//    }
//
//    /**
//     * Test method for {@link EpisodeDAO#add(Episode)}.
//     */
//    @Test
//    public void testAdd() {
//        final Episode episode = EntitiesUtils.newEpisode(objectGenerator, entityManager);
//
//        episodeDAO.add(episode);
//
//        DeepAsserts.assertNotNull(episode.getId());
//        DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT + 1, episode.getId());
//        DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT, episode.getPosition());
//        final Episode addedEpisode = SpringUtils.getEpisode(entityManager, SpringUtils.EPISODES_COUNT + 1);
//        DeepAsserts.assertEquals(episode, addedEpisode);
//        DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT + 1, SpringUtils.getEpisodesCount(entityManager));
//    }
//
//    /**
//     * Test method for {@link EpisodeDAO#update(Episode)}.
//     */
//    @Test
//    public void testUpdate() {
//        final Episode episode = EntitiesUtils.updateEpisode(1, objectGenerator, entityManager);
//
//        episodeDAO.update(episode);
//
//        final Episode updatedEpisode = SpringUtils.getEpisode(entityManager, 1);
//        DeepAsserts.assertEquals(episode, updatedEpisode);
//        DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT, SpringUtils.getEpisodesCount(entityManager));
//    }
//
//    /**
//     * Test method for {@link EpisodeDAO#remove(Episode)}.
//     */
//    @Test
//    public void testRemove() {
//        episodeDAO.remove(SpringUtils.getEpisode(entityManager, 1));
//
//        assertNull(SpringUtils.getEpisode(entityManager, 1));
//        DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT - 1, SpringUtils.getEpisodesCount(entityManager));
//    }
//
//    /**
//     * Test method for {@link EpisodeDAO#findEpisodesBySeason(Season)}.
//     */
//    @Test
//    public void testFindEpisodesBySeason() {
//        for (int i = 1; i <= SpringUtils.SEASONS_COUNT; i++) {
//            final Season season = SpringUtils.getSeason(entityManager, i);
//            final int seasonNumber = (i - 1) % SpringUtils.SEASONS_PER_SHOW_COUNT + 1;
//            DeepAsserts.assertEquals(EntitiesUtils.getEpisodes(season.getShow().getId(), seasonNumber), episodeDAO.findEpisodesBySeason(season));
//        }
//        DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT, SpringUtils.getEpisodesCount(entityManager));
//    }
//
//}
