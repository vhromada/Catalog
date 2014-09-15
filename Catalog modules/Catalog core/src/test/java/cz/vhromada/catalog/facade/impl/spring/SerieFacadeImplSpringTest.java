package cz.vhromada.catalog.facade.impl.spring;

import static cz.vhromada.catalog.common.SpringUtils.EPISODES_COUNT;
import static cz.vhromada.catalog.common.SpringUtils.SEASONS_COUNT;
import static cz.vhromada.catalog.common.SpringUtils.SEASONS_PER_SERIE_COUNT;
import static cz.vhromada.catalog.common.SpringUtils.SERIES_COUNT;
import static cz.vhromada.catalog.common.TestConstants.BAD_MAX_IMDB_CODE;
import static cz.vhromada.catalog.common.TestConstants.BAD_MIN_IMDB_CODE;
import static cz.vhromada.catalog.common.TestConstants.INNER_ID;
import static cz.vhromada.catalog.common.TestConstants.NEGATIVE_TIME;
import static cz.vhromada.catalog.common.TestConstants.PRIMARY_ID;
import static cz.vhromada.catalog.common.TestConstants.SECONDARY_INNER_ID;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.common.SpringEntitiesUtils;
import cz.vhromada.catalog.common.SpringToUtils;
import cz.vhromada.catalog.common.SpringUtils;
import cz.vhromada.catalog.common.ToGenerator;
import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.facade.SerieFacade;
import cz.vhromada.catalog.facade.impl.SerieFacadeImpl;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.SerieTO;
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
 * A class represents test for class {@link SerieFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeContext.xml")
public class SerieFacadeImplSpringTest {

	/** Instance of {@link EntityManager} */
	@Autowired
	private EntityManager entityManager;

	/** Instance of {@link PlatformTransactionManager} */
	@Autowired
	private PlatformTransactionManager transactionManager;

	/** Instance of {@link SerieFacade} */
	@Autowired
	private SerieFacade serieFacade;

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

	/** Test method for {@link SerieFacade#newData()}. */
	@Test
	public void testNewData() {
		serieFacade.newData();

		DeepAsserts.assertEquals(0, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieFacade#getSeries()}. */
	@Test
	public void testGetSeries() {
		DeepAsserts.assertEquals(SpringToUtils.getSeries(), serieFacade.getSeries());
		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieFacade#getSerie(Integer)}. */
	@Test
	public void testGetSerie() {
		for (int i = 1; i <= SERIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringToUtils.getSerie(i), serieFacade.getSerie(i));
		}

		assertNull(serieFacade.getSerie(Integer.MAX_VALUE));

		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieFacade#getSerie(Integer)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testGetSerieWithNullArgument() {
		serieFacade.getSerie(null);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)}. */
	@Test
	public void testAdd() {
		final SerieTO serie = ToGenerator.createSerie();
		serie.setGenres(CollectionUtils.newList(SpringToUtils.getGenre(4)));
		final Serie expectedSerie = EntityGenerator.createSerie(SERIES_COUNT + 1);
		expectedSerie.setPosition(SERIES_COUNT);
		expectedSerie.setGenres(CollectionUtils.newList(SpringEntitiesUtils.getGenre(4)));

		serieFacade.add(serie);

		DeepAsserts.assertNotNull(serie.getId());
		DeepAsserts.assertEquals(SERIES_COUNT + 1, serie.getId());
		final Serie addedSerie = SpringUtils.getSerie(entityManager, SERIES_COUNT + 1);
		DeepAsserts.assertEquals(serie, addedSerie, "seasonsCount", "episodesCount", "totalLength", "genresAsString");
		DeepAsserts.assertEquals(expectedSerie, addedSerie);
		DeepAsserts.assertEquals(SERIES_COUNT + 1, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testAddWithNullArgument() {
		serieFacade.add(null);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with not null ID. */
	@Test(expected = ValidationException.class)
	public void testAddWithSerieWithNotNullId() {
		serieFacade.add(ToGenerator.createSerie(Integer.MAX_VALUE));
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with null czech name. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullCzechName() {
		final SerieTO serie = ToGenerator.createSerie();
		serie.setCzechName(null);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with empty string as czech name. */
	@Test(expected = ValidationException.class)
	public void testAddWithEmptyCzechName() {
		final SerieTO serie = ToGenerator.createSerie();
		serie.setCzechName("");

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with null original name. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullOriginalName() {
		final SerieTO serie = ToGenerator.createSerie();
		serie.setOriginalName(null);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with empty string as original name. */
	@Test(expected = ValidationException.class)
	public void testAddWithEmptyOriginalName() {
		final SerieTO serie = ToGenerator.createSerie();
		serie.setOriginalName("");

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with null URL to ČSFD page about serie. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullCsfd() {
		final SerieTO serie = ToGenerator.createSerie();
		serie.setCsfd(null);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with bad minimal IMDB code. */
	@Test(expected = ValidationException.class)
	public void testAddWithBadMinimalImdb() {
		final SerieTO serie = ToGenerator.createSerie();
		serie.setImdbCode(BAD_MIN_IMDB_CODE);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with bad divider IMDB code. */
	@Test(expected = ValidationException.class)
	public void testAddWithBadDividerImdb() {
		final SerieTO serie = ToGenerator.createSerie();
		serie.setImdbCode(0);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with bad maximal IMDB code. */
	@Test(expected = ValidationException.class)
	public void testAddWithBadMaximalImdb() {
		final SerieTO serie = ToGenerator.createSerie();
		serie.setImdbCode(BAD_MAX_IMDB_CODE);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with null URL to english Wikipedia page about serie. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullWikiEn() {
		final SerieTO serie = ToGenerator.createSerie();
		serie.setWikiEn(null);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with null URL to czech Wikipedia page about serie. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullWikiCz() {
		final SerieTO serie = ToGenerator.createSerie();
		serie.setWikiCz(null);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with null path to file with serie's picture. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullPicture() {
		final SerieTO serie = ToGenerator.createSerie();
		serie.setPicture(null);
		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with negative count of seasons. */
	@Test(expected = ValidationException.class)
	public void testAddWithNegativeSeasonsCount() {
		final SerieTO serie = ToGenerator.createSerie();
		serie.setSeasonsCount(-1);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with negative count of episodes. */
	@Test(expected = ValidationException.class)
	public void testAddWithNegativeEpisodesCount() {
		final SerieTO serie = ToGenerator.createSerie();
		serie.setEpisodesCount(-1);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with null total length of seasons. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullTotalLength() {
		final SerieTO serie = ToGenerator.createSerie();
		serie.setTotalLength(null);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with negative total length of seasons. */
	@Test(expected = ValidationException.class)
	public void testAddWithNegativeTotalLength() {
		final SerieTO serie = ToGenerator.createSerie();
		serie.setTotalLength(NEGATIVE_TIME);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with null note. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullNote() {
		final SerieTO serie = ToGenerator.createSerie();
		serie.setNote(null);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with null genres. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullGenres() {
		final SerieTO serie = ToGenerator.createSerie();
		serie.setGenres(null);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with genres with null value. */
	@Test(expected = ValidationException.class)
	public void testAddWithBadGenres() {
		final SerieTO serie = ToGenerator.createSerie();
		serie.setGenres(CollectionUtils.newList(ToGenerator.createGenre(INNER_ID), null));

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with genres with genre with null ID. */
	@Test(expected = ValidationException.class)
	public void testAddWithGenresWithGenreWithNullId() {
		final SerieTO serie = ToGenerator.createSerie();
		serie.setGenres(CollectionUtils.newList(ToGenerator.createGenre(INNER_ID), ToGenerator.createGenre()));

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with genres with genre with null name. */
	@Test(expected = ValidationException.class)
	public void testAddWithGenresWithGenreWithNullName() {
		final SerieTO serie = ToGenerator.createSerie();
		final GenreTO badGenre = ToGenerator.createGenre(SECONDARY_INNER_ID);
		badGenre.setName(null);
		serie.setGenres(CollectionUtils.newList(ToGenerator.createGenre(INNER_ID), badGenre));

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)}. */
	@Test
	public void testUpdate() {
		final SerieTO serie = ToGenerator.createSerie(1);
		serie.setGenres(CollectionUtils.newList(SpringToUtils.getGenre(4)));
		final Serie expectedSerie = EntityGenerator.createSerie(1);
		expectedSerie.setGenres(CollectionUtils.newList(SpringEntitiesUtils.getGenre(4)));

		serieFacade.update(serie);

		final Serie updatedSerie = SpringUtils.getSerie(entityManager, 1);
		DeepAsserts.assertEquals(serie, updatedSerie, "seasonsCount", "episodesCount", "totalLength", "genresAsString");
		DeepAsserts.assertEquals(expectedSerie, updatedSerie);
		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateWithNullArgument() {
		serieFacade.update(null);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with null ID. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithSerieWithNullId() {
		serieFacade.update(ToGenerator.createSerie());
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with null czech name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNullCzechName() {
		final SerieTO serie = ToGenerator.createSerie(PRIMARY_ID);
		serie.setCzechName(null);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with empty string as czech name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithEmptyCzechName() {
		final SerieTO serie = ToGenerator.createSerie(PRIMARY_ID);
		serie.setCzechName("");

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with null original name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNullOriginalName() {
		final SerieTO serie = ToGenerator.createSerie(PRIMARY_ID);
		serie.setOriginalName(null);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with empty string as original name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithEmptyOriginalName() {
		final SerieTO serie = ToGenerator.createSerie(PRIMARY_ID);
		serie.setOriginalName("");

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with null URL to ČSFD page about serie. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNullCsfd() {
		final SerieTO serie = ToGenerator.createSerie(PRIMARY_ID);
		serie.setCsfd(null);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with bad minimal IMDB code. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithBadMinimalImdb() {
		final SerieTO serie = ToGenerator.createSerie(PRIMARY_ID);
		serie.setImdbCode(BAD_MIN_IMDB_CODE);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with bad divider IMDB code. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithBadDividerImdb() {
		final SerieTO serie = ToGenerator.createSerie(PRIMARY_ID);
		serie.setImdbCode(0);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with bad maximal IMDB code. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithBadMaximalImdb() {
		final SerieTO serie = ToGenerator.createSerie(PRIMARY_ID);
		serie.setImdbCode(BAD_MAX_IMDB_CODE);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with null URL to english Wikipedia page about serie. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNullWikiEn() {
		final SerieTO serie = ToGenerator.createSerie(PRIMARY_ID);
		serie.setWikiEn(null);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with null URL to czech Wikipedia page about serie. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNullWikiCz() {
		final SerieTO serie = ToGenerator.createSerie(PRIMARY_ID);
		serie.setWikiCz(null);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with null path to file with serie's picture. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNullPicture() {
		final SerieTO serie = ToGenerator.createSerie(PRIMARY_ID);
		serie.setPicture(null);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with negative count of seasons. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNegativeSeasonsCount() {
		final SerieTO serie = ToGenerator.createSerie(PRIMARY_ID);
		serie.setSeasonsCount(-1);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with negative count of episodes. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNegativeEpisodesCount() {
		final SerieTO serie = ToGenerator.createSerie(PRIMARY_ID);
		serie.setEpisodesCount(-1);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with null total length of seasons. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNullTotalLength() {
		final SerieTO serie = ToGenerator.createSerie(PRIMARY_ID);
		serie.setTotalLength(null);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with negative total length of seasons. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNegativeTotalLength() {
		final SerieTO serie = ToGenerator.createSerie(PRIMARY_ID);
		serie.setTotalLength(NEGATIVE_TIME);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with null note. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNullNote() {
		final SerieTO serie = ToGenerator.createSerie(PRIMARY_ID);
		serie.setNote(null);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with null genres. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNullGenres() {
		final SerieTO serie = ToGenerator.createSerie(PRIMARY_ID);
		serie.setGenres(null);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with genres with null value. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithBadGenres() {
		final SerieTO serie = ToGenerator.createSerie(PRIMARY_ID);
		serie.setGenres(CollectionUtils.newList(ToGenerator.createGenre(INNER_ID), null));

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with genres with genre with null ID. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithGenresWithGenreWithNullId() {
		final SerieTO serie = ToGenerator.createSerie(PRIMARY_ID);
		serie.setGenres(CollectionUtils.newList(ToGenerator.createGenre(INNER_ID), ToGenerator.createGenre()));

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with genres with genre with null name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithGenresWithGenreWithNullName() {
		final SerieTO serie = ToGenerator.createSerie(PRIMARY_ID);
		final GenreTO badGenre = ToGenerator.createGenre(SECONDARY_INNER_ID);
		badGenre.setName(null);
		serie.setGenres(CollectionUtils.newList(ToGenerator.createGenre(INNER_ID), badGenre));

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testUpdateWithSerieWithBadId() {
		serieFacade.update(ToGenerator.createSerie(Integer.MAX_VALUE));
	}

	/** Test method for {@link SerieFacade#remove(SerieTO)}. */
	@Test
	public void testRemove() {
		serieFacade.remove(ToGenerator.createSerie(1));

		assertNull(SpringUtils.getSerie(entityManager, 1));
		DeepAsserts.assertEquals(SERIES_COUNT - 1, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieFacade#remove(SerieTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveWithNullArgument() {
		serieFacade.remove(null);
	}

	/** Test method for {@link SerieFacade#remove(SerieTO)} with serie with null ID. */
	@Test(expected = ValidationException.class)
	public void testRemoveWithSerieWithNullId() {
		serieFacade.remove(ToGenerator.createSerie());
	}

	/** Test method for {@link SerieFacade#remove(SerieTO)} with serie with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testRemoveWithSerieWithBadId() {
		serieFacade.remove(ToGenerator.createSerie(Integer.MAX_VALUE));
	}

	/** Test method for {@link SerieFacade#duplicate(SerieTO)}. */
	@Test
	public void testDuplicate() {
		final Serie serie = SpringEntitiesUtils.getSerie(SERIES_COUNT);
		serie.setId(SERIES_COUNT + 1);

		serieFacade.duplicate(ToGenerator.createSerie(SERIES_COUNT));

		final Serie duplicatedSerie = SpringUtils.getSerie(entityManager, SERIES_COUNT + 1);
		DeepAsserts.assertEquals(serie, duplicatedSerie);
		DeepAsserts.assertEquals(SERIES_COUNT + 1, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieFacade#duplicate(SerieTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testDuplicateWithNullArgument() {
		serieFacade.duplicate(null);
	}

	/** Test method for {@link SerieFacade#duplicate(SerieTO)} with serie with null ID. */
	@Test(expected = ValidationException.class)
	public void testDuplicateWithSerieWithNullId() {
		serieFacade.duplicate(ToGenerator.createSerie());
	}

	/** Test method for {@link SerieFacade#duplicate(SerieTO)} with serie with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testDuplicateWithSerieWithBadId() {
		serieFacade.duplicate(ToGenerator.createSerie(Integer.MAX_VALUE));
	}

	/** Test method for {@link SerieFacade#moveUp(SerieTO)}. */
	@Test
	public void testMoveUp() {
		final Serie serie1 = SpringEntitiesUtils.getSerie(1);
		serie1.setPosition(1);
		final Serie serie2 = SpringEntitiesUtils.getSerie(2);
		serie2.setPosition(0);

		serieFacade.moveUp(ToGenerator.createSerie(2));
		DeepAsserts.assertEquals(serie1, SpringUtils.getSerie(entityManager, 1));
		DeepAsserts.assertEquals(serie2, SpringUtils.getSerie(entityManager, 2));
		for (int i = 3; i <= SERIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getSerie(i), SpringUtils.getSerie(entityManager, i));
		}
		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieFacade#moveUp(SerieTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testMoveUpWithNullArgument() {
		serieFacade.moveUp(null);
	}

	/** Test method for {@link SerieFacade#moveUp(SerieTO)} with serie with null ID. */
	@Test(expected = ValidationException.class)
	public void testMoveUpWithSerieWithNullId() {
		serieFacade.moveUp(ToGenerator.createSerie());
	}

	/** Test method for {@link SerieFacade#moveUp(SerieTO)} with not moveable argument. */
	@Test(expected = ValidationException.class)
	public void testMoveUpWithNotMoveableArgument() {
		serieFacade.moveUp(ToGenerator.createSerie(1));
	}

	/** Test method for {@link SerieFacade#moveUp(SerieTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testMoveUpWithBadId() {
		serieFacade.moveUp(ToGenerator.createSerie(Integer.MAX_VALUE));
	}

	/** Test method for {@link SerieFacade#moveDown(SerieTO)}. */
	@Test
	public void testMoveDown() {
		final SerieTO serie = ToGenerator.createSerie(1);
		final Serie serie1 = SpringEntitiesUtils.getSerie(1);
		serie1.setPosition(1);
		final Serie serie2 = SpringEntitiesUtils.getSerie(2);
		serie2.setPosition(0);

		serieFacade.moveDown(serie);
		DeepAsserts.assertEquals(serie1, SpringUtils.getSerie(entityManager, 1));
		DeepAsserts.assertEquals(serie2, SpringUtils.getSerie(entityManager, 2));
		for (int i = 3; i <= SERIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getSerie(i), SpringUtils.getSerie(entityManager, i));
		}
		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieFacade#moveDown(SerieTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testMoveDownWithNullArgument() {
		serieFacade.moveDown(null);
	}

	/** Test method for {@link SerieFacade#moveDown(SerieTO)} with serie with null ID. */
	@Test(expected = ValidationException.class)
	public void testMoveDownWithSerieWithNullId() {
		serieFacade.moveDown(ToGenerator.createSerie());
	}

	/** Test method for {@link SerieFacade#moveDown(SerieTO)} with not moveable argument. */
	@Test(expected = ValidationException.class)
	public void testMoveDownWithNotMoveableArgument() {
		serieFacade.moveDown(ToGenerator.createSerie(SERIES_COUNT));
	}

	/** Test method for {@link SerieFacade#moveDown(SerieTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testMoveDownWithBadId() {
		serieFacade.moveDown(ToGenerator.createSerie(Integer.MAX_VALUE));
	}

	/** Test method for {@link SerieFacade#exists(SerieTO)} with existing serie. */
	@Test
	public void testExists() {
		for (int i = 1; i <= SERIES_COUNT; i++) {
			assertTrue(serieFacade.exists(ToGenerator.createSerie(i)));
		}

		assertFalse(serieFacade.exists(ToGenerator.createSerie(Integer.MAX_VALUE)));

		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieFacade#exists(SerieTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testExistsWithNullArgument() {
		serieFacade.exists(null);
	}

	/** Test method for {@link SerieFacade#exists(SerieTO)} with serie with null ID. */
	@Test(expected = ValidationException.class)
	public void testExistsWithSerieWithNullId() {
		serieFacade.exists(ToGenerator.createSerie());
	}

	/** Test method for {@link SerieFacade#updatePositions()}. */
	@Test
	public void testUpdatePositions() {
		serieFacade.updatePositions();

		for (int i = 1; i <= SERIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getSerie(i), SpringUtils.getSerie(entityManager, i));
		}
		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieFacade#getTotalLength()}. */
	@Test
	public void testGetTotalLength() {
		DeepAsserts.assertEquals(new Time(1998), serieFacade.getTotalLength());
		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieFacade#getSeasonsCount()}. */
	@Test
	public void testGetSeasonsCount() {
		DeepAsserts.assertEquals(SEASONS_COUNT, serieFacade.getSeasonsCount());
		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieFacade#getEpisodesCount()}. */
	@Test
	public void testGetEpisodesCount() {
		DeepAsserts.assertEquals(EPISODES_COUNT, serieFacade.getEpisodesCount());
		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
	}

}
