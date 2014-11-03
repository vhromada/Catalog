package cz.vhromada.catalog.facade.impl.spring;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringToUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.commons.TestConstants;
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.facade.SerieFacade;
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
 * A class represents test for class {@link cz.vhromada.catalog.facade.impl.SerieFacadeImpl}.
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
		for (int i = 1; i <= SpringUtils.SERIES_COUNT; i++) {
			for (Season season : SpringEntitiesUtils.getSeasons(i)) {
				season.setId(null);
				SpringUtils.persist(transactionManager, entityManager, season);
			}
		}
		for (int i = 1; i <= SpringUtils.SERIES_COUNT; i++) {
			for (int j = 1; j <= SpringUtils.SEASONS_PER_SERIE_COUNT; j++) {
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
		DeepAsserts.assertEquals(SpringUtils.SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieFacade#getSerie(Integer)}. */
	@Test
	public void testGetSerie() {
		for (int i = 1; i <= SpringUtils.SERIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringToUtils.getSerie(i), serieFacade.getSerie(i));
		}

		assertNull(serieFacade.getSerie(Integer.MAX_VALUE));

		DeepAsserts.assertEquals(SpringUtils.SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieFacade#getSerie(Integer)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testGetSerieWithNullArgument() {
		serieFacade.getSerie(null);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)}. */
	@Test
	public void testAdd() {
		final SerieTO serie = SpringToUtils.newSerie(objectGenerator);

		serieFacade.add(serie);

		DeepAsserts.assertNotNull(serie.getId());
		DeepAsserts.assertEquals(SpringUtils.SERIES_COUNT + 1, serie.getId());
		final Serie addedSerie = SpringUtils.getSerie(entityManager, SpringUtils.SERIES_COUNT + 1);
		DeepAsserts.assertEquals(serie, addedSerie, "seasonsCount", "episodesCount", "totalLength", "genresAsString");
		DeepAsserts.assertEquals(SpringUtils.SERIES_COUNT + 1, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testAddWithNullArgument() {
		serieFacade.add(null);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with not null ID. */
	@Test(expected = ValidationException.class)
	public void testAddWithSerieWithNotNullId() {
		serieFacade.add(SpringToUtils.newSerieWithId(objectGenerator));
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with null czech name. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullCzechName() {
		final SerieTO serie = SpringToUtils.newSerie(objectGenerator);
		serie.setCzechName(null);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with empty string as czech name. */
	@Test(expected = ValidationException.class)
	public void testAddWithEmptyCzechName() {
		final SerieTO serie = SpringToUtils.newSerie(objectGenerator);
		serie.setCzechName("");

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with null original name. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullOriginalName() {
		final SerieTO serie = SpringToUtils.newSerie(objectGenerator);
		serie.setOriginalName(null);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with empty string as original name. */
	@Test(expected = ValidationException.class)
	public void testAddWithEmptyOriginalName() {
		final SerieTO serie = SpringToUtils.newSerie(objectGenerator);
		serie.setOriginalName("");

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with null URL to ČSFD page about serie. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullCsfd() {
		final SerieTO serie = SpringToUtils.newSerie(objectGenerator);
		serie.setCsfd(null);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with bad minimal IMDB code. */
	@Test(expected = ValidationException.class)
	public void testAddWithBadMinimalImdb() {
		final SerieTO serie = SpringToUtils.newSerie(objectGenerator);
		serie.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with bad divider IMDB code. */
	@Test(expected = ValidationException.class)
	public void testAddWithBadDividerImdb() {
		final SerieTO serie = SpringToUtils.newSerie(objectGenerator);
		serie.setImdbCode(0);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with bad maximal IMDB code. */
	@Test(expected = ValidationException.class)
	public void testAddWithBadMaximalImdb() {
		final SerieTO serie = SpringToUtils.newSerie(objectGenerator);
		serie.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with null URL to english Wikipedia page about serie. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullWikiEn() {
		final SerieTO serie = SpringToUtils.newSerie(objectGenerator);
		serie.setWikiEn(null);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with null URL to czech Wikipedia page about serie. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullWikiCz() {
		final SerieTO serie = SpringToUtils.newSerie(objectGenerator);
		serie.setWikiCz(null);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with null path to file with serie's picture. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullPicture() {
		final SerieTO serie = SpringToUtils.newSerie(objectGenerator);
		serie.setPicture(null);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with negative count of seasons. */
	@Test(expected = ValidationException.class)
	public void testAddWithNegativeSeasonsCount() {
		final SerieTO serie = SpringToUtils.newSerie(objectGenerator);
		serie.setSeasonsCount(-1);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with negative count of episodes. */
	@Test(expected = ValidationException.class)
	public void testAddWithNegativeEpisodesCount() {
		final SerieTO serie = SpringToUtils.newSerie(objectGenerator);
		serie.setEpisodesCount(-1);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with null total length of seasons. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullTotalLength() {
		final SerieTO serie = SpringToUtils.newSerie(objectGenerator);
		serie.setTotalLength(null);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with negative total length of seasons. */
	@Test(expected = ValidationException.class)
	public void testAddWithNegativeTotalLength() {
		final SerieTO serie = SpringToUtils.newSerie(objectGenerator);
		serie.setTotalLength(TestConstants.NEGATIVE_TIME);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with null note. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullNote() {
		final SerieTO serie = SpringToUtils.newSerie(objectGenerator);
		serie.setNote(null);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with null genres. */
	@Test(expected = ValidationException.class)
	public void testAddWithNullGenres() {
		final SerieTO serie = SpringToUtils.newSerie(objectGenerator);
		serie.setGenres(null);

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with genres with null value. */
	@Test(expected = ValidationException.class)
	public void testAddWithBadGenres() {
		final SerieTO serie = SpringToUtils.newSerie(objectGenerator);
		serie.setGenres(CollectionUtils.newList(SpringToUtils.newGenreWithId(objectGenerator), null));

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with genres with genre with null ID. */
	@Test(expected = ValidationException.class)
	public void testAddWithGenresWithGenreWithNullId() {
		final SerieTO serie = SpringToUtils.newSerie(objectGenerator);
		serie.setGenres(CollectionUtils.newList(SpringToUtils.newGenreWithId(objectGenerator), SpringToUtils.newGenre(objectGenerator)));

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with serie with genres with genre with null name. */
	@Test(expected = ValidationException.class)
	public void testAddWithGenresWithGenreWithNullName() {
		final SerieTO serie = SpringToUtils.newSerie(objectGenerator);
		final GenreTO badGenre = SpringToUtils.newGenreWithId(objectGenerator);
		badGenre.setName(null);
		serie.setGenres(CollectionUtils.newList(SpringToUtils.newGenreWithId(objectGenerator), badGenre));

		serieFacade.add(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)}. */
	@Test
	public void testUpdate() {
		final SerieTO serie = SpringToUtils.newSerie(objectGenerator, 1);
		serie.setGenres(CollectionUtils.newList(SpringToUtils.getGenre(4)));

		serieFacade.update(serie);

		final Serie updatedSerie = SpringUtils.getSerie(entityManager, 1);
		DeepAsserts.assertEquals(serie, updatedSerie, "seasonsCount", "episodesCount", "totalLength", "genresAsString");
		DeepAsserts.assertEquals(SpringUtils.SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateWithNullArgument() {
		serieFacade.update(null);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with null ID. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithSerieWithNullId() {
		serieFacade.update(SpringToUtils.newSerie(objectGenerator));
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
		serie.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

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
		serie.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

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
		serie.setTotalLength(TestConstants.NEGATIVE_TIME);

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
		serie.setGenres(CollectionUtils.newList(SpringToUtils.newGenreWithId(objectGenerator), null));

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with genres with genre with null ID. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithGenresWithGenreWithNullId() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setGenres(CollectionUtils.newList(SpringToUtils.newGenreWithId(objectGenerator), SpringToUtils.newGenre(objectGenerator)));

		serieFacade.update(serie);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with serie with genres with genre with null name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithGenresWithGenreWithNullName() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		final GenreTO badGenre = SpringToUtils.newGenreWithId(objectGenerator);
		badGenre.setName(null);
		serie.setGenres(CollectionUtils.newList(SpringToUtils.newGenreWithId(objectGenerator), badGenre));

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
		serieFacade.remove(SpringToUtils.newSerie(objectGenerator, 1));

		assertNull(SpringUtils.getSerie(entityManager, 1));
		DeepAsserts.assertEquals(SpringUtils.SERIES_COUNT - 1, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieFacade#remove(SerieTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveWithNullArgument() {
		serieFacade.remove(null);
	}

	/** Test method for {@link SerieFacade#remove(SerieTO)} with serie with null ID. */
	@Test(expected = ValidationException.class)
	public void testRemoveWithSerieWithNullId() {
		serieFacade.remove(SpringToUtils.newSerie(objectGenerator));
	}

	/** Test method for {@link SerieFacade#remove(SerieTO)} with serie with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testRemoveWithSerieWithBadId() {
		serieFacade.remove(SpringToUtils.newSerie(objectGenerator, Integer.MAX_VALUE));
	}

	/** Test method for {@link SerieFacade#duplicate(SerieTO)}. */
	@Test
	public void testDuplicate() {
		final Serie serie = SpringEntitiesUtils.getSerie(SpringUtils.SERIES_COUNT);
		serie.setId(SpringUtils.SERIES_COUNT + 1);

		serieFacade.duplicate(SpringToUtils.newSerie(objectGenerator, SpringUtils.SERIES_COUNT));

		final Serie duplicatedSerie = SpringUtils.getSerie(entityManager, SpringUtils.SERIES_COUNT + 1);
		DeepAsserts.assertEquals(serie, duplicatedSerie);
		DeepAsserts.assertEquals(SpringUtils.SERIES_COUNT + 1, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieFacade#duplicate(SerieTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testDuplicateWithNullArgument() {
		serieFacade.duplicate(null);
	}

	/** Test method for {@link SerieFacade#duplicate(SerieTO)} with serie with null ID. */
	@Test(expected = ValidationException.class)
	public void testDuplicateWithSerieWithNullId() {
		serieFacade.duplicate(SpringToUtils.newSerie(objectGenerator));
	}

	/** Test method for {@link SerieFacade#duplicate(SerieTO)} with serie with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testDuplicateWithSerieWithBadId() {
		serieFacade.duplicate(SpringToUtils.newSerie(objectGenerator, Integer.MAX_VALUE));
	}

	/** Test method for {@link SerieFacade#moveUp(SerieTO)}. */
	@Test
	public void testMoveUp() {
		final Serie serie1 = SpringEntitiesUtils.getSerie(1);
		serie1.setPosition(1);
		final Serie serie2 = SpringEntitiesUtils.getSerie(2);
		serie2.setPosition(0);

		serieFacade.moveUp(SpringToUtils.newSerie(objectGenerator, 2));
		DeepAsserts.assertEquals(serie1, SpringUtils.getSerie(entityManager, 1));
		DeepAsserts.assertEquals(serie2, SpringUtils.getSerie(entityManager, 2));
		for (int i = 3; i <= SpringUtils.SERIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getSerie(i), SpringUtils.getSerie(entityManager, i));
		}
		DeepAsserts.assertEquals(SpringUtils.SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieFacade#moveUp(SerieTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testMoveUpWithNullArgument() {
		serieFacade.moveUp(null);
	}

	/** Test method for {@link SerieFacade#moveUp(SerieTO)} with serie with null ID. */
	@Test(expected = ValidationException.class)
	public void testMoveUpWithSerieWithNullId() {
		serieFacade.moveUp(SpringToUtils.newSerie(objectGenerator));
	}

	/** Test method for {@link SerieFacade#moveUp(SerieTO)} with not moveable argument. */
	@Test(expected = ValidationException.class)
	public void testMoveUpWithNotMoveableArgument() {
		serieFacade.moveUp(SpringToUtils.newSerie(objectGenerator, 1));
	}

	/** Test method for {@link SerieFacade#moveUp(SerieTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testMoveUpWithBadId() {
		serieFacade.moveUp(SpringToUtils.newSerie(objectGenerator, Integer.MAX_VALUE));
	}

	/** Test method for {@link SerieFacade#moveDown(SerieTO)}. */
	@Test
	public void testMoveDown() {
		final Serie serie1 = SpringEntitiesUtils.getSerie(1);
		serie1.setPosition(1);
		final Serie serie2 = SpringEntitiesUtils.getSerie(2);
		serie2.setPosition(0);

		serieFacade.moveDown(SpringToUtils.newSerie(objectGenerator, 1));
		DeepAsserts.assertEquals(serie1, SpringUtils.getSerie(entityManager, 1));
		DeepAsserts.assertEquals(serie2, SpringUtils.getSerie(entityManager, 2));
		for (int i = 3; i <= SpringUtils.SERIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getSerie(i), SpringUtils.getSerie(entityManager, i));
		}
		DeepAsserts.assertEquals(SpringUtils.SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieFacade#moveDown(SerieTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testMoveDownWithNullArgument() {
		serieFacade.moveDown(null);
	}

	/** Test method for {@link SerieFacade#moveDown(SerieTO)} with serie with null ID. */
	@Test(expected = ValidationException.class)
	public void testMoveDownWithSerieWithNullId() {
		serieFacade.moveDown(SpringToUtils.newSerie(objectGenerator));
	}

	/** Test method for {@link SerieFacade#moveDown(SerieTO)} with not moveable argument. */
	@Test(expected = ValidationException.class)
	public void testMoveDownWithNotMoveableArgument() {
		final SerieTO serie = objectGenerator.generate(SerieTO.class);
		serie.setId(SpringUtils.SERIES_COUNT);

		serieFacade.moveDown(serie);
	}

	/** Test method for {@link SerieFacade#moveDown(SerieTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testMoveDownWithBadId() {
		serieFacade.moveDown(SpringToUtils.newSerie(objectGenerator, Integer.MAX_VALUE));
	}

	/** Test method for {@link SerieFacade#exists(SerieTO)} with existing serie. */
	@Test
	public void testExists() {
		for (int i = 1; i <= SpringUtils.SERIES_COUNT; i++) {
			assertTrue(serieFacade.exists(SpringToUtils.newSerie(objectGenerator, i)));
		}

		assertFalse(serieFacade.exists(SpringToUtils.newSerie(objectGenerator, Integer.MAX_VALUE)));

		DeepAsserts.assertEquals(SpringUtils.SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieFacade#exists(SerieTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testExistsWithNullArgument() {
		serieFacade.exists(null);
	}

	/** Test method for {@link SerieFacade#exists(SerieTO)} with serie with null ID. */
	@Test(expected = ValidationException.class)
	public void testExistsWithSerieWithNullId() {
		serieFacade.exists(SpringToUtils.newSerie(objectGenerator));
	}

	/** Test method for {@link SerieFacade#updatePositions()}. */
	@Test
	public void testUpdatePositions() {
		serieFacade.updatePositions();

		for (int i = 1; i <= SpringUtils.SERIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getSerie(i), SpringUtils.getSerie(entityManager, i));
		}
		DeepAsserts.assertEquals(SpringUtils.SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieFacade#getTotalLength()}. */
	@Test
	public void testGetTotalLength() {
		final Time length = new Time(1998);

		DeepAsserts.assertEquals(length, serieFacade.getTotalLength());
		DeepAsserts.assertEquals(SpringUtils.SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieFacade#getSeasonsCount()}. */
	@Test
	public void testGetSeasonsCount() {
		DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT, serieFacade.getSeasonsCount());
		DeepAsserts.assertEquals(SpringUtils.SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
	}

	/** Test method for {@link SerieFacade#getEpisodesCount()}. */
	@Test
	public void testGetEpisodesCount() {
		DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT, serieFacade.getEpisodesCount());
		DeepAsserts.assertEquals(SpringUtils.SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
	}

}
