//package cz.vhromada.catalog.facade.impl.spring;
//
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertTrue;
//
//import javax.persistence.EntityManager;
//
//import cz.vhromada.catalog.commons.EntitiesUtils;
//import cz.vhromada.catalog.commons.SpringToUtils;
//import cz.vhromada.catalog.commons.SpringUtils;
//import cz.vhromada.catalog.entities.Episode;
//import cz.vhromada.catalog.entities.Season;
//import cz.vhromada.catalog.entities.Show;
//import cz.vhromada.catalog.facade.EpisodeFacade;
//import cz.vhromada.catalog.facade.to.EpisodeTO;
//import cz.vhromada.generator.ObjectGenerator;
//import cz.vhromada.test.DeepAsserts;
//import cz.vhromada.validators.exceptions.RecordNotFoundException;
//import cz.vhromada.validators.exceptions.ValidationException;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.transaction.PlatformTransactionManager;
//
///**
// * A class represents test for class {@link cz.vhromada.catalog.facade.impl.EpisodeFacadeImpl} with Spring framework.
// *
// * @author Vladimir Hromada
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:testFacadeContext.xml")
//public class EpisodeFacadeImplSpringTest {
//
//    /**
//     * Year field
//     */
//    private static final String YEAR_FIELD = "year";
//
//    /**
//     * Count of seasons field
//     */
//    private static final String SEASONS_COUNT_FIELD = "seasonsCount";
//
//    /**
//     * Count of episodes field
//     */
//    private static final String EPISODES_COUNT_FIELD = "episodesCount";
//
//    /**
//     * Total length field
//     */
//    private static final String TOTAL_LENGTH_FIELD = "totalLength";
//
//    /**
//     * Subtitles as string method
//     */
//    private static final String SUBTITLES_AS_STRING_METHOD = "subtitlesAsString";
//
//    /**
//     * Genres as string method
//     */
//    private static final String GENRES_AS_STRING_METHOD = "genresAsString";
//
//    /**
//     * Instance of {@link EntityManager}
//     */
//    @Autowired
//    private EntityManager entityManager;
//
//    /**
//     * Instance of {@link PlatformTransactionManager}
//     */
//    @Autowired
//    private PlatformTransactionManager transactionManager;
//
//    /**
//     * Instance of (@link EpisodeFacade}
//     */
//    @Autowired
//    private EpisodeFacade episodeFacade;
//
//    /**
//     * Instance of {@link ObjectGenerator}
//     */
//    @Autowired
//    private ObjectGenerator objectGenerator;
//
//    /**
//     * Initializes database.
//     */
//    @Before
//    public void setUp() {
//        SpringUtils.remove(transactionManager, entityManager, Episode.class);
//        SpringUtils.remove(transactionManager, entityManager, Season.class);
//        SpringUtils.remove(transactionManager, entityManager, Show.class);
//        SpringUtils.updateSequence(transactionManager, entityManager, "tv_shows_sq");
//        SpringUtils.updateSequence(transactionManager, entityManager, "seasons_sq");
//        SpringUtils.updateSequence(transactionManager, entityManager, "episodes_sq");
//        for (final Show show : EntitiesUtils.getShows()) {
//            show.setId(null);
//            SpringUtils.persist(transactionManager, entityManager, show);
//        }
//        for (int i = 1; i <= SpringUtils.SHOWS_COUNT; i++) {
//            for (final Season season : EntitiesUtils.getSeasons(i)) {
//                season.setId(null);
//                SpringUtils.persist(transactionManager, entityManager, season);
//            }
//        }
//        for (int i = 1; i <= SpringUtils.SHOWS_COUNT; i++) {
//            for (int j = 1; j <= SpringUtils.SEASONS_PER_SHOW_COUNT; j++) {
//                for (final Episode episode : EntitiesUtils.getEpisodes(i, j)) {
//                    episode.setId(null);
//                    SpringUtils.persist(transactionManager, entityManager, episode);
//                }
//            }
//        }
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#getEpisode(Integer)}.
//     */
//    @Test
//    public void testGetEpisode() {
//        for (int i = 0; i < SpringUtils.EPISODES_COUNT; i++) {
//            final int showNumber = i / SpringUtils.EPISODES_PER_SHOW_COUNT + 1;
//            final int seasonNumber = i % SpringUtils.EPISODES_PER_SHOW_COUNT / SpringUtils.EPISODES_PER_SEASON_COUNT + 1;
//            final int episodeNumber = i % SpringUtils.EPISODES_PER_SEASON_COUNT + 1;
//            DeepAsserts.assertEquals(SpringToUtils.getEpisode(showNumber, seasonNumber, episodeNumber), episodeFacade.getEpisode(i + 1), SEASONS_COUNT_FIELD,
//                    EPISODES_COUNT_FIELD, TOTAL_LENGTH_FIELD);
//        }
//
//        assertNull(episodeFacade.getEpisode(Integer.MAX_VALUE));
//
//        DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT, SpringUtils.getEpisodesCount(entityManager));
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#getEpisode(Integer)} with null argument.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testGetEpisodeWithNullArgument() {
//        episodeFacade.getEpisode(null);
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#add(EpisodeTO)}.
//     */
//    @Test
//    public void testAdd() {
//        final EpisodeTO episode = SpringToUtils.newEpisode(objectGenerator);
//
//        episodeFacade.add(episode);
//
//        DeepAsserts.assertNotNull(episode.getId());
//        DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT + 1, episode.getId());
//        DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT, episode.getPosition());
//        final Episode addedEpisode = SpringUtils.getEpisode(entityManager, SpringUtils.EPISODES_COUNT + 1);
//        DeepAsserts.assertEquals(episode, addedEpisode, YEAR_FIELD, TOTAL_LENGTH_FIELD, SEASONS_COUNT_FIELD, EPISODES_COUNT_FIELD, SUBTITLES_AS_STRING_METHOD,
//                GENRES_AS_STRING_METHOD);
//        DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT + 1, SpringUtils.getEpisodesCount(entityManager));
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#add(EpisodeTO)} with null argument.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testAddWithNullArgument() {
//        episodeFacade.add(null);
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#add(EpisodeTO)} with episode with not null ID.
//     */
//    @Test(expected = ValidationException.class)
//    public void testAddWithEpisodeWithNotNullId() {
//        episodeFacade.add(SpringToUtils.newEpisodeWithId(objectGenerator));
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#add(EpisodeTO)} with episode with not positive number of episode.
//     */
//    @Test(expected = ValidationException.class)
//    public void testAddWithEpisodeWithNotPositiveNumber() {
//        final EpisodeTO episode = SpringToUtils.newEpisode(objectGenerator);
//        episode.setNumber(0);
//
//        episodeFacade.add(episode);
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#add(EpisodeTO)} with episode with null name.
//     */
//    @Test(expected = ValidationException.class)
//    public void testAddWithEpisodeWithNullName() {
//        final EpisodeTO episode = SpringToUtils.newEpisode(objectGenerator);
//        episode.setName(null);
//
//        episodeFacade.add(episode);
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#add(EpisodeTO)} with episode with empty string as name.
//     */
//    @Test(expected = ValidationException.class)
//    public void testAddWithEpisodeWithEmptyName() {
//        final EpisodeTO episode = SpringToUtils.newEpisode(objectGenerator);
//        episode.setName("");
//
//        episodeFacade.add(episode);
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#add(EpisodeTO)} with episode with negative length.
//     */
//    @Test(expected = ValidationException.class)
//    public void testAddWithEpisodeWithNegativeLength() {
//        final EpisodeTO episode = SpringToUtils.newEpisode(objectGenerator);
//        episode.setLength(-1);
//
//        episodeFacade.add(episode);
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#add(EpisodeTO)} with episode with null note.
//     */
//    @Test(expected = ValidationException.class)
//    public void testAddWithEpisodeWithNullNote() {
//        final EpisodeTO episode = SpringToUtils.newEpisode(objectGenerator);
//        episode.setNote(null);
//
//        episodeFacade.add(episode);
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#add(EpisodeTO)} with episode with null TO for season.
//     */
//    @Test(expected = ValidationException.class)
//    public void testAddWithEpisodeWithNullSeason() {
//        final EpisodeTO episode = SpringToUtils.newEpisode(objectGenerator);
//        episode.setSeason(null);
//
//        episodeFacade.add(episode);
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#add(EpisodeTO)} with episode with TO for season with null ID.
//     */
//    @Test(expected = ValidationException.class)
//    public void testAddWithEpisodeWithSeasonWithNullId() {
//        final EpisodeTO episode = SpringToUtils.newEpisode(objectGenerator);
//        episode.getSeason().setId(null);
//
//        episodeFacade.add(episode);
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#add(EpisodeTO)} with episode with not existing season.
//     */
//    @Test(expected = RecordNotFoundException.class)
//    public void testAddWithEpisodeWithNotExistingSeason() {
//        final EpisodeTO episode = SpringToUtils.newEpisode(objectGenerator);
//        episode.getSeason().setId(Integer.MAX_VALUE);
//
//        episodeFacade.add(episode);
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#update(EpisodeTO)}.
//     */
//    @Test
//    public void testUpdate() {
//        final EpisodeTO episode = SpringToUtils.newEpisode(objectGenerator, 1);
//
//        episodeFacade.update(episode);
//
//        final Episode updatedEpisode = SpringUtils.getEpisode(entityManager, 1);
//        DeepAsserts.assertEquals(episode, updatedEpisode, YEAR_FIELD, TOTAL_LENGTH_FIELD, SEASONS_COUNT_FIELD, EPISODES_COUNT_FIELD, SUBTITLES_AS_STRING_METHOD,
//                GENRES_AS_STRING_METHOD);
//        DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT, SpringUtils.getEpisodesCount(entityManager));
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#update(EpisodeTO)} with null argument.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testUpdateWithNullArgument() {
//        episodeFacade.update(null);
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#update(EpisodeTO)} with episode with null ID.
//     */
//    @Test(expected = ValidationException.class)
//    public void testUpdateWithEpisodeWithNullId() {
//        episodeFacade.update(SpringToUtils.newEpisode(objectGenerator));
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#update(EpisodeTO)} with episode with not positive number of episode.
//     */
//    @Test(expected = ValidationException.class)
//    public void testUpdateWithEpisodeWithNotPositiveNumber() {
//        final EpisodeTO episode = SpringToUtils.newEpisodeWithId(objectGenerator);
//        episode.setNumber(0);
//
//        episodeFacade.update(episode);
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#update(EpisodeTO)} with episode with null name.
//     */
//    @Test(expected = ValidationException.class)
//    public void testUpdateWithEpisodeWithNullName() {
//        final EpisodeTO episode = SpringToUtils.newEpisodeWithId(objectGenerator);
//        episode.setName(null);
//
//        episodeFacade.update(episode);
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#update(EpisodeTO)} with episode with empty string as name.
//     */
//    @Test(expected = ValidationException.class)
//    public void testUpdateWithEpisodeWithEmptyName() {
//        final EpisodeTO episode = SpringToUtils.newEpisodeWithId(objectGenerator);
//        episode.setName(null);
//
//        episodeFacade.update(episode);
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#update(EpisodeTO)} with episode with negative length.
//     */
//    @Test(expected = ValidationException.class)
//    public void testUpdateWithEpisodeWithNegativeLength() {
//        final EpisodeTO episode = SpringToUtils.newEpisodeWithId(objectGenerator);
//        episode.setLength(-1);
//
//        episodeFacade.update(episode);
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#update(EpisodeTO)} with episode with null note.
//     */
//    @Test(expected = ValidationException.class)
//    public void testUpdateWithEpisodeWithNullNote() {
//        final EpisodeTO episode = SpringToUtils.newEpisodeWithId(objectGenerator);
//        episode.setNote(null);
//
//        episodeFacade.update(episode);
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#update(EpisodeTO)} with episode with null TO for season.
//     */
//    @Test(expected = ValidationException.class)
//    public void testUpdateWithEpisodeWithNullSeason() {
//        final EpisodeTO episode = SpringToUtils.newEpisodeWithId(objectGenerator);
//        episode.setSeason(null);
//
//        episodeFacade.update(episode);
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#update(EpisodeTO)} with episode with TO for season with null ID.
//     */
//    @Test(expected = ValidationException.class)
//    public void testUpdateWithEpisodeWithSeasonWithNullId() {
//        final EpisodeTO episode = SpringToUtils.newEpisodeWithId(objectGenerator);
//        episode.getSeason().setId(null);
//
//        episodeFacade.update(episode);
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#update(EpisodeTO)} with bad ID.
//     */
//    @Test(expected = RecordNotFoundException.class)
//    public void testUpdateWithBadId() {
//        episodeFacade.update(SpringToUtils.newEpisode(objectGenerator, Integer.MAX_VALUE));
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#update(EpisodeTO)} with not existing season.
//     */
//    @Test(expected = RecordNotFoundException.class)
//    public void testUpdateWithNotExistingSeason() {
//        final EpisodeTO episode = SpringToUtils.newEpisodeWithId(objectGenerator);
//        episode.getSeason().setId(Integer.MAX_VALUE);
//
//        episodeFacade.update(episode);
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#remove(EpisodeTO)}.
//     */
//    @Test
//    public void testRemove() {
//        episodeFacade.remove(SpringToUtils.newEpisode(objectGenerator, 1));
//
//        assertNull(SpringUtils.getEpisode(entityManager, 1));
//        DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT - 1, SpringUtils.getEpisodesCount(entityManager));
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#remove(EpisodeTO)} with null argument.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testRemoveWithNullArgument() {
//        episodeFacade.remove(null);
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#remove(EpisodeTO)} with episode with null ID.
//     */
//    @Test(expected = ValidationException.class)
//    public void testRemoveWithEpisodeWithNullId() {
//        episodeFacade.remove(SpringToUtils.newEpisode(objectGenerator));
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#remove(EpisodeTO)} with bad ID.
//     */
//    @Test(expected = RecordNotFoundException.class)
//    public void testRemoveWithBadId() {
//        episodeFacade.remove(SpringToUtils.newEpisode(objectGenerator, Integer.MAX_VALUE));
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#duplicate(EpisodeTO)}.
//     */
//    @Test
//    public void testDuplicate() {
//        final Episode episode = EntitiesUtils
//                .getEpisode(SpringUtils.SHOWS_COUNT, SpringUtils.SEASONS_PER_SHOW_COUNT, SpringUtils.EPISODES_PER_SEASON_COUNT);
//        episode.setId(SpringUtils.EPISODES_COUNT + 1);
//
//        episodeFacade.duplicate(SpringToUtils.newEpisode(objectGenerator, SpringUtils.EPISODES_COUNT));
//
//        final Episode duplicatedEpisode = SpringUtils.getEpisode(entityManager, SpringUtils.EPISODES_COUNT + 1);
//        DeepAsserts.assertEquals(episode, duplicatedEpisode);
//        DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT + 1, SpringUtils.getEpisodesCount(entityManager));
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#duplicate(EpisodeTO)} with null argument.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testDuplicateWithNullArgument() {
//        episodeFacade.duplicate(null);
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#duplicate(EpisodeTO)} with episode with null ID.
//     */
//    @Test(expected = ValidationException.class)
//    public void testDuplicateWithEpisodeWithNullId() {
//        episodeFacade.duplicate(SpringToUtils.newEpisode(objectGenerator));
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#duplicate(EpisodeTO)} with bad ID.
//     */
//    @Test(expected = RecordNotFoundException.class)
//    public void testDuplicateWithBadId() {
//        episodeFacade.duplicate(SpringToUtils.newEpisode(objectGenerator, Integer.MAX_VALUE));
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#moveUp(EpisodeTO)}.
//     */
//    @Test
//    public void testMoveUp() {
//        final Episode episode1 = EntitiesUtils.getEpisode(1, 1, 1);
//        episode1.setPosition(1);
//        final Episode episode2 = EntitiesUtils.getEpisode(1, 1, 2);
//        episode2.setPosition(0);
//
//        episodeFacade.moveUp(SpringToUtils.newEpisode(objectGenerator, 2));
//        DeepAsserts.assertEquals(episode1, SpringUtils.getEpisode(entityManager, 1));
//        DeepAsserts.assertEquals(episode2, SpringUtils.getEpisode(entityManager, 2));
//        for (int i = 2; i < SpringUtils.EPISODES_COUNT; i++) {
//            final int showNumber = i / SpringUtils.EPISODES_PER_SHOW_COUNT + 1;
//            final int seasonNumber = i % SpringUtils.EPISODES_PER_SHOW_COUNT / SpringUtils.EPISODES_PER_SEASON_COUNT + 1;
//            final int episodeNumber = i % SpringUtils.EPISODES_PER_SEASON_COUNT + 1;
//            DeepAsserts.assertEquals(EntitiesUtils.getEpisode(showNumber, seasonNumber, episodeNumber), SpringUtils.getEpisode(entityManager, i + 1));
//        }
//        DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT, SpringUtils.getEpisodesCount(entityManager));
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#moveUp(EpisodeTO)} with null argument.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testMoveUpWithNullArgument() {
//        episodeFacade.moveUp(null);
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#moveUp(EpisodeTO)} with episode with null ID.
//     */
//    @Test(expected = ValidationException.class)
//    public void testMoveUpWithEpisodeWithNullId() {
//        episodeFacade.moveUp(SpringToUtils.newEpisode(objectGenerator));
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#moveUp(EpisodeTO)} with not movable argument.
//     */
//    @Test(expected = ValidationException.class)
//    public void testMoveUpWithNotMovableArgument() {
//        episodeFacade.moveUp(SpringToUtils.newEpisode(objectGenerator, 1));
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#moveUp(EpisodeTO)} with bad ID.
//     */
//    @Test(expected = RecordNotFoundException.class)
//    public void testMoveUpWithBadId() {
//        episodeFacade.moveUp(SpringToUtils.newEpisode(objectGenerator, Integer.MAX_VALUE));
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#moveDown(EpisodeTO)}.
//     */
//    @Test
//    public void testMoveDown() {
//        final Episode episode1 = EntitiesUtils.getEpisode(1, 1, 1);
//        episode1.setPosition(1);
//        final Episode episode2 = EntitiesUtils.getEpisode(1, 1, 2);
//        episode2.setPosition(0);
//
//        episodeFacade.moveDown(SpringToUtils.newEpisode(objectGenerator, 1));
//        DeepAsserts.assertEquals(episode1, SpringUtils.getEpisode(entityManager, 1));
//        DeepAsserts.assertEquals(episode2, SpringUtils.getEpisode(entityManager, 2));
//        for (int i = 2; i < SpringUtils.EPISODES_COUNT; i++) {
//            final int showNumber = i / SpringUtils.EPISODES_PER_SHOW_COUNT + 1;
//            final int seasonNumber = i % SpringUtils.EPISODES_PER_SHOW_COUNT / SpringUtils.EPISODES_PER_SEASON_COUNT + 1;
//            final int episodeNumber = i % SpringUtils.EPISODES_PER_SEASON_COUNT + 1;
//            DeepAsserts.assertEquals(EntitiesUtils.getEpisode(showNumber, seasonNumber, episodeNumber), SpringUtils.getEpisode(entityManager, i + 1));
//        }
//        DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT, SpringUtils.getEpisodesCount(entityManager));
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#moveDown(EpisodeTO)} with null argument.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testMoveDownWithNullArgument() {
//        episodeFacade.moveDown(null);
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#moveDown(EpisodeTO)} with episode with null ID.
//     */
//    @Test(expected = ValidationException.class)
//    public void testMoveDownWithEpisodeWithNullId() {
//        episodeFacade.moveDown(SpringToUtils.newEpisode(objectGenerator));
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#moveDown(EpisodeTO)} with not movable argument.
//     */
//    @Test(expected = ValidationException.class)
//    public void testMoveDownWithNotMovableArgument() {
//        episodeFacade.moveDown(SpringToUtils.newEpisode(objectGenerator, SpringUtils.EPISODES_COUNT));
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#moveDown(EpisodeTO)} with bad ID.
//     */
//    @Test(expected = RecordNotFoundException.class)
//    public void testMoveDownWithBadId() {
//        episodeFacade.moveDown(SpringToUtils.newEpisode(objectGenerator, Integer.MAX_VALUE));
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#exists(EpisodeTO)}.
//     */
//    @Test
//    public void testExists() {
//        for (int i = 1; i <= SpringUtils.EPISODES_COUNT; i++) {
//            assertTrue(episodeFacade.exists(SpringToUtils.newEpisode(objectGenerator, i)));
//        }
//
//        assertFalse(episodeFacade.exists(SpringToUtils.newEpisode(objectGenerator, Integer.MAX_VALUE)));
//
//        DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT, SpringUtils.getEpisodesCount(entityManager));
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#exists(EpisodeTO)} with null argument.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testExistsWithNullArgument() {
//        episodeFacade.exists(null);
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#exists(EpisodeTO)} with episode with null ID.
//     */
//    @Test(expected = ValidationException.class)
//    public void testExistsWithEpisodeWithNullId() {
//        episodeFacade.exists(SpringToUtils.newEpisode(objectGenerator));
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#findEpisodesBySeason(cz.vhromada.catalog.facade.to.SeasonTO)}.
//     */
//    @Test
//    public void testFindEpisodesBySeason() {
//        for (int i = 0; i < SpringUtils.SEASONS_COUNT; i++) {
//            final int showNumber = i / SpringUtils.SEASONS_PER_SHOW_COUNT + 1;
//            final int seasonNumber = i % SpringUtils.SEASONS_PER_SHOW_COUNT + 1;
//            DeepAsserts.assertEquals(SpringToUtils.getEpisodes(showNumber, seasonNumber),
//                    episodeFacade.findEpisodesBySeason(SpringToUtils.newSeason(objectGenerator, i + 1)), SEASONS_COUNT_FIELD, EPISODES_COUNT_FIELD,
//                    TOTAL_LENGTH_FIELD);
//        }
//        DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT, SpringUtils.getEpisodesCount(entityManager));
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#findEpisodesBySeason(cz.vhromada.catalog.facade.to.SeasonTO)} with null argument.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testFindEpisodesBySeasonWithNullArgument() {
//        episodeFacade.findEpisodesBySeason(null);
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#findEpisodesBySeason(cz.vhromada.catalog.facade.to.SeasonTO)} with season with null ID.
//     */
//    @Test(expected = ValidationException.class)
//    public void testFindEpisodesBySeasonWithNullId() {
//        episodeFacade.findEpisodesBySeason(SpringToUtils.newSeason(objectGenerator));
//    }
//
//    /**
//     * Test method for {@link EpisodeFacade#findEpisodesBySeason(cz.vhromada.catalog.facade.to.SeasonTO)} with bad ID.
//     */
//    @Test(expected = RecordNotFoundException.class)
//    public void testFindEpisodesBySeasonWithBadId() {
//        episodeFacade.findEpisodesBySeason(SpringToUtils.newSeason(objectGenerator, Integer.MAX_VALUE));
//    }
//
//
//}
