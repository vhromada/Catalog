package cz.vhromada.catalog.facade.impl.spring;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.Language;
import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringToUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.commons.TestConstants;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.facade.SeasonFacade;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.generator.ObjectGenerator;
import cz.vhromada.test.DeepAsserts;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * A class represents test for class {@link cz.vhromada.catalog.facade.impl.SeasonFacadeImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeContext.xml")
public class SeasonFacadeImplSpringTest {

    /** Year field */
    private static final String YEAR_FIELD = "year";

    /** Count of seasons field */
    private static final String SEASONS_COUNT_FIELD = "seasonsCount";

    /** Count of episodes field */
    private static final String EPISODES_COUNT_FIELD = "episodesCount";

    /** Total length field */
    private static final String TOTAL_LENGTH_FIELD = "totalLength";

    /** Subtitles as string method */
    private static final String SUBTITLES_AS_STRING_METHOD = "subtitlesAsString";

    /** Genres as string method */
    private static final String GENRES_AS_STRING_METHOD = "genresAsString";

    /** Instance of {@link EntityManager} */
    @Autowired
    private EntityManager entityManager;

    /** Instance of {@link PlatformTransactionManager} */
    @Autowired
    private PlatformTransactionManager transactionManager;

    /** Instance of (@link SeasonFacade} */
    @Autowired
    private SeasonFacade seasonFacade;

    /** Instance of {@link ObjectGenerator} */
    @Autowired
    private ObjectGenerator objectGenerator;

    /** Initializes database. */
    @Before
    public void setUp() {
        SpringUtils.remove(transactionManager, entityManager, Episode.class);
        SpringUtils.remove(transactionManager, entityManager, Season.class);
        SpringUtils.remove(transactionManager, entityManager, Serie.class);
        SpringUtils.updateSequence(transactionManager, entityManager, "series_sq");
        SpringUtils.updateSequence(transactionManager, entityManager, "seasons_sq");
        SpringUtils.updateSequence(transactionManager, entityManager, "episodes_sq");
        for (final Serie serie : SpringEntitiesUtils.getSeries()) {
            serie.setId(null);
            SpringUtils.persist(transactionManager, entityManager, serie);
        }
        for (int i = 1; i <= SpringUtils.SERIES_COUNT; i++) {
            for (final Season season : SpringEntitiesUtils.getSeasons(i)) {
                season.setId(null);
                SpringUtils.persist(transactionManager, entityManager, season);
            }
        }
        for (int i = 1; i <= SpringUtils.SERIES_COUNT; i++) {
            for (int j = 1; j <= SpringUtils.SEASONS_PER_SERIE_COUNT; j++) {
                for (final Episode episode : SpringEntitiesUtils.getEpisodes(i, j)) {
                    episode.setId(null);
                    SpringUtils.persist(transactionManager, entityManager, episode);
                }
            }
        }
    }

    /** Test method for {@link SeasonFacade#getSeason(Integer)}. */
    @Test
    public void testGetSeason() {
        for (int i = 0; i < SpringUtils.SEASONS_COUNT; i++) {
            final int serieNumber = i / SpringUtils.SEASONS_PER_SERIE_COUNT + 1;
            final int seasonNumber = i % SpringUtils.SEASONS_PER_SERIE_COUNT + 1;
            DeepAsserts.assertEquals(SpringToUtils.getSeason(serieNumber, seasonNumber), seasonFacade.getSeason(i + 1), SEASONS_COUNT_FIELD,
                    EPISODES_COUNT_FIELD, TOTAL_LENGTH_FIELD);
        }

        assertNull(seasonFacade.getSeason(Integer.MAX_VALUE));

        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
    }

    /** Test method for {@link SeasonFacade#getSeason(Integer)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testGetSeasonWithNullArgument() {
        seasonFacade.getSeason(null);
    }

    /** Test method for {@link SeasonFacade#add(SeasonTO)}. */
    @Test
    public void testAdd() {
        final SeasonTO season = SpringToUtils.newSeason(objectGenerator);

        seasonFacade.add(season);

        DeepAsserts.assertNotNull(season.getId());
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT + 1, season.getId());
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT, season.getPosition());
        final Season addedSeason = SpringUtils.getSeason(entityManager, SpringUtils.SEASONS_COUNT + 1);
        DeepAsserts.assertEquals(season, addedSeason, YEAR_FIELD, SEASONS_COUNT_FIELD, EPISODES_COUNT_FIELD, TOTAL_LENGTH_FIELD, SUBTITLES_AS_STRING_METHOD,
                GENRES_AS_STRING_METHOD);
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT + 1, SpringUtils.getSeasonsCount(entityManager));
    }

    /** Test method for {@link SeasonFacade#add(SeasonTO)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testAddWithNullArgument() {
        seasonFacade.add(null);
    }

    /** Test method for {@link SeasonFacade#add(SeasonTO)} with season with not null ID. */
    @Test(expected = ValidationException.class)
    public void testAddWithSeasonWithNotNullId() {
        seasonFacade.add(SpringToUtils.newSeasonWithId(objectGenerator));
    }

    /** Test method for {@link SeasonFacade#add(SeasonTO)} with season with not positive number of season. */
    @Test(expected = ValidationException.class)
    public void testAddWithNotPositiveNumber() {
        final SeasonTO season = SpringToUtils.newSeason(objectGenerator);
        season.setNumber(0);

        seasonFacade.add(season);
    }

    /** Test method for {@link SeasonFacade#add(SeasonTO)} with season with bad minimum starting year. */
    @Test(expected = ValidationException.class)
    public void testAddWithBadMinimumStartYear() {
        final SeasonTO season = SpringToUtils.newSeason(objectGenerator);
        season.setStartYear(TestConstants.BAD_MIN_YEAR);

        seasonFacade.add(season);
    }

    /** Test method for {@link SeasonFacade#add(SeasonTO)} with season with bad maximum starting year. */
    @Test(expected = ValidationException.class)
    public void testAddWithBadMaximumStartYear() {
        final SeasonTO season = SpringToUtils.newSeason(objectGenerator);
        season.setStartYear(TestConstants.BAD_MAX_YEAR);

        seasonFacade.add(season);
    }

    /** Test method for {@link SeasonFacade#add(SeasonTO)} with season with bad minimum ending year. */
    @Test(expected = ValidationException.class)
    public void testAddWithBadMinimumEndYear() {
        final SeasonTO season = SpringToUtils.newSeason(objectGenerator);
        season.setEndYear(TestConstants.BAD_MIN_YEAR);

        seasonFacade.add(season);
    }

    /** Test method for {@link SeasonFacade#add(SeasonTO)} with season with bad maximum ending year. */
    @Test(expected = ValidationException.class)
    public void testAddWithBadMaximumEndYear() {
        final SeasonTO season = SpringToUtils.newSeason(objectGenerator);
        season.setEndYear(TestConstants.BAD_MAX_YEAR);

        seasonFacade.add(season);
    }

    /** Test method for {@link SeasonFacade#add(SeasonTO)} with season with starting year greater than ending year. */
    @Test(expected = ValidationException.class)
    public void testAddWithBadYear() {
        final SeasonTO season = SpringToUtils.newSeason(objectGenerator);
        season.setStartYear(season.getEndYear() + 1);

        seasonFacade.add(season);
    }

    /** Test method for {@link SeasonFacade#add(SeasonTO)} with season with null language. */
    @Test(expected = ValidationException.class)
    public void testAddWithNullLanguage() {
        final SeasonTO season = SpringToUtils.newSeason(objectGenerator);
        season.setLanguage(null);

        seasonFacade.add(season);
    }

    /** Test method for {@link SeasonFacade#add(SeasonTO)} with season with null subtitles. */
    @Test(expected = ValidationException.class)
    public void testAddWithNullSubtitles() {
        final SeasonTO season = SpringToUtils.newSeason(objectGenerator);
        season.setSubtitles(null);

        seasonFacade.add(season);
    }

    /** Test method for {@link SeasonFacade#add(SeasonTO)} with season with subtitles with null value. */
    @Test(expected = ValidationException.class)
    public void testAddWithBadSubtitles() {
        final SeasonTO season = SpringToUtils.newSeason(objectGenerator);
        season.setSubtitles(CollectionUtils.newList(objectGenerator.generate(Language.class), null));

        seasonFacade.add(season);
    }

    /** Test method for {@link SeasonFacade#add(SeasonTO)} with season with null note. */
    @Test(expected = ValidationException.class)
    public void testAddWithNullNote() {
        final SeasonTO season = SpringToUtils.newSeason(objectGenerator);
        season.setNote(null);

        seasonFacade.add(season);
    }

    /** Test method for {@link SeasonFacade#add(SeasonTO)} with season with null TO for serie. */
    @Test(expected = ValidationException.class)
    public void testAddWithNullSerie() {
        final SeasonTO season = SpringToUtils.newSeason(objectGenerator);
        season.setSerie(null);

        seasonFacade.add(season);
    }

    /** Test method for {@link SeasonFacade#add(SeasonTO)} with season with TO for serie with null ID. */
    @Test(expected = ValidationException.class)
    public void testAddWithSerieWithNullId() {
        final SeasonTO season = SpringToUtils.newSeason(objectGenerator);
        season.getSerie().setId(null);

        seasonFacade.add(season);
    }

    /** Test method for {@link SeasonFacade#add(SeasonTO)} with season with not existing serie. */
    @Test(expected = RecordNotFoundException.class)
    public void testAddWithSeasonWithNotExistingSerie() {
        final SeasonTO season = SpringToUtils.newSeason(objectGenerator);
        season.getSerie().setId(Integer.MAX_VALUE);

        seasonFacade.add(season);
    }

    /** Test method for {@link SeasonFacade#update(SeasonTO)}. */
    @Test
    public void testUpdate() {
        final SeasonTO season = SpringToUtils.newSeason(objectGenerator, 1);

        seasonFacade.update(season);

        final Season updatedSeason = SpringUtils.getSeason(entityManager, 1);
        DeepAsserts.assertEquals(season, updatedSeason, YEAR_FIELD, SEASONS_COUNT_FIELD, EPISODES_COUNT_FIELD, TOTAL_LENGTH_FIELD, SUBTITLES_AS_STRING_METHOD,
                GENRES_AS_STRING_METHOD);
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
    }

    /** Test method for {@link SeasonFacade#update(SeasonTO)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithNullArgument() {
        seasonFacade.update(null);
    }

    /** Test method for {@link SeasonFacade#update(SeasonTO)} with season with null ID. */
    @Test(expected = ValidationException.class)
    public void testUpdateWithSeasonWithNullId() {
        seasonFacade.update(SpringToUtils.newSeason(objectGenerator));
    }

    /** Test method for {@link SeasonFacade#update(SeasonTO)} with season with not positive number of season. */
    @Test(expected = ValidationException.class)
    public void testUpdateWithNotPositiveNumber() {
        final SeasonTO season = SpringToUtils.newSeasonWithId(objectGenerator);
        season.setNumber(0);

        seasonFacade.update(season);
    }

    /** Test method for {@link SeasonFacade#update(SeasonTO)} with season with bad minimum starting year. */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBadMinimumStartYear() {
        final SeasonTO season = SpringToUtils.newSeasonWithId(objectGenerator);
        season.setStartYear(TestConstants.BAD_MIN_YEAR);

        seasonFacade.update(season);
    }

    /** Test method for {@link SeasonFacade#update(SeasonTO)} with season with bad maximum starting year. */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBadMaximumStartYear() {
        final SeasonTO season = SpringToUtils.newSeasonWithId(objectGenerator);
        season.setStartYear(TestConstants.BAD_MAX_YEAR);

        seasonFacade.update(season);
    }

    /** Test method for {@link SeasonFacade#update(SeasonTO)} with season with bad minimum ending year. */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBadMinimumEndYear() {
        final SeasonTO season = SpringToUtils.newSeasonWithId(objectGenerator);
        season.setEndYear(TestConstants.BAD_MIN_YEAR);

        seasonFacade.update(season);
    }

    /** Test method for {@link SeasonFacade#update(SeasonTO)} with season with bad maximum ending year. */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBadMaximumEndYear() {
        final SeasonTO season = SpringToUtils.newSeasonWithId(objectGenerator);
        season.setEndYear(TestConstants.BAD_MAX_YEAR);

        seasonFacade.update(season);
    }

    /** Test method for {@link SeasonFacade#update(SeasonTO)} with season with starting year greater than ending year. */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBadYear() {
        final SeasonTO season = SpringToUtils.newSeasonWithId(objectGenerator);
        season.setStartYear(season.getEndYear() + 1);

        seasonFacade.update(season);
    }

    /** Test method for {@link SeasonFacade#update(SeasonTO)} with season with null language. */
    @Test(expected = ValidationException.class)
    public void testUpdateWithNullLanguage() {
        final SeasonTO season = SpringToUtils.newSeasonWithId(objectGenerator);
        season.setLanguage(null);

        seasonFacade.update(season);
    }

    /** Test method for {@link SeasonFacade#update(SeasonTO)} with season with null subtitles. */
    @Test(expected = ValidationException.class)
    public void testUpdateWithNullSubtitles() {
        final SeasonTO season = SpringToUtils.newSeasonWithId(objectGenerator);
        season.setSubtitles(null);

        seasonFacade.update(season);
    }

    /** Test method for {@link SeasonFacade#update(SeasonTO)} with season with subtitles with null value. */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBadSubtitles() {
        final SeasonTO season = SpringToUtils.newSeasonWithId(objectGenerator);
        season.setSubtitles(CollectionUtils.newList(objectGenerator.generate(Language.class), null));

        seasonFacade.update(season);
    }

    /** Test method for {@link SeasonFacade#update(SeasonTO)} with season with null note. */
    @Test(expected = ValidationException.class)
    public void testUpdateWithNullNote() {
        final SeasonTO season = SpringToUtils.newSeasonWithId(objectGenerator);
        season.setNote(null);

        seasonFacade.update(season);
    }

    /** Test method for {@link SeasonFacade#update(SeasonTO)} with season with null TO for serie. */
    @Test(expected = ValidationException.class)
    public void testUpdateWithNullSerie() {
        final SeasonTO season = SpringToUtils.newSeasonWithId(objectGenerator);
        season.setSerie(null);

        seasonFacade.update(season);
    }

    /** Test method for {@link SeasonFacade#update(SeasonTO)} with season with TO for serie with null ID. */
    @Test(expected = ValidationException.class)
    public void testUpdateWithSerieWithNullId() {
        final SeasonTO season = SpringToUtils.newSeasonWithId(objectGenerator);
        season.getSerie().setId(null);

        seasonFacade.update(season);
    }

    /** Test method for {@link SeasonFacade#update(SeasonTO)} with bad ID. */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdateWithBadId() {
        seasonFacade.update(SpringToUtils.newSeason(objectGenerator, Integer.MAX_VALUE));
    }

    /** Test method for {@link SeasonFacade#update(SeasonTO)} with not existing serie. */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdateWithNotExistingSerie() {
        final SeasonTO season = SpringToUtils.newSeasonWithId(objectGenerator);
        season.getSerie().setId(Integer.MAX_VALUE);

        seasonFacade.update(season);
    }

    /** Test method for {@link SeasonFacade#remove(SeasonTO)}. */
    @Test
    public void testRemove() {
        seasonFacade.remove(SpringToUtils.newSeason(objectGenerator, 1));

        assertNull(SpringUtils.getSeason(entityManager, 1));
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT - 1, SpringUtils.getSeasonsCount(entityManager));
    }

    /** Test method for {@link SeasonFacade#remove(SeasonTO)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveWithNullArgument() {
        seasonFacade.remove(null);
    }

    /** Test method for {@link SeasonFacade#remove(SeasonTO)} with season with null ID. */
    @Test(expected = ValidationException.class)
    public void testRemoveWithSeasonWithNullId() {
        seasonFacade.remove(SpringToUtils.newSeason(objectGenerator));
    }

    /** Test method for {@link SeasonFacade#remove(SeasonTO)} with bad ID. */
    @Test(expected = RecordNotFoundException.class)
    public void testRemoveWithBadId() {
        seasonFacade.remove(SpringToUtils.newSeason(objectGenerator, Integer.MAX_VALUE));
    }

    /** Test method for {@link SeasonFacade#duplicate(SeasonTO)}. */
    @Test
    public void testDuplicate() {
        final Season season = SpringEntitiesUtils.getSeason(SpringUtils.SERIES_COUNT, SpringUtils.SEASONS_PER_SERIE_COUNT);
        season.setId(SpringUtils.SEASONS_COUNT + 1);

        seasonFacade.duplicate(SpringToUtils.newSeason(objectGenerator, SpringUtils.SEASONS_COUNT));

        final Season duplicatedSeason = SpringUtils.getSeason(entityManager, SpringUtils.SEASONS_COUNT + 1);
        DeepAsserts.assertEquals(season, duplicatedSeason);
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT + 1, SpringUtils.getSeasonsCount(entityManager));
    }

    /** Test method for {@link SeasonFacade#duplicate(SeasonTO)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateWithNullArgument() {
        seasonFacade.duplicate(null);
    }

    /** Test method for {@link SeasonFacade#duplicate(SeasonTO)} with season with null ID. */
    @Test(expected = ValidationException.class)
    public void testDuplicateWithSeasonWithNullId() {
        seasonFacade.duplicate(SpringToUtils.newSeason(objectGenerator));
    }

    /** Test method for {@link SeasonFacade#duplicate(SeasonTO)} with bad ID. */
    @Test(expected = RecordNotFoundException.class)
    public void testDuplicateWithBadId() {
        seasonFacade.duplicate(SpringToUtils.newSeason(objectGenerator, Integer.MAX_VALUE));
    }

    /** Test method for {@link SeasonFacade#moveUp(SeasonTO)}. */
    @Test
    public void testMoveUp() {
        final Season season1 = SpringEntitiesUtils.getSeason(1, 1);
        season1.setPosition(1);
        final Season season2 = SpringEntitiesUtils.getSeason(1, 2);
        season2.setPosition(0);

        seasonFacade.moveUp(SpringToUtils.newSeason(objectGenerator, 2));
        DeepAsserts.assertEquals(season1, SpringUtils.getSeason(entityManager, 1));
        DeepAsserts.assertEquals(season2, SpringUtils.getSeason(entityManager, 2));
        for (int i = 2; i < SpringUtils.SEASONS_COUNT; i++) {
            final int serieNumber = i / SpringUtils.SEASONS_PER_SERIE_COUNT + 1;
            final int seasonNumber = i % SpringUtils.SEASONS_PER_SERIE_COUNT + 1;
            DeepAsserts.assertEquals(SpringEntitiesUtils.getSeason(serieNumber, seasonNumber), SpringUtils.getSeason(entityManager, i + 1));
        }
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
    }

    /** Test method for {@link SeasonFacade#moveUp(SeasonTO)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUpWithNullArgument() {
        seasonFacade.moveUp(null);
    }

    /** Test method for {@link SeasonFacade#moveUp(SeasonTO)} with season with null ID. */
    @Test(expected = ValidationException.class)
    public void testMoveUpWithSeasonWithNullId() {
        seasonFacade.moveUp(SpringToUtils.newSeason(objectGenerator));
    }

    /** Test method for {@link SeasonFacade#moveUp(SeasonTO)} with not moveable argument. */
    @Test(expected = ValidationException.class)
    public void testMoveUpWithNotMoveableArgument() {
        seasonFacade.moveUp(SpringToUtils.newSeason(objectGenerator, 1));
    }

    /** Test method for {@link SeasonFacade#moveUp(SeasonTO)} with bad ID. */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveUpWithBadId() {
        seasonFacade.moveUp(SpringToUtils.newSeason(objectGenerator, Integer.MAX_VALUE));
    }

    /** Test method for {@link SeasonFacade#moveDown(SeasonTO)}. */
    @Test
    public void testMoveDown() {
        final Season season1 = SpringEntitiesUtils.getSeason(1, 1);
        season1.setPosition(1);
        final Season season2 = SpringEntitiesUtils.getSeason(1, 2);
        season2.setPosition(0);

        seasonFacade.moveDown(SpringToUtils.newSeason(objectGenerator, 1));
        DeepAsserts.assertEquals(season1, SpringUtils.getSeason(entityManager, 1));
        DeepAsserts.assertEquals(season2, SpringUtils.getSeason(entityManager, 2));
        for (int i = 2; i < SpringUtils.SEASONS_COUNT; i++) {
            final int serieNumber = i / SpringUtils.SEASONS_PER_SERIE_COUNT + 1;
            final int seasonNumber = i % SpringUtils.SEASONS_PER_SERIE_COUNT + 1;
            DeepAsserts.assertEquals(SpringEntitiesUtils.getSeason(serieNumber, seasonNumber), SpringUtils.getSeason(entityManager, i + 1));
        }
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
    }

    /** Test method for {@link SeasonFacade#moveDown(SeasonTO)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDownWithNullArgument() {
        seasonFacade.moveDown(null);
    }

    /** Test method for {@link SeasonFacade#moveDown(SeasonTO)} with season with null ID. */
    @Test(expected = ValidationException.class)
    public void testMoveDownWithSeasonWithNullId() {
        seasonFacade.moveDown(SpringToUtils.newSeason(objectGenerator));
    }

    /** Test method for {@link SeasonFacade#moveDown(SeasonTO)} with not moveable argument. */
    @Test(expected = ValidationException.class)
    public void testMoveDownWithNotMoveableArgument() {
        seasonFacade.moveDown(SpringToUtils.newSeason(objectGenerator, SpringUtils.SEASONS_COUNT));
    }

    /** Test method for {@link SeasonFacade#moveDown(SeasonTO)} with bad ID. */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveDownWithBadId() {
        seasonFacade.moveDown(SpringToUtils.newSeason(objectGenerator, Integer.MAX_VALUE));
    }

    /** Test method for {@link SeasonFacade#exists(SeasonTO)}. */
    @Test
    public void testExists() {
        for (int i = 1; i <= SpringUtils.SEASONS_COUNT; i++) {
            assertTrue(seasonFacade.exists(SpringToUtils.newSeason(objectGenerator, i)));
        }

        assertFalse(seasonFacade.exists(SpringToUtils.newSeason(objectGenerator, Integer.MAX_VALUE)));

        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
    }

    /** Test method for {@link SeasonFacade#exists(SeasonTO)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testExistsWithNullArgument() {
        seasonFacade.exists(null);
    }

    /** Test method for {@link SeasonFacade#exists(SeasonTO)} with season with null ID. */
    @Test(expected = ValidationException.class)
    public void testExistsWithSeasonWithNullId() {
        seasonFacade.exists(SpringToUtils.newSeason(objectGenerator));
    }

    /** Test method for {@link SeasonFacade#findSeasonsBySerie(cz.vhromada.catalog.facade.to.SerieTO)}. */
    @Test
    public void testFindSeasonsBySerie() {
        for (int i = 1; i <= SpringUtils.SERIES_COUNT; i++) {
            final List<SeasonTO> expectedSeasons = SpringToUtils.getSeasons(i);
            final List<SeasonTO> actualSeasons = seasonFacade.findSeasonsBySerie(SpringToUtils.newSerie(objectGenerator, i));
            DeepAsserts.assertEquals(expectedSeasons, actualSeasons, SEASONS_COUNT_FIELD, EPISODES_COUNT_FIELD, TOTAL_LENGTH_FIELD);
        }
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
    }

    /** Test method for {@link SeasonFacade#findSeasonsBySerie(cz.vhromada.catalog.facade.to.SerieTO)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testFindSeasonsBySerieWithNullArgument() {
        seasonFacade.findSeasonsBySerie(null);
    }

    /** Test method for {@link SeasonFacade#findSeasonsBySerie(cz.vhromada.catalog.facade.to.SerieTO)} with serie with null ID. */
    @Test(expected = ValidationException.class)
    public void testFindSeasonsBySerieWithNullId() {
        seasonFacade.findSeasonsBySerie(SpringToUtils.newSerie(objectGenerator));
    }

    /** Test method for {@link SeasonFacade#findSeasonsBySerie(cz.vhromada.catalog.facade.to.SerieTO)} with bad ID. */
    @Test(expected = RecordNotFoundException.class)
    public void testFindSeasonsBySerieWithBadId() {
        seasonFacade.findSeasonsBySerie(SpringToUtils.newSerie(objectGenerator, Integer.MAX_VALUE));
    }

}
