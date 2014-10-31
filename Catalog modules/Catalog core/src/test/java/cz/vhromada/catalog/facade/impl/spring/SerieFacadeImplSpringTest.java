package cz.vhromada.catalog.facade.impl.spring;

import static cz.vhromada.catalog.commons.SpringUtils.EPISODES_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.SEASONS_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.SEASONS_PER_SERIE_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.SERIES_COUNT;
import static cz.vhromada.catalog.commons.TestConstants.BAD_MAX_IMDB_CODE;
import static cz.vhromada.catalog.commons.TestConstants.BAD_MIN_IMDB_CODE;
import static cz.vhromada.catalog.commons.TestConstants.INNER_ID;
import static cz.vhromada.catalog.commons.TestConstants.NAME;
import static cz.vhromada.catalog.commons.TestConstants.NEGATIVE_TIME;
import static cz.vhromada.catalog.commons.TestConstants.SECONDARY_INNER_ID;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.EntityGenerator;
import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringToUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.commons.ToGenerator;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.facade.SerieFacade;
import cz.vhromada.catalog.facade.impl.SerieFacadeImpl;
import cz.vhromada.catalog.facade.to.GenreTO;
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
 * A class represents test for class {@link SerieFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeContext.xml")
//TODO vhromada 31.10.2014: implement object generator
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
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(null);
		serie.setGenres(CollectionUtils.newList(SpringToUtils.getGenre(4)));

		serieFacade.add(serie);

		DeepAsserts.assertNotNull(serie.getId());
		DeepAsserts.assertEquals(SERIES_COUNT + 1, serie.getId());
		final Serie addedSerie = SpringUtils.getSerie(entityManager, SERIES_COUNT + 1);
		DeepAsserts.assertEquals(serie, addedSerie, "seasonsCount", "episodesCount", "totalLength", "genresAsString");
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
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(Integer.MAX_VALUE);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with null czech name. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullCzechName() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(null);
		serie.setCzechName(null);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with empty string as czech name. */
	@Test(expected = ValidationException.class)
	public void testAddWithEmptyCzechName() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(null);
		serie.setCzechName("");

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with null original name. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullOriginalName() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(null);
		serie.setOriginalName(null);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with empty string as original name. */
	@Test(expected = ValidationException.class)
	public void testAddWithEmptyOriginalName() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(null);
		serie.setOriginalName("");

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with null URL to ČSFD page about serie. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullCsfd() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(null);
		serie.setCsfd(null);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with bad minimal IMDB code. */
	@Test(expected = ValidationException.class)
	public void testAddWithBadMinimalImdb() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(null);
		serie.setImdbCode(BAD_MIN_IMDB_CODE);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with bad divider IMDB code. */
	@Test(expected = ValidationException.class)
	public void testAddWithBadDividerImdb() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(null);
		serie.setImdbCode(0);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with bad maximal IMDB code. */
	@Test(expected = ValidationException.class)
	public void testAddWithBadMaximalImdb() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(null);
		serie.setImdbCode(BAD_MAX_IMDB_CODE);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with null URL to english Wikipedia page about serie. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullWikiEn() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(null);
		serie.setWikiEn(null);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with null URL to czech Wikipedia page about serie. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullWikiCz() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(null);
		serie.setWikiCz(null);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with null path to file with serie's picture. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullPicture() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(null);
		serie.setPicture(null);
		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with negative count of seasons. */
	@Test(expected = ValidationException.class)
	public void testAddWithNegativeSeasonsCount() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(null);
		serie.setSeasonsCount(-1);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with negative count of episodes. */
	@Test(expected = ValidationException.class)
	public void testAddWithNegativeEpisodesCount() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(null);
		serie.setEpisodesCount(-1);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with null total length of seasons. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullTotalLength() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(null);
		serie.setTotalLength(null);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with negative total length of seasons. */
	@Test(expected = ValidationException.class)
	public void testAddWithNegativeTotalLength() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(null);
		serie.setTotalLength(NEGATIVE_TIME);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with null note. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullNote() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(null);
		serie.setNote(null);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with null genres. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullGenres() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(null);
		serie.setGenres(null);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with genres with null value. */
	@Test(expected = ValidationException.class)
	public void testAddWithBadGenres() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(null);
		serie.setGenres(CollectionUtils.newList(createGenre(INNER_ID), null));

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with genres with genre with null ID. */
	@Test(expected = ValidationException.class)
	public void testAddWithGenresWithGenreWithNullId() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(null);
		serie.setGenres(CollectionUtils.newList(createGenre(INNER_ID), createGenre()));

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with genres with genre with null name. */
	@Test(expected = ValidationException.class)
	public void testAddWithGenresWithGenreWithNullName() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(null);
		final GenreTO badGenre = createGenre(SECONDARY_INNER_ID);
		badGenre.setName(null);
		serie.setGenres(CollectionUtils.newList(createGenre(INNER_ID), badGenre));

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
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(null);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with null czech name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNullCzechName() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setCzechName(null);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with empty string as czech name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithEmptyCzechName() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setCzechName("");

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with null original name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNullOriginalName() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setOriginalName(null);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with empty string as original name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithEmptyOriginalName() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setOriginalName("");

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with null URL to ČSFD page about serie. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNullCsfd() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setCsfd(null);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with bad minimal IMDB code. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithBadMinimalImdb() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setImdbCode(BAD_MIN_IMDB_CODE);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with bad divider IMDB code. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithBadDividerImdb() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setImdbCode(0);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with bad maximal IMDB code. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithBadMaximalImdb() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setImdbCode(BAD_MAX_IMDB_CODE);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with null URL to english Wikipedia page about serie. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNullWikiEn() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setWikiEn(null);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with null URL to czech Wikipedia page about serie. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNullWikiCz() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setWikiCz(null);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with null path to file with serie's picture. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNullPicture() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setPicture(null);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with negative count of seasons. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNegativeSeasonsCount() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setSeasonsCount(-1);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with negative count of episodes. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNegativeEpisodesCount() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setEpisodesCount(-1);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with null total length of seasons. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNullTotalLength() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setTotalLength(null);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with negative total length of seasons. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNegativeTotalLength() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setTotalLength(NEGATIVE_TIME);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with null note. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNullNote() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setNote(null);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with null genres. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithNullGenres() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setGenres(null);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with genres with null value. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithBadGenres() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setGenres(CollectionUtils.newList(createGenre(INNER_ID), null));

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with genres with genre with null ID. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithGenresWithGenreWithNullId() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setGenres(CollectionUtils.newList(createGenre(INNER_ID), createGenre()));

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with genres with genre with null name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithGenresWithGenreWithNullName() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		final GenreTO badGenre = createGenre(SECONDARY_INNER_ID);
		badGenre.setName(null);
		serie.setGenres(CollectionUtils.newList(createGenre(INNER_ID), badGenre));

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testUpdateWithSerieWithBadId() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(Integer.MAX_VALUE);

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#remove(SerieTO)}. */
	@Test
	public void testRemove() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(1);

		serieFacade.remove(serie);

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
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(null);

		serieFacade.remove(serie);
	}

	/** Test method for {@link SerieFacade#remove(SerieTO)} with serie with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testRemoveWithSerieWithBadId() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(Integer.MAX_VALUE);

		serieFacade.remove(serie);
	}

	/** Test method for {@link SerieFacade#duplicate(SerieTO)}. */
	@Test
	public void testDuplicate() {
		final SerieTO serieTO = objectGenerator.generate(SerieTO.class);
		serieTO.setId(SERIES_COUNT);
		final Serie serie = SpringEntitiesUtils.getSerie(SERIES_COUNT);
		serie.setId(SERIES_COUNT + 1);

		serieFacade.duplicate(serieTO);

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
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(null);

		serieFacade.duplicate(serie);
	}

	/** Test method for {@link SerieFacade#duplicate(SerieTO)} with serie with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testDuplicateWithSerieWithBadId() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(Integer.MAX_VALUE);

		serieFacade.duplicate(serie);
	}

	/** Test method for {@link SerieFacade#moveUp(SerieTO)}. */
	@Test
	public void testMoveUp() {
		final SerieTO serieTO = objectGenerator.generate(SerieTO.class);
		serieTO.setId(2);
		final Serie serie1 = SpringEntitiesUtils.getSerie(1);
		serie1.setPosition(1);
		final Serie serie2 = SpringEntitiesUtils.getSerie(2);
		serie2.setPosition(0);

		serieFacade.moveUp(serieTO);
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
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(null);

		serieFacade.moveUp(serie);
	}

	/** Test method for {@link SerieFacade#moveUp(SerieTO)} with not moveable argument. */
	@Test(expected = ValidationException.class)
	public void testMoveUpWithNotMoveableArgument() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(1);

		serieFacade.moveUp(serie);
	}

	/** Test method for {@link SerieFacade#moveUp(SerieTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testMoveUpWithBadId() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(Integer.MAX_VALUE);

		serieFacade.moveUp(serie);
	}

	/** Test method for {@link SerieFacade#moveDown(SerieTO)}. */
	@Test
	public void testMoveDown() {
		final SerieTO serieTO = objectGenerator.generate(SerieTO.class);
		serieTO.setId(1);
		final Serie serie1 = SpringEntitiesUtils.getSerie(1);
		serie1.setPosition(1);
		final Serie serie2 = SpringEntitiesUtils.getSerie(2);
		serie2.setPosition(0);

		serieFacade.moveDown(serieTO);
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
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(null);

		serieFacade.moveDown(serie);
	}

	/** Test method for {@link SerieFacade#moveDown(SerieTO)} with not moveable argument. */
	@Test(expected = ValidationException.class)
	public void testMoveDownWithNotMoveableArgument() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(SERIES_COUNT);

		serieFacade.moveDown(serie);
	}

	/** Test method for {@link SerieFacade#moveDown(SerieTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testMoveDownWithBadId() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(Integer.MAX_VALUE);

		serieFacade.moveDown(serie);
	}

	/** Test method for {@link SerieFacade#exists(SerieTO)} with existing serie. */
	@Test
	public void testExists() {
		for (int i = 1; i <= SERIES_COUNT; i++) {
			final SerieTO serie = objectGenerator.generate(SerieTO.class);
			serie.setId(i);
			assertTrue(serieFacade.exists(serie));
		}

		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(Integer.MAX_VALUE);
		assertFalse(serieFacade.exists(serie));

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
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(null);

		serieFacade.exists(serie);
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

	/**
	 * Returns new TO for genre.
	 *
	 * @return new TO for genre
	 */
	@Deprecated
	private static GenreTO createGenre() {
		final GenreTO genre = new GenreTO();
		genre.setName(NAME);
		return genre;
	}

	/**
	 * Returns new TO for genre with specified ID.
	 *
	 * @param id ID
	 * @return new TO for genre with specified ID
	 */
	@Deprecated
	private static GenreTO createGenre(final Integer id) {
		final GenreTO genre = createGenre();
		genre.setId(id);
		return genre;
	}

}
