package cz.vhromada.catalog.facade.impl.spring;

import static cz.vhromada.catalog.commons.SpringUtils.SEASONS_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.SEASONS_PER_SERIE_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.SERIES_COUNT;
import static cz.vhromada.catalog.commons.TestConstants.BAD_MAX_YEAR;
import static cz.vhromada.catalog.commons.TestConstants.BAD_MIN_YEAR;
import static cz.vhromada.catalog.commons.TestConstants.BAD_SUBTITLES;
import static cz.vhromada.catalog.commons.TestConstants.CSFD;
import static cz.vhromada.catalog.commons.TestConstants.CZECH_NAME;
import static cz.vhromada.catalog.commons.TestConstants.END_YEAR;
import static cz.vhromada.catalog.commons.TestConstants.IMDB;
import static cz.vhromada.catalog.commons.TestConstants.INNER_COUNT;
import static cz.vhromada.catalog.commons.TestConstants.INNER_ID;
import static cz.vhromada.catalog.commons.TestConstants.INNER_INNER_COUNT;
import static cz.vhromada.catalog.commons.TestConstants.NEGATIVE_TIME;
import static cz.vhromada.catalog.commons.TestConstants.NOTE;
import static cz.vhromada.catalog.commons.TestConstants.ORIGINAL_NAME;
import static cz.vhromada.catalog.commons.TestConstants.PICTURE;
import static cz.vhromada.catalog.commons.TestConstants.POSITION;
import static cz.vhromada.catalog.commons.TestConstants.PRIMARY_ID;
import static cz.vhromada.catalog.commons.TestConstants.SECONDARY_INNER_ID;
import static cz.vhromada.catalog.commons.TestConstants.START_YEAR;
import static cz.vhromada.catalog.commons.TestConstants.TOTAL_LENGTH;
import static cz.vhromada.catalog.commons.TestConstants.WIKIPEDIA_CZ;
import static cz.vhromada.catalog.commons.TestConstants.WIKIPEDIA_EN;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.EntityGenerator;
import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringToUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.commons.ToGenerator;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.facade.SeasonFacade;
import cz.vhromada.catalog.facade.impl.SeasonFacadeImpl;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.facade.to.SerieTO;
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
 * A class represents test for class {@link SeasonFacadeImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeContext.xml")
//TODO vhromada 31.10.2014: implement object generator
public class SeasonFacadeImplSpringTest {

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
		for (Serie serie : SpringEntitiesUtils.getSeries()) {
			serie.setId(null);
			SpringUtils.persist(transactionManager, entityManager, serie);
		}
		for (int i = 1; i <= SERIES_COUNT; i++) {
			for (Season season : SpringEntitiesUtils.getSeasons(i)) {
				season.setId(null);
				SpringUtils.persist(transactionManager, entityManager, season);
			}
		}
		for (int i = 1; i <= SERIES_COUNT; i++) {
			for (int j = 1; j <= SEASONS_PER_SERIE_COUNT; j++) {
				for (Episode episode : SpringEntitiesUtils.getEpisodes(i, j)) {
					episode.setId(null);
					SpringUtils.persist(transactionManager, entityManager, episode);
				}
			}
		}
	}

	/** Test method for {@link SeasonFacade#getSeason(Integer)}. */
	@Test
	public void testGetSeason() {
		for (int i = 0; i < SEASONS_COUNT; i++) {
			final int serieNumber = i / SEASONS_PER_SERIE_COUNT + 1;
			final int seasonNumber = i % SEASONS_PER_SERIE_COUNT + 1;
			DeepAsserts.assertEquals(SpringToUtils.getSeason(serieNumber, seasonNumber), seasonFacade.getSeason(i + 1), "seasonsCount", "episodesCount",
					"totalLength");
		}

		assertNull(seasonFacade.getSeason(Integer.MAX_VALUE));

		DeepAsserts.assertEquals(SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
	}

	/** Test method for {@link SeasonFacade#getSeason(Integer)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testGetSeasonWithNullArgument() {
		seasonFacade.getSeason(null);
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)}. */
	@Test
	public void testAdd() {
		final SeasonTO season = ToGenerator.createSeason(SpringToUtils.getSerie(1));
		final Season expectedSeason = EntityGenerator.createSeason(SEASONS_COUNT + 1, SpringEntitiesUtils.getSerie(1));
		expectedSeason.setPosition(SEASONS_COUNT);

		seasonFacade.add(season);

		DeepAsserts.assertNotNull(season.getId());
		DeepAsserts.assertEquals(SEASONS_COUNT + 1, season.getId());
		DeepAsserts.assertEquals(SEASONS_COUNT, season.getPosition());
		final Season addedSeason = SpringUtils.getSeason(entityManager, SEASONS_COUNT + 1);
		DeepAsserts.assertEquals(season, addedSeason, "year", "subtitlesAsString", "episodesCount", "totalLength", "seasonsCount", "genresAsString");
		DeepAsserts.assertEquals(expectedSeason, addedSeason);
		DeepAsserts.assertEquals(SEASONS_COUNT + 1, SpringUtils.getSeasonsCount(entityManager));
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testAddWithNullArgument() {
		seasonFacade.add(null);
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)} with season with not null ID. */
	@Test(expected = ValidationException.class)
	public void testAddWithSeasonWithNotNullId() {
		seasonFacade.add(ToGenerator.createSeason(Integer.MAX_VALUE, createSerie(INNER_ID)));
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)} with season with not positive number of season. */
	@Test(expected = ValidationException.class)
	public void testAddWithNotPositiveNumber() {
		final SeasonTO season = ToGenerator.createSeason(createSerie(INNER_ID));
		season.setNumber(0);

		seasonFacade.add(season);
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)} with season with bad minimum starting year. */
	@Test(expected = ValidationException.class)
	public void testAddWithBadMinimumStartYear() {
		final SeasonTO season = ToGenerator.createSeason(createSerie(INNER_ID));
		season.setStartYear(BAD_MIN_YEAR);

		seasonFacade.add(season);
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)} with season with bad maximum starting year. */
	@Test(expected = ValidationException.class)
	public void testAddWithBadMaximumStartYear() {
		final SeasonTO season = ToGenerator.createSeason(createSerie(INNER_ID));
		season.setStartYear(BAD_MAX_YEAR);

		seasonFacade.add(season);
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)} with season with bad minimum ending year. */
	@Test(expected = ValidationException.class)
	public void testAddWithBadMinimumEndYear() {
		final SeasonTO season = ToGenerator.createSeason(createSerie(INNER_ID));
		season.setEndYear(BAD_MIN_YEAR);

		seasonFacade.add(season);
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)} with season with bad maximum ending year. */
	@Test(expected = ValidationException.class)
	public void testAddWithBadMaximumEndYear() {
		final SeasonTO season = ToGenerator.createSeason(createSerie(INNER_ID));
		season.setEndYear(BAD_MAX_YEAR);

		seasonFacade.add(season);
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)} with season with starting year greater than ending year. */
	@Test(expected = ValidationException.class)
	public void testAddWithBadYear() {
		final SeasonTO season = ToGenerator.createSeason(createSerie(INNER_ID));
		season.setStartYear(END_YEAR);
		season.setEndYear(START_YEAR);

		seasonFacade.add(season);
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)} with season with null language. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullLanguage() {
		final SeasonTO season = ToGenerator.createSeason(createSerie(INNER_ID));
		season.setLanguage(null);

		seasonFacade.add(season);
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)} with season with null subtitles. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullSubtitles() {
		final SeasonTO season = ToGenerator.createSeason(createSerie(INNER_ID));
		season.setSubtitles(null);

		seasonFacade.add(season);
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)} with season with subtitles with null value. */
	@Test(expected = ValidationException.class)
	public void testAddWithBadSubtitles() {
		final SeasonTO season = ToGenerator.createSeason(createSerie(INNER_ID));
		season.setSubtitles(BAD_SUBTITLES);

		seasonFacade.add(season);
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)} with season with negative count of episodes. */
	@Test(expected = ValidationException.class)
	public void testAddWithNegativeEpisodesCount() {
		final SeasonTO season = ToGenerator.createSeason(createSerie(INNER_ID));
		season.setEpisodesCount(-1);

		seasonFacade.add(season);
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)} with season with null total length of episodes. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullTotalLength() {
		final SeasonTO season = ToGenerator.createSeason(createSerie(INNER_ID));
		season.setTotalLength(null);

		seasonFacade.add(season);
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)} with season with negative total length of episodes. */
	@Test(expected = ValidationException.class)
	public void testAddWithNegativeTotalLength() {
		final SeasonTO season = ToGenerator.createSeason(createSerie(INNER_ID));
		season.setTotalLength(NEGATIVE_TIME);

		seasonFacade.add(season);
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)} with season with null note. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullNote() {
		final SeasonTO season = ToGenerator.createSeason(createSerie(INNER_ID));
		season.setNote(null);

		seasonFacade.add(season);
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)} with season with null TO for serie. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullSerie() {
		seasonFacade.add(ToGenerator.createSeason());
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)} with season with TO for serie with null ID. */
	@Test(expected = ValidationException.class)
	public void testAddWithSerieWithNullId() {
		seasonFacade.add(ToGenerator.createSeason(createSerie()));
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)} with season with not existing serie. */
	@Test(expected = RecordNotFoundException.class)
	public void testAddWithSeasonWithNotExistingSerie() {
		seasonFacade.add(ToGenerator.createSeason(createSerie(Integer.MAX_VALUE)));
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)}. */
	@Test
	public void testUpdate() {
		final SeasonTO season = ToGenerator.createSeason(1, SpringToUtils.getSerie(1));
		final Season expectedSeason = EntityGenerator.createSeason(1, SpringEntitiesUtils.getSerie(1));

		seasonFacade.update(season);

		final Season updatedSeason = SpringUtils.getSeason(entityManager, 1);
		DeepAsserts.assertEquals(season, updatedSeason, "year", "subtitlesAsString", "episodesCount", "totalLength", "seasonsCount", "genresAsString");
		DeepAsserts.assertEquals(expectedSeason, updatedSeason);
		DeepAsserts.assertEquals(SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateWithNullArgument() {
		seasonFacade.update(null);
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)} with season with null ID. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithSeasonWithNullId() {
		seasonFacade.update(ToGenerator.createSeason(createSerie(INNER_ID)));
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)} with season with not positive number of season. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNotPositiveNumber() {
		final SeasonTO season = ToGenerator.createSeason(PRIMARY_ID, createSerie(INNER_ID));
		season.setNumber(0);

		seasonFacade.update(season);
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)} with season with bad minimum starting year. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithBadMinimumStartYear() {
		final SeasonTO season = ToGenerator.createSeason(PRIMARY_ID, createSerie(INNER_ID));
		season.setStartYear(BAD_MIN_YEAR);

		seasonFacade.update(season);
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)} with season with bad maximum starting year. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithBadMaximumStartYear() {
		final SeasonTO season = ToGenerator.createSeason(PRIMARY_ID, createSerie(INNER_ID));
		season.setStartYear(BAD_MAX_YEAR);

		seasonFacade.update(season);
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)} with season with bad minimum ending year. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithBadMinimumEndYear() {
		final SeasonTO season = ToGenerator.createSeason(PRIMARY_ID, createSerie(INNER_ID));
		season.setEndYear(BAD_MIN_YEAR);

		seasonFacade.update(season);
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)} with season with bad maximum ending year. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithBadMaximumEndYear() {
		final SeasonTO season = ToGenerator.createSeason(PRIMARY_ID, createSerie(INNER_ID));
		season.setEndYear(BAD_MAX_YEAR);

		seasonFacade.update(season);
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)} with season with starting year greater than ending year. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithBadYear() {
		final SeasonTO season = ToGenerator.createSeason(PRIMARY_ID, createSerie(INNER_ID));
		season.setStartYear(END_YEAR);
		season.setEndYear(START_YEAR);

		seasonFacade.update(season);
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)} with season with null language. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNullLanguage() {
		final SeasonTO season = ToGenerator.createSeason(PRIMARY_ID, createSerie(INNER_ID));
		season.setLanguage(null);

		seasonFacade.update(season);
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)} with season with null subtitles. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNullSubtitles() {
		final SeasonTO season = ToGenerator.createSeason(PRIMARY_ID, createSerie(INNER_ID));
		season.setSubtitles(null);

		seasonFacade.update(season);
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)} with season with subtitles with null value. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithBadSubtitles() {
		final SeasonTO season = ToGenerator.createSeason(PRIMARY_ID, createSerie(INNER_ID));
		season.setSubtitles(BAD_SUBTITLES);

		seasonFacade.update(season);
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)} with season with negative count of episodes. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNegativeEpisodesCount() {
		final SeasonTO season = ToGenerator.createSeason(PRIMARY_ID, createSerie(INNER_ID));
		season.setEpisodesCount(-1);

		seasonFacade.update(season);
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)} with season with null total length of episodes. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNullTotalLength() {
		final SeasonTO season = ToGenerator.createSeason(PRIMARY_ID, createSerie(INNER_ID));
		season.setTotalLength(null);

		seasonFacade.update(season);
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)} with season with negative total length of episodes. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNegativeTotalLength() {
		final SeasonTO season = ToGenerator.createSeason(PRIMARY_ID, createSerie(INNER_ID));
		season.setTotalLength(NEGATIVE_TIME);

		seasonFacade.update(season);
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)} with season with null note. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNullNote() {
		final SeasonTO season = ToGenerator.createSeason(PRIMARY_ID, createSerie(INNER_ID));
		season.setNote(null);

		seasonFacade.update(season);
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)} with season with null TO for serie. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNullSerie() {
		seasonFacade.update(ToGenerator.createSeason(PRIMARY_ID));
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)} with season with TO for serie with null ID. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithSerieWithNullId() {
		seasonFacade.update(ToGenerator.createSeason(PRIMARY_ID, createSerie()));
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testUpdateWithBadId() {
		seasonFacade.update(ToGenerator.createSeason(Integer.MAX_VALUE, createSerie(INNER_ID)));
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)} with not existing serie. */
	@Test(expected = RecordNotFoundException.class)
	public void testUpdateWithNotExistingSerie() {
		seasonFacade.update(ToGenerator.createSeason(PRIMARY_ID, createSerie(Integer.MAX_VALUE)));
	}

	/** Test method for {@link SeasonFacade#remove(SeasonTO)}. */
	@Test
	public void testRemove() {
		seasonFacade.remove(ToGenerator.createSeason(1));

		assertNull(SpringUtils.getSeason(entityManager, 1));
		DeepAsserts.assertEquals(SEASONS_COUNT - 1, SpringUtils.getSeasonsCount(entityManager));
	}

	/** Test method for {@link SeasonFacade#remove(SeasonTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveWithNullArgument() {
		seasonFacade.remove(null);
	}

	/** Test method for {@link SeasonFacade#remove(SeasonTO)} with season with null ID. */
	@Test(expected = ValidationException.class)
	public void testRemoveWithSeasonWithNullId() {
		seasonFacade.remove(ToGenerator.createSeason());
	}

	/** Test method for {@link SeasonFacade#remove(SeasonTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testRemoveWithBadId() {
		seasonFacade.remove(ToGenerator.createSeason(Integer.MAX_VALUE));
	}

	/** Test method for {@link SeasonFacade#duplicate(SeasonTO)}. */
	@Test
	public void testDuplicate() {
		final Season season = SpringEntitiesUtils.getSeason(SERIES_COUNT, SEASONS_PER_SERIE_COUNT);
		season.setId(SEASONS_COUNT + 1);

		seasonFacade.duplicate(ToGenerator.createSeason(SEASONS_COUNT));

		final Season duplicatedSeason = SpringUtils.getSeason(entityManager, SEASONS_COUNT + 1);
		DeepAsserts.assertEquals(season, duplicatedSeason);
		DeepAsserts.assertEquals(SEASONS_COUNT + 1, SpringUtils.getSeasonsCount(entityManager));
	}

	/** Test method for {@link SeasonFacade#duplicate(SeasonTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testDuplicateWithNullArgument() {
		seasonFacade.duplicate(null);
	}

	/** Test method for {@link SeasonFacade#duplicate(SeasonTO)} with season with null ID. */
	@Test(expected = ValidationException.class)
	public void testDuplicateWithSeasonWithNullId() {
		seasonFacade.duplicate(ToGenerator.createSeason());
	}

	/** Test method for {@link SeasonFacade#duplicate(SeasonTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testDuplicateWithBadId() {
		seasonFacade.duplicate(ToGenerator.createSeason(Integer.MAX_VALUE));
	}

	/** Test method for {@link SeasonFacade#moveUp(SeasonTO)}. */
	@Test
	public void testMoveUp() {
		final Season season1 = SpringEntitiesUtils.getSeason(1, 1);
		season1.setPosition(1);
		final Season season2 = SpringEntitiesUtils.getSeason(1, 2);
		season2.setPosition(0);

		seasonFacade.moveUp(ToGenerator.createSeason(2));
		DeepAsserts.assertEquals(season1, SpringUtils.getSeason(entityManager, 1));
		DeepAsserts.assertEquals(season2, SpringUtils.getSeason(entityManager, 2));
		for (int i = 2; i < SEASONS_COUNT; i++) {
			final int serieNumber = i / SEASONS_PER_SERIE_COUNT + 1;
			final int seasonNumber = i % SEASONS_PER_SERIE_COUNT + 1;
			DeepAsserts.assertEquals(SpringEntitiesUtils.getSeason(serieNumber, seasonNumber), SpringUtils.getSeason(entityManager, i + 1));
		}
		DeepAsserts.assertEquals(SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
	}

	/** Test method for {@link SeasonFacade#moveUp(SeasonTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testMoveUpWithNullArgument() {
		seasonFacade.moveUp(null);
	}

	/** Test method for {@link SeasonFacade#moveUp(SeasonTO)} with season with null ID. */
	@Test(expected = ValidationException.class)
	public void testMoveUpWithSeasonWithNullId() {
		seasonFacade.moveUp(ToGenerator.createSeason());
	}

	/** Test method for {@link SeasonFacade#moveUp(SeasonTO)} with not moveable argument. */
	@Test(expected = ValidationException.class)
	public void testMoveUpWithNotMoveableArgument() {
		seasonFacade.moveUp(ToGenerator.createSeason(1));
	}

	/** Test method for {@link SeasonFacade#moveUp(SeasonTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testMoveUpWithBadId() {
		seasonFacade.moveUp(ToGenerator.createSeason(Integer.MAX_VALUE));
	}

	/** Test method for {@link SeasonFacade#moveDown(SeasonTO)}. */
	@Test
	public void testMoveDown() {
		final Season season1 = SpringEntitiesUtils.getSeason(1, 1);
		season1.setPosition(1);
		final Season season2 = SpringEntitiesUtils.getSeason(1, 2);
		season2.setPosition(0);

		seasonFacade.moveDown(ToGenerator.createSeason(1));
		DeepAsserts.assertEquals(season1, SpringUtils.getSeason(entityManager, 1));
		DeepAsserts.assertEquals(season2, SpringUtils.getSeason(entityManager, 2));
		for (int i = 2; i < SEASONS_COUNT; i++) {
			final int serieNumber = i / SEASONS_PER_SERIE_COUNT + 1;
			final int seasonNumber = i % SEASONS_PER_SERIE_COUNT + 1;
			DeepAsserts.assertEquals(SpringEntitiesUtils.getSeason(serieNumber, seasonNumber), SpringUtils.getSeason(entityManager, i + 1));
		}
		DeepAsserts.assertEquals(SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
	}

	/** Test method for {@link SeasonFacade#moveDown(SeasonTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testMoveDownWithNullArgument() {
		seasonFacade.moveDown(null);
	}

	/** Test method for {@link SeasonFacade#moveDown(SeasonTO)} with season with null ID. */
	@Test(expected = ValidationException.class)
	public void testMoveDownWithSeasonWithNullId() {
		seasonFacade.moveDown(ToGenerator.createSeason());
	}

	/** Test method for {@link SeasonFacade#moveDown(SeasonTO)} with not moveable argument. */
	@Test(expected = ValidationException.class)
	public void testMoveDownWithNotMoveableArgument() {
		seasonFacade.moveDown(ToGenerator.createSeason(SEASONS_COUNT));
	}

	/** Test method for {@link SeasonFacade#moveDown(SeasonTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testMoveDownWithBadId() {
		seasonFacade.moveDown(ToGenerator.createSeason(Integer.MAX_VALUE));
	}

	/** Test method for {@link SeasonFacade#exists(SeasonTO)}. */
	@Test
	public void testExists() {
		for (int i = 1; i <= SEASONS_COUNT; i++) {
			assertTrue(seasonFacade.exists(ToGenerator.createSeason(i)));
		}

		assertFalse(seasonFacade.exists(ToGenerator.createSeason(Integer.MAX_VALUE)));

		DeepAsserts.assertEquals(SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
	}

	/** Test method for {@link SeasonFacade#exists(SeasonTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testExistsWithNullArgument() {
		seasonFacade.exists(null);
	}

	/** Test method for {@link SeasonFacade#exists(SeasonTO)} with season with null ID. */
	@Test(expected = ValidationException.class)
	public void testExistsWithSeasonWithNullId() {
		seasonFacade.exists(ToGenerator.createSeason());
	}

	/** Test method for {@link SeasonFacade#findSeasonsBySerie(SerieTO)}. */
	@Test
	public void testFindSeasonsBySerie() {
		for (int i = 1; i <= SERIES_COUNT; i++) {
			final List<SeasonTO> expectedSeasons = SpringToUtils.getSeasons(i);
			final List<SeasonTO> actualSeasons = seasonFacade.findSeasonsBySerie(createSerie(i));
			DeepAsserts.assertEquals(expectedSeasons, actualSeasons, "seasonsCount", "episodesCount", "totalLength");
			for (int j = 0; j < expectedSeasons.size(); j++) {
				DeepAsserts.assertEquals(expectedSeasons.get(j).getEpisodesCount(), actualSeasons.get(j).getEpisodesCount());
				DeepAsserts.assertEquals(expectedSeasons.get(j).getTotalLength(), actualSeasons.get(j).getTotalLength());
			}
		}
		DeepAsserts.assertEquals(SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
	}

	/** Test method for {@link SeasonFacade#findSeasonsBySerie(SerieTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testFindSeasonsBySerieWithNullArgument() {
		seasonFacade.findSeasonsBySerie(null);
	}

	/** Test method for {@link SeasonFacade#findSeasonsBySerie(SerieTO)} with serie with null ID. */
	@Test(expected = ValidationException.class)
	public void testFindSeasonsBySerieWithNullId() {
		seasonFacade.findSeasonsBySerie(createSerie());
	}

	/** Test method for {@link SeasonFacade#findSeasonsBySerie(SerieTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testFindSeasonsBySerieWithBadId() {
		seasonFacade.findSeasonsBySerie(createSerie(Integer.MAX_VALUE));
	}

	/**
	 * Returns new TO for serie.
	 *
	 * @return new TO for serie
	 */
	@Deprecated
	private static SerieTO createSerie() {
		final SerieTO serie = new SerieTO();
		serie.setCzechName(CZECH_NAME);
		serie.setOriginalName(ORIGINAL_NAME);
		serie.setCsfd(CSFD);
		serie.setImdbCode(IMDB);
		serie.setWikiEn(WIKIPEDIA_EN);
		serie.setWikiCz(WIKIPEDIA_CZ);
		serie.setPicture(PICTURE);
		serie.setSeasonsCount(INNER_COUNT);
		serie.setEpisodesCount(INNER_INNER_COUNT);
		serie.setTotalLength(TOTAL_LENGTH);
		serie.setNote(NOTE);
		serie.setPosition(POSITION);
		serie.setGenres(CollectionUtils.newList(createGenre(INNER_ID), createGenre(SECONDARY_INNER_ID)));
		return serie;
	}

	/**
	 * Returns new TO for genre with specified ID.
	 *
	 * @param id ID
	 * @return new TO for genre with specified ID
	 */
	@Deprecated
	private static GenreTO createGenre(final Integer id) {
		final GenreTO genre = new GenreTO();
		genre.setId(id);
		return genre;
	}

	/**
	 * Returns new TO for serie with specified ID.
	 *
	 * @param id ID
	 * @return new TO for serie with specified ID
	 */
	@Deprecated
	private static SerieTO createSerie(final Integer id) {
		final SerieTO serie = createSerie();
		serie.setId(id);
		return serie;
	}

}
