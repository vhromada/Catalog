package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.common.CollectionUtils;
import cz.vhromada.catalog.common.EpisodeUtils;
import cz.vhromada.catalog.common.GenreUtils;
import cz.vhromada.catalog.common.SeasonUtils;
import cz.vhromada.catalog.common.ShowUtils;
import cz.vhromada.catalog.common.TestConstants;
import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.domain.Episode;
import cz.vhromada.catalog.domain.Season;
import cz.vhromada.catalog.domain.Show;
import cz.vhromada.catalog.entity.GenreTO;
import cz.vhromada.catalog.entity.ShowTO;
import cz.vhromada.catalog.facade.ShowFacade;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents integration test for class {@link ShowFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeContext.xml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ShowFacadeImplIntegrationTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private EntityManager entityManager;

    /**
     * Instance of {@link ShowFacade}
     */
    @Autowired
    private ShowFacade showFacade;

    /**
     * Test method for {@link ShowFacade#newData()}.
     */
    @Test
    @DirtiesContext
    public void testNewData() {
        showFacade.newData();

        assertEquals(0, ShowUtils.getShowsCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#getShows()}.
     */
    @Test
    public void testGetShows() {
        ShowUtils.assertShowListDeepEquals(showFacade.getShows(), ShowUtils.getShows());

        assertEquals(ShowUtils.SHOWS_COUNT, ShowUtils.getShowsCount(entityManager));
        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#getShow(Integer)}.
     */
    @Test
    public void testGetShow() {
        for (int i = 1; i <= ShowUtils.SHOWS_COUNT; i++) {
            ShowUtils.assertShowDeepEquals(showFacade.getShow(i), ShowUtils.getShow(i));
        }

        assertNull(showFacade.getShow(Integer.MAX_VALUE));

        assertEquals(ShowUtils.SHOWS_COUNT, ShowUtils.getShowsCount(entityManager));
        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#getShow(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetShow_NullArgument() {
        showFacade.getShow(null);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)}.
     */
    @Test
    @DirtiesContext
    public void testAdd() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.getGenreTO(1)));
        final Show expectedShow = ShowUtils.newShow(ShowUtils.SHOWS_COUNT + 1);
        expectedShow.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));

        showFacade.add(show);

        final Show addedShow = ShowUtils.getShow(entityManager, ShowUtils.SHOWS_COUNT + 1);
        ShowUtils.assertShowDeepEquals(expectedShow, addedShow);

        assertEquals(ShowUtils.SHOWS_COUNT + 1, ShowUtils.getShowsCount(entityManager));
        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        showFacade.add(null);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NotNullId() {
        showFacade.add(ShowUtils.newShowTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with null czech name.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullCzechName() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setCzechName(null);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with empty string as czech name.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_EmptyCzechName() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setCzechName("");

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with null original name.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullOriginalName() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setOriginalName(null);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with empty string as original name.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_EmptyOriginalName() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setOriginalName("");

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with null URL to ČSFD page about show.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullCsfd() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setCsfd(null);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with bad minimal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadMinimalImdb() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with bad divider IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadDividerImdb() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setImdbCode(0);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with bad maximal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadMaximalImdb() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with null URL to english Wikipedia page about show.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullWikiEn() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setWikiEn(null);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with null URL to czech Wikipedia page about show.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullWikiCz() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setWikiCz(null);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with null path to file with show picture.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullPicture() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setPicture(null);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with null note.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullNote() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setNote(null);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with null genres.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullGenres() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setGenres(null);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with genres with null value.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadGenres() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), null));

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with genres with genre with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_Genres_Genre_NullId() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), GenreUtils.newGenreTO(null)));

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with show with genres with genre with null name.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_Genres_Genre_NullName() {
        final ShowTO show = ShowUtils.newShowTO(null);
        final GenreTO badGenre = GenreUtils.newGenreTO(1);
        badGenre.setName(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), badGenre));

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)}.
     */
    @Test
    @DirtiesContext
    public void testUpdate() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setGenres(CollectionUtils.newList(GenreUtils.getGenreTO(4)));

        showFacade.update(show);

        final Show updatedShow = ShowUtils.getShow(entityManager, 1);
        ShowUtils.assertShowDeepEquals(show, updatedShow);

        assertEquals(ShowUtils.SHOWS_COUNT, ShowUtils.getShowsCount(entityManager));
        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        showFacade.update(null);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullId() {
        showFacade.update(ShowUtils.newShowTO(null));
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with null czech name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullCzechName() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setCzechName(null);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with empty string as czech name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_EmptyCzechName() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setCzechName("");

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with null original name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullOriginalName() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setOriginalName(null);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with empty string as original name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_EmptyOriginalName() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setOriginalName("");

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with null URL to ČSFD page about show.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullCsfd() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setCsfd(null);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with bad minimal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadMinimalImdb() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with bad divider IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadDividerImdb() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setImdbCode(0);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with bad maximal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadMaximalImdb() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with null URL to english Wikipedia page about show.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullWikiEn() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setWikiEn(null);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with null URL to czech Wikipedia page about show.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullWikiCz() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setWikiCz(null);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with null path to file with show picture.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullPicture() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setPicture(null);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with null note.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullNote() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setNote(null);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with null genres.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullGenres() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setGenres(null);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with genres with null value.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadGenres() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), null));

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with genres with genre with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_Genres_Genre_NullId() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), GenreUtils.newGenreTO(null)));

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with genres with genre with null name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_Genres_Genre_NullName() {
        final ShowTO show = ShowUtils.newShowTO(1);
        final GenreTO badGenre = GenreUtils.newGenreTO(1);
        badGenre.setName(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), badGenre));

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with show with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdate_BadId() {
        showFacade.update(ShowUtils.newShowTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#remove(ShowTO)}.
     */
    @Test
    @DirtiesContext
    public void testRemove() {
        showFacade.remove(ShowUtils.newShowTO(1));

        assertNull(ShowUtils.getShow(entityManager, 1));

        assertEquals(ShowUtils.SHOWS_COUNT - 1, ShowUtils.getShowsCount(entityManager));
        assertEquals(SeasonUtils.SEASONS_COUNT - SeasonUtils.SEASONS_PER_SHOW_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT - EpisodeUtils.EPISODES_PER_SHOW_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#remove(ShowTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        showFacade.remove(null);
    }

    /**
     * Test method for {@link ShowFacade#remove(ShowTO)} with show with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testRemove_NullId() {
        showFacade.remove(ShowUtils.newShowTO(null));
    }

    /**
     * Test method for {@link ShowFacade#remove(ShowTO)} with show with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testRemove_BadId() {
        showFacade.remove(ShowUtils.newShowTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#duplicate(ShowTO)}.
     */
    @Test
    @DirtiesContext
    public void testDuplicate() {
        final Show show = ShowUtils.getShow(ShowUtils.SHOWS_COUNT);
        show.setId(ShowUtils.SHOWS_COUNT + 1);
        for (final Season season : show.getSeasons()) {
            final int index = show.getSeasons().indexOf(season);
            season.setId(SeasonUtils.SEASONS_COUNT + index + 1);
            for (final Episode episode : season.getEpisodes()) {
                episode.setId(EpisodeUtils.EPISODES_COUNT + EpisodeUtils.EPISODES_PER_SEASON_COUNT * index + season.getEpisodes().indexOf(episode) + 1);
            }
        }

        showFacade.duplicate(ShowUtils.newShowTO(ShowUtils.SHOWS_COUNT));

        final Show duplicatedShow = ShowUtils.getShow(entityManager, ShowUtils.SHOWS_COUNT + 1);
        ShowUtils.assertShowDeepEquals(show, duplicatedShow);

        assertEquals(ShowUtils.SHOWS_COUNT + 1, ShowUtils.getShowsCount(entityManager));
        assertEquals(SeasonUtils.SEASONS_COUNT + SeasonUtils.SEASONS_PER_SHOW_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT + EpisodeUtils.EPISODES_PER_SHOW_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#duplicate(ShowTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        showFacade.duplicate(null);
    }

    /**
     * Test method for {@link ShowFacade#duplicate(ShowTO)} with show with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testDuplicate_NullId() {
        showFacade.duplicate(ShowUtils.newShowTO(null));
    }

    /**
     * Test method for {@link ShowFacade#duplicate(ShowTO)} with show with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testDuplicate_BadId() {
        showFacade.duplicate(ShowUtils.newShowTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#moveUp(ShowTO)}.
     */
    @Test
    @DirtiesContext
    public void testMoveUp() {
        final Show show1 = ShowUtils.getShow(1);
        show1.setPosition(1);
        final Show show2 = ShowUtils.getShow(2);
        show2.setPosition(0);

        showFacade.moveUp(ShowUtils.newShowTO(2));

        ShowUtils.assertShowDeepEquals(show1, ShowUtils.getShow(entityManager, 1));
        ShowUtils.assertShowDeepEquals(show2, ShowUtils.getShow(entityManager, 2));
        for (int i = 3; i <= ShowUtils.SHOWS_COUNT; i++) {
            ShowUtils.assertShowDeepEquals(ShowUtils.getShow(i), ShowUtils.getShow(entityManager, i));
        }

        assertEquals(ShowUtils.SHOWS_COUNT, ShowUtils.getShowsCount(entityManager));
        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#moveUp(ShowTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        showFacade.moveUp(null);
    }

    /**
     * Test method for {@link ShowFacade#moveUp(ShowTO)} with show with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_NullId() {
        showFacade.moveUp(ShowUtils.newShowTO(null));
    }

    /**
     * Test method for {@link ShowFacade#moveUp(ShowTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_NotMovableArgument() {
        showFacade.moveUp(ShowUtils.newShowTO(1));
    }

    /**
     * Test method for {@link ShowFacade#moveUp(ShowTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveUp_BadId() {
        showFacade.moveUp(ShowUtils.newShowTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#moveDown(ShowTO)}.
     */
    @Test
    @DirtiesContext
    public void testMoveDown() {
        final Show show1 = ShowUtils.getShow(1);
        show1.setPosition(1);
        final Show show2 = ShowUtils.getShow(2);
        show2.setPosition(0);

        showFacade.moveDown(ShowUtils.newShowTO(1));

        ShowUtils.assertShowDeepEquals(show1, ShowUtils.getShow(entityManager, 1));
        ShowUtils.assertShowDeepEquals(show2, ShowUtils.getShow(entityManager, 2));
        for (int i = 3; i <= ShowUtils.SHOWS_COUNT; i++) {
            ShowUtils.assertShowDeepEquals(ShowUtils.getShow(i), ShowUtils.getShow(entityManager, i));
        }

        assertEquals(ShowUtils.SHOWS_COUNT, ShowUtils.getShowsCount(entityManager));
        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#moveDown(ShowTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        showFacade.moveDown(null);
    }

    /**
     * Test method for {@link ShowFacade#moveDown(ShowTO)} with show with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_NullId() {
        showFacade.moveDown(ShowUtils.newShowTO(null));
    }

    /**
     * Test method for {@link ShowFacade#moveDown(ShowTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_NotMovableArgument() {
        showFacade.moveDown(ShowUtils.newShowTO(ShowUtils.SHOWS_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#moveDown(ShowTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveDown_BadId() {
        showFacade.moveDown(ShowUtils.newShowTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#updatePositions()}.
     */
    @Test
    @DirtiesContext
    public void testUpdatePositions() {
        showFacade.updatePositions();

        for (int i = 1; i <= ShowUtils.SHOWS_COUNT; i++) {
            ShowUtils.assertShowDeepEquals(ShowUtils.getShow(i), ShowUtils.getShow(entityManager, i));
        }

        assertEquals(ShowUtils.SHOWS_COUNT, ShowUtils.getShowsCount(entityManager));
        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#getTotalLength()}.
     */
    @Test
    public void testGetTotalLength() {
        final Time length = new Time(1998);

        assertEquals(length, showFacade.getTotalLength());

        assertEquals(ShowUtils.SHOWS_COUNT, ShowUtils.getShowsCount(entityManager));
        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#getSeasonsCount()}.
     */
    @Test
    public void testGetSeasonsCount() {
        assertEquals(SeasonUtils.SEASONS_COUNT, showFacade.getSeasonsCount());

        assertEquals(ShowUtils.SHOWS_COUNT, ShowUtils.getShowsCount(entityManager));
        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#getEpisodesCount()}.
     */
    @Test
    public void testGetEpisodesCount() {
        assertEquals(EpisodeUtils.EPISODES_COUNT, showFacade.getEpisodesCount());

        assertEquals(ShowUtils.SHOWS_COUNT, ShowUtils.getShowsCount(entityManager));
        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

}
