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
import cz.vhromada.catalog.dao.entities.Show;
import cz.vhromada.catalog.facade.ShowFacade;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.ShowTO;
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
 * A class represents test for class {@link cz.vhromada.catalog.facade.impl.ShowFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeContext.xml")
public class ShowFacadeImplSpringTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * Instance of {@link PlatformTransactionManager}
     */
    @Autowired
    private PlatformTransactionManager transactionManager;

    /**
     * Instance of {@link ShowFacade}
     */
    @Autowired
    private ShowFacade showFacade;

    /**
     * Instance of {@link ObjectGenerator}
     */
    @Autowired
    private ObjectGenerator objectGenerator;

    /**
     * Initializes database.
     */
    @Before
    public void setUp() {
        SpringUtils.remove(transactionManager, entityManager, Episode.class);
        SpringUtils.remove(transactionManager, entityManager, Season.class);
        SpringUtils.remove(transactionManager, entityManager, Show.class);
        SpringUtils.updateSequence(transactionManager, entityManager, "tv_shows_sq");
        SpringUtils.updateSequence(transactionManager, entityManager, "seasons_sq");
        SpringUtils.updateSequence(transactionManager, entityManager, "episodes_sq");
        for (final Show show : SpringEntitiesUtils.getShows()) {
            show.setId(null);
            SpringUtils.persist(transactionManager, entityManager, show);
        }
        for (int i = 1; i <= SpringUtils.SHOWS_COUNT; i++) {
            for (final Season season : SpringEntitiesUtils.getSeasons(i)) {
                season.setId(null);
                SpringUtils.persist(transactionManager, entityManager, season);
            }
        }
        for (int i = 1; i <= SpringUtils.SHOWS_COUNT; i++) {
            for (int j = 1; j <= SpringUtils.SEASONS_PER_SHOW_COUNT; j++) {
                for (final Episode episode : SpringEntitiesUtils.getEpisodes(i, j)) {
                    episode.setId(null);
                    SpringUtils.persist(transactionManager, entityManager, episode);
                }
            }
        }
    }

    /**
     * Test method for {@link ShowFacade#newData()}.
     */
    @Test
    public void testNewData() {
        showFacade.newData();

        DeepAsserts.assertEquals(0, SpringUtils.getShowsCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#getShows()}.
     */
    @Test
    public void testGetShows() {
        DeepAsserts.assertEquals(SpringToUtils.getShows(), showFacade.getShows());
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#getShow(Integer)}.
     */
    @Test
    public void testGetShow() {
        for (int i = 1; i <= SpringUtils.SHOWS_COUNT; i++) {
            DeepAsserts.assertEquals(SpringToUtils.getShow(i), showFacade.getShow(i));
        }

        assertNull(showFacade.getShow(Integer.MAX_VALUE));

        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#getShow(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetShowWithNullArgument() {
        showFacade.getShow(null);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)}.
     */
    @Test
    public void testAdd() {
        final ShowTO show = SpringToUtils.newShow(objectGenerator);

        showFacade.add(show);

        DeepAsserts.assertNotNull(show.getId());
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT + 1, show.getId());
        final Show addedShow = SpringUtils.getShow(entityManager, SpringUtils.SHOWS_COUNT + 1);
        DeepAsserts.assertEquals(show, addedShow, "seasonsCount", "episodesCount", "totalLength", "genresAsString");
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT + 1, SpringUtils.getShowsCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddWithNullArgument() {
        showFacade.add(null);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithShowWithNotNullId() {
        showFacade.add(SpringToUtils.newShowWithId(objectGenerator));
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with null czech name.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithNullCzechName() {
        final ShowTO show = SpringToUtils.newShow(objectGenerator);
        show.setCzechName(null);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with empty string as czech name.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithEmptyCzechName() {
        final ShowTO show = SpringToUtils.newShow(objectGenerator);
        show.setCzechName("");

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with null original name.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithNullOriginalName() {
        final ShowTO show = SpringToUtils.newShow(objectGenerator);
        show.setOriginalName(null);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with empty string as original name.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithEmptyOriginalName() {
        final ShowTO show = SpringToUtils.newShow(objectGenerator);
        show.setOriginalName("");

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with null URL to ČSFD page about show.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithNullCsfd() {
        final ShowTO show = SpringToUtils.newShow(objectGenerator);
        show.setCsfd(null);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with bad minimal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithBadMinimalImdb() {
        final ShowTO show = SpringToUtils.newShow(objectGenerator);
        show.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with bad divider IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithBadDividerImdb() {
        final ShowTO show = SpringToUtils.newShow(objectGenerator);
        show.setImdbCode(0);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with bad maximal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithBadMaximalImdb() {
        final ShowTO show = SpringToUtils.newShow(objectGenerator);
        show.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with null URL to english Wikipedia page about show.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithNullWikiEn() {
        final ShowTO show = SpringToUtils.newShow(objectGenerator);
        show.setWikiEn(null);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with null URL to czech Wikipedia page about show.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithNullWikiCz() {
        final ShowTO show = SpringToUtils.newShow(objectGenerator);
        show.setWikiCz(null);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with null path to file with show picture.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithNullPicture() {
        final ShowTO show = SpringToUtils.newShow(objectGenerator);
        show.setPicture(null);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with null note.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithNullNote() {
        final ShowTO show = SpringToUtils.newShow(objectGenerator);
        show.setNote(null);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with null genres.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithNullGenres() {
        final ShowTO show = SpringToUtils.newShow(objectGenerator);
        show.setGenres(null);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with genres with null value.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithBadGenres() {
        final ShowTO show = SpringToUtils.newShow(objectGenerator);
        show.setGenres(CollectionUtils.newList(SpringToUtils.newGenreWithId(objectGenerator), null));

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with genres with genre with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithGenresWithGenreWithNullId() {
        final ShowTO show = SpringToUtils.newShow(objectGenerator);
        show.setGenres(CollectionUtils.newList(SpringToUtils.newGenreWithId(objectGenerator), SpringToUtils.newGenre(objectGenerator)));

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with genres with genre with null name.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithGenresWithGenreWithNullName() {
        final ShowTO show = SpringToUtils.newShow(objectGenerator);
        final GenreTO badGenre = SpringToUtils.newGenreWithId(objectGenerator);
        badGenre.setName(null);
        show.setGenres(CollectionUtils.newList(SpringToUtils.newGenreWithId(objectGenerator), badGenre));

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)}.
     */
    @Test
    public void testUpdate() {
        final ShowTO show = SpringToUtils.newShow(objectGenerator, 1);
        show.setGenres(CollectionUtils.newList(SpringToUtils.getGenre(4)));

        showFacade.update(show);

        final Show updatedShow = SpringUtils.getShow(entityManager, 1);
        DeepAsserts.assertEquals(show, updatedShow, "seasonsCount", "episodesCount", "totalLength", "genresAsString");
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithNullArgument() {
        showFacade.update(null);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithShowWithNullId() {
        showFacade.update(SpringToUtils.newShow(objectGenerator));
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with null czech name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithNullCzechName() {
        final ShowTO show = objectGenerator.generate(ShowTO.class);
        show.setCzechName(null);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with empty string as czech name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithEmptyCzechName() {
        final ShowTO show = objectGenerator.generate(ShowTO.class);
        show.setCzechName("");

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with null original name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithNullOriginalName() {
        final ShowTO show = objectGenerator.generate(ShowTO.class);
        show.setOriginalName(null);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with empty string as original name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithEmptyOriginalName() {
        final ShowTO show = objectGenerator.generate(ShowTO.class);
        show.setOriginalName("");

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with null URL to ČSFD page about show.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithNullCsfd() {
        final ShowTO show = objectGenerator.generate(ShowTO.class);
        show.setCsfd(null);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with bad minimal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBadMinimalImdb() {
        final ShowTO show = objectGenerator.generate(ShowTO.class);
        show.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with bad divider IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBadDividerImdb() {
        final ShowTO show = objectGenerator.generate(ShowTO.class);
        show.setImdbCode(0);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with bad maximal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBadMaximalImdb() {
        final ShowTO show = objectGenerator.generate(ShowTO.class);
        show.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with null URL to english Wikipedia page about show.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithNullWikiEn() {
        final ShowTO show = objectGenerator.generate(ShowTO.class);
        show.setWikiEn(null);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with null URL to czech Wikipedia page about show.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithNullWikiCz() {
        final ShowTO show = objectGenerator.generate(ShowTO.class);
        show.setWikiCz(null);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with null path to file with show picture.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithNullPicture() {
        final ShowTO show = objectGenerator.generate(ShowTO.class);
        show.setPicture(null);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with null note.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithNullNote() {
        final ShowTO show = objectGenerator.generate(ShowTO.class);
        show.setNote(null);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with null genres.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithNullGenres() {
        final ShowTO show = objectGenerator.generate(ShowTO.class);
        show.setGenres(null);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with genres with null value.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBadGenres() {
        final ShowTO show = objectGenerator.generate(ShowTO.class);
        show.setGenres(CollectionUtils.newList(SpringToUtils.newGenreWithId(objectGenerator), null));

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with genres with genre with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithGenresWithGenreWithNullId() {
        final ShowTO show = objectGenerator.generate(ShowTO.class);
        show.setGenres(CollectionUtils.newList(SpringToUtils.newGenreWithId(objectGenerator), SpringToUtils.newGenre(objectGenerator)));

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with genres with genre with null name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithGenresWithGenreWithNullName() {
        final ShowTO show = objectGenerator.generate(ShowTO.class);
        final GenreTO badGenre = SpringToUtils.newGenreWithId(objectGenerator);
        badGenre.setName(null);
        show.setGenres(CollectionUtils.newList(SpringToUtils.newGenreWithId(objectGenerator), badGenre));

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdateWithShowWithBadId() {
        final ShowTO show = objectGenerator.generate(ShowTO.class);
        show.setId(Integer.MAX_VALUE);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#remove(ShowTO)}.
     */
    @Test
    public void testRemove() {
        showFacade.remove(SpringToUtils.newShow(objectGenerator, 1));

        assertNull(SpringUtils.getShow(entityManager, 1));
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT - 1, SpringUtils.getShowsCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#remove(ShowTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveWithNullArgument() {
        showFacade.remove(null);
    }

    /**
     * Test method for {@link ShowFacade#remove(ShowTO)} with show with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testRemoveWithShowWithNullId() {
        showFacade.remove(SpringToUtils.newShow(objectGenerator));
    }

    /**
     * Test method for {@link ShowFacade#remove(ShowTO)} with show with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testRemoveWithShowWithBadId() {
        showFacade.remove(SpringToUtils.newShow(objectGenerator, Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#duplicate(ShowTO)}.
     */
    @Test
    public void testDuplicate() {
        final Show show = SpringEntitiesUtils.getShow(SpringUtils.SHOWS_COUNT);
        show.setId(SpringUtils.SHOWS_COUNT + 1);

        showFacade.duplicate(SpringToUtils.newShow(objectGenerator, SpringUtils.SHOWS_COUNT));

        final Show duplicatedShow = SpringUtils.getShow(entityManager, SpringUtils.SHOWS_COUNT + 1);
        DeepAsserts.assertEquals(show, duplicatedShow);
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT + 1, SpringUtils.getShowsCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#duplicate(ShowTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateWithNullArgument() {
        showFacade.duplicate(null);
    }

    /**
     * Test method for {@link ShowFacade#duplicate(ShowTO)} with show with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testDuplicateWithShowWithNullId() {
        showFacade.duplicate(SpringToUtils.newShow(objectGenerator));
    }

    /**
     * Test method for {@link ShowFacade#duplicate(ShowTO)} with show with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testDuplicateWithShowWithBadId() {
        showFacade.duplicate(SpringToUtils.newShow(objectGenerator, Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#moveUp(ShowTO)}.
     */
    @Test
    public void testMoveUp() {
        final Show show1 = SpringEntitiesUtils.getShow(1);
        show1.setPosition(1);
        final Show show2 = SpringEntitiesUtils.getShow(2);
        show2.setPosition(0);

        showFacade.moveUp(SpringToUtils.newShow(objectGenerator, 2));
        DeepAsserts.assertEquals(show1, SpringUtils.getShow(entityManager, 1));
        DeepAsserts.assertEquals(show2, SpringUtils.getShow(entityManager, 2));
        for (int i = 3; i <= SpringUtils.SHOWS_COUNT; i++) {
            DeepAsserts.assertEquals(SpringEntitiesUtils.getShow(i), SpringUtils.getShow(entityManager, i));
        }
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#moveUp(ShowTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUpWithNullArgument() {
        showFacade.moveUp(null);
    }

    /**
     * Test method for {@link ShowFacade#moveUp(ShowTO)} with show with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUpWithShowWithNullId() {
        showFacade.moveUp(SpringToUtils.newShow(objectGenerator));
    }

    /**
     * Test method for {@link ShowFacade#moveUp(ShowTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUpWithNotMovableArgument() {
        showFacade.moveUp(SpringToUtils.newShow(objectGenerator, 1));
    }

    /**
     * Test method for {@link ShowFacade#moveUp(ShowTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveUpWithBadId() {
        showFacade.moveUp(SpringToUtils.newShow(objectGenerator, Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#moveDown(ShowTO)}.
     */
    @Test
    public void testMoveDown() {
        final Show show1 = SpringEntitiesUtils.getShow(1);
        show1.setPosition(1);
        final Show show2 = SpringEntitiesUtils.getShow(2);
        show2.setPosition(0);

        showFacade.moveDown(SpringToUtils.newShow(objectGenerator, 1));
        DeepAsserts.assertEquals(show1, SpringUtils.getShow(entityManager, 1));
        DeepAsserts.assertEquals(show2, SpringUtils.getShow(entityManager, 2));
        for (int i = 3; i <= SpringUtils.SHOWS_COUNT; i++) {
            DeepAsserts.assertEquals(SpringEntitiesUtils.getShow(i), SpringUtils.getShow(entityManager, i));
        }
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#moveDown(ShowTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDownWithNullArgument() {
        showFacade.moveDown(null);
    }

    /**
     * Test method for {@link ShowFacade#moveDown(ShowTO)} with show with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDownWithShowWithNullId() {
        showFacade.moveDown(SpringToUtils.newShow(objectGenerator));
    }

    /**
     * Test method for {@link ShowFacade#moveDown(ShowTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDownWithNotMovableArgument() {
        final ShowTO show = objectGenerator.generate(ShowTO.class);
        show.setId(SpringUtils.SHOWS_COUNT);

        showFacade.moveDown(show);
    }

    /**
     * Test method for {@link ShowFacade#moveDown(ShowTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveDownWithBadId() {
        showFacade.moveDown(SpringToUtils.newShow(objectGenerator, Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#exists(ShowTO)} with existing show.
     */
    @Test
    public void testExists() {
        for (int i = 1; i <= SpringUtils.SHOWS_COUNT; i++) {
            assertTrue(showFacade.exists(SpringToUtils.newShow(objectGenerator, i)));
        }

        assertFalse(showFacade.exists(SpringToUtils.newShow(objectGenerator, Integer.MAX_VALUE)));

        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#exists(ShowTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExistsWithNullArgument() {
        showFacade.exists(null);
    }

    /**
     * Test method for {@link ShowFacade#exists(ShowTO)} with show with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testExistsWithShowWithNullId() {
        showFacade.exists(SpringToUtils.newShow(objectGenerator));
    }

    /**
     * Test method for {@link ShowFacade#updatePositions()}.
     */
    @Test
    public void testUpdatePositions() {
        showFacade.updatePositions();

        for (int i = 1; i <= SpringUtils.SHOWS_COUNT; i++) {
            DeepAsserts.assertEquals(SpringEntitiesUtils.getShow(i), SpringUtils.getShow(entityManager, i));
        }
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#getTotalLength()}.
     */
    @Test
    public void testGetTotalLength() {
        final Time length = new Time(1998);

        DeepAsserts.assertEquals(length, showFacade.getTotalLength());
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#getSeasonsCount()}.
     */
    @Test
    public void testGetSeasonsCount() {
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT, showFacade.getSeasonsCount());
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#getEpisodesCount()}.
     */
    @Test
    public void testGetEpisodesCount() {
        DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT, showFacade.getEpisodesCount());
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
    }

}
