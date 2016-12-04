package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.domain.Episode;
import cz.vhromada.catalog.domain.Season;
import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.catalog.facade.ShowFacade;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.EpisodeUtils;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.catalog.utils.SeasonUtils;
import cz.vhromada.catalog.utils.ShowUtils;
import cz.vhromada.catalog.utils.TestConstants;

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
@ContextConfiguration("classpath:testCatalogContext.xml")
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
     * Test method for {@link ShowFacade#add(Show)}.
     */
    @Test
    @DirtiesContext
    public void testAdd() {
        final Show show = ShowUtils.newShow(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        final cz.vhromada.catalog.domain.Show expectedShow = ShowUtils.newShowDomain(ShowUtils.SHOWS_COUNT + 1);
        expectedShow.setGenres(CollectionUtils.newList(GenreUtils.getGenreDomain(1)));

        showFacade.add(show);

        final cz.vhromada.catalog.domain.Show addedShow = ShowUtils.getShow(entityManager, ShowUtils.SHOWS_COUNT + 1);
        ShowUtils.assertShowDeepEquals(expectedShow, addedShow);

        assertEquals(ShowUtils.SHOWS_COUNT + 1, ShowUtils.getShowsCount(entityManager));
        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        showFacade.add(null);
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with not null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NotNullId() {
        showFacade.add(ShowUtils.newShow(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with null czech name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullCzechName() {
        final Show show = ShowUtils.newShow(null);
        show.setCzechName(null);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with empty string as czech name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_EmptyCzechName() {
        final Show show = ShowUtils.newShow(null);
        show.setCzechName("");

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with null original name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullOriginalName() {
        final Show show = ShowUtils.newShow(null);
        show.setOriginalName(null);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with empty string as original name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_EmptyOriginalName() {
        final Show show = ShowUtils.newShow(null);
        show.setOriginalName("");

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with null URL to ČSFD page about show.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullCsfd() {
        final Show show = ShowUtils.newShow(null);
        show.setCsfd(null);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with bad minimal IMDB code.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadMinimalImdb() {
        final Show show = ShowUtils.newShow(null);
        show.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with bad divider IMDB code.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadDividerImdb() {
        final Show show = ShowUtils.newShow(null);
        show.setImdbCode(0);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with bad maximal IMDB code.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadMaximalImdb() {
        final Show show = ShowUtils.newShow(null);
        show.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with null URL to english Wikipedia page about show.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullWikiEn() {
        final Show show = ShowUtils.newShow(null);
        show.setWikiEn(null);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with null URL to czech Wikipedia page about show.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullWikiCz() {
        final Show show = ShowUtils.newShow(null);
        show.setWikiCz(null);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with null path to file with show picture.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullPicture() {
        final Show show = ShowUtils.newShow(null);
        show.setPicture(null);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullNote() {
        final Show show = ShowUtils.newShow(null);
        show.setNote(null);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with null genres.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullGenres() {
        final Show show = ShowUtils.newShow(null);
        show.setGenres(null);

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with genres with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadGenres() {
        final Show show = ShowUtils.newShow(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), null));

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with genres with genre with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_Genres_Genre_NullId() {
        final Show show = ShowUtils.newShow(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), GenreUtils.newGenre(null)));

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with genres with genre with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_Genres_Genre_NullName() {
        final Show show = ShowUtils.newShow(null);
        final Genre badGenre = GenreUtils.newGenre(1);
        badGenre.setName(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), badGenre));

        showFacade.add(show);
    }

    /**
     * Test method for {@link ShowFacade#update(Show)}.
     */
    @Test
    @DirtiesContext
    public void testUpdate() {
        final Show show = ShowUtils.newShow(1);
        show.setGenres(CollectionUtils.newList(GenreUtils.getGenre(4)));

        showFacade.update(show);

        final cz.vhromada.catalog.domain.Show updatedShow = ShowUtils.getShow(entityManager, 1);
        ShowUtils.assertShowDeepEquals(show, updatedShow);

        assertEquals(ShowUtils.SHOWS_COUNT, ShowUtils.getShowsCount(entityManager));
        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        showFacade.update(null);
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullId() {
        showFacade.update(ShowUtils.newShow(null));
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with null czech name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullCzechName() {
        final Show show = ShowUtils.newShow(1);
        show.setCzechName(null);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with empty string as czech name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_EmptyCzechName() {
        final Show show = ShowUtils.newShow(1);
        show.setCzechName("");

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with null original name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullOriginalName() {
        final Show show = ShowUtils.newShow(1);
        show.setOriginalName(null);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with empty string as original name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_EmptyOriginalName() {
        final Show show = ShowUtils.newShow(1);
        show.setOriginalName("");

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with null URL to ČSFD page about show.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullCsfd() {
        final Show show = ShowUtils.newShow(1);
        show.setCsfd(null);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with bad minimal IMDB code.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadMinimalImdb() {
        final Show show = ShowUtils.newShow(1);
        show.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with bad divider IMDB code.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadDividerImdb() {
        final Show show = ShowUtils.newShow(1);
        show.setImdbCode(0);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with bad maximal IMDB code.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadMaximalImdb() {
        final Show show = ShowUtils.newShow(1);
        show.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with null URL to english Wikipedia page about show.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullWikiEn() {
        final Show show = ShowUtils.newShow(1);
        show.setWikiEn(null);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with null URL to czech Wikipedia page about show.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullWikiCz() {
        final Show show = ShowUtils.newShow(1);
        show.setWikiCz(null);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with null path to file with show picture.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullPicture() {
        final Show show = ShowUtils.newShow(1);
        show.setPicture(null);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullNote() {
        final Show show = ShowUtils.newShow(1);
        show.setNote(null);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with null genres.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullGenres() {
        final Show show = ShowUtils.newShow(1);
        show.setGenres(null);

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with genres with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadGenres() {
        final Show show = ShowUtils.newShow(1);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), null));

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with genres with genre with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_Genres_Genre_NullId() {
        final Show show = ShowUtils.newShow(1);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), GenreUtils.newGenre(null)));

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with genres with genre with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_Genres_Genre_NullName() {
        final Show show = ShowUtils.newShow(1);
        final Genre badGenre = GenreUtils.newGenre(1);
        badGenre.setName(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), badGenre));

        showFacade.update(show);
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadId() {
        showFacade.update(ShowUtils.newShow(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#remove(Show)}.
     */
    @Test
    @DirtiesContext
    public void testRemove() {
        showFacade.remove(ShowUtils.newShow(1));

        assertNull(ShowUtils.getShow(entityManager, 1));

        assertEquals(ShowUtils.SHOWS_COUNT - 1, ShowUtils.getShowsCount(entityManager));
        assertEquals(SeasonUtils.SEASONS_COUNT - SeasonUtils.SEASONS_PER_SHOW_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT - EpisodeUtils.EPISODES_PER_SHOW_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#remove(Show)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        showFacade.remove(null);
    }

    /**
     * Test method for {@link ShowFacade#remove(Show)} with show with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullId() {
        showFacade.remove(ShowUtils.newShow(null));
    }

    /**
     * Test method for {@link ShowFacade#remove(Show)} with show with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_BadId() {
        showFacade.remove(ShowUtils.newShow(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#duplicate(Show)}.
     */
    @Test
    @DirtiesContext
    public void testDuplicate() {
        final cz.vhromada.catalog.domain.Show show = ShowUtils.getShow(ShowUtils.SHOWS_COUNT);
        show.setId(ShowUtils.SHOWS_COUNT + 1);
        for (final Season season : show.getSeasons()) {
            final int index = show.getSeasons().indexOf(season);
            season.setId(SeasonUtils.SEASONS_COUNT + index + 1);
            for (final Episode episode : season.getEpisodes()) {
                episode.setId(EpisodeUtils.EPISODES_COUNT + EpisodeUtils.EPISODES_PER_SEASON_COUNT * index + season.getEpisodes().indexOf(episode) + 1);
            }
        }

        showFacade.duplicate(ShowUtils.newShow(ShowUtils.SHOWS_COUNT));

        final cz.vhromada.catalog.domain.Show duplicatedShow = ShowUtils.getShow(entityManager, ShowUtils.SHOWS_COUNT + 1);
        ShowUtils.assertShowDeepEquals(show, duplicatedShow);

        assertEquals(ShowUtils.SHOWS_COUNT + 1, ShowUtils.getShowsCount(entityManager));
        assertEquals(SeasonUtils.SEASONS_COUNT + SeasonUtils.SEASONS_PER_SHOW_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT + EpisodeUtils.EPISODES_PER_SHOW_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link ShowFacade#duplicate(Show)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        showFacade.duplicate(null);
    }

    /**
     * Test method for {@link ShowFacade#duplicate(Show)} with show with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullId() {
        showFacade.duplicate(ShowUtils.newShow(null));
    }

    /**
     * Test method for {@link ShowFacade#duplicate(Show)} with show with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_BadId() {
        showFacade.duplicate(ShowUtils.newShow(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#moveUp(Show)}.
     */
    @Test
    @DirtiesContext
    public void testMoveUp() {
        final cz.vhromada.catalog.domain.Show show1 = ShowUtils.getShow(1);
        show1.setPosition(1);
        final cz.vhromada.catalog.domain.Show show2 = ShowUtils.getShow(2);
        show2.setPosition(0);

        showFacade.moveUp(ShowUtils.newShow(2));

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
     * Test method for {@link ShowFacade#moveUp(Show)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        showFacade.moveUp(null);
    }

    /**
     * Test method for {@link ShowFacade#moveUp(Show)} with show with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullId() {
        showFacade.moveUp(ShowUtils.newShow(null));
    }

    /**
     * Test method for {@link ShowFacade#moveUp(Show)} with not movable argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NotMovableArgument() {
        showFacade.moveUp(ShowUtils.newShow(1));
    }

    /**
     * Test method for {@link ShowFacade#moveUp(Show)} with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_BadId() {
        showFacade.moveUp(ShowUtils.newShow(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#moveDown(Show)}.
     */
    @Test
    @DirtiesContext
    public void testMoveDown() {
        final cz.vhromada.catalog.domain.Show show1 = ShowUtils.getShow(1);
        show1.setPosition(1);
        final cz.vhromada.catalog.domain.Show show2 = ShowUtils.getShow(2);
        show2.setPosition(0);

        showFacade.moveDown(ShowUtils.newShow(1));

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
     * Test method for {@link ShowFacade#moveDown(Show)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        showFacade.moveDown(null);
    }

    /**
     * Test method for {@link ShowFacade#moveDown(Show)} with show with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullId() {
        showFacade.moveDown(ShowUtils.newShow(null));
    }

    /**
     * Test method for {@link ShowFacade#moveDown(Show)} with not movable argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NotMovableArgument() {
        showFacade.moveDown(ShowUtils.newShow(ShowUtils.SHOWS_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#moveDown(Show)} with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_BadId() {
        showFacade.moveDown(ShowUtils.newShow(Integer.MAX_VALUE));
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
