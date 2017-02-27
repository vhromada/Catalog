package cz.vhromada.catalog.facade.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
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
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

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
@ContextConfiguration(classes = CatalogTestConfiguration.class)
@DirtiesContext
public class ShowFacadeImplIntegrationTest {

    /**
     * Event for null show
     */
    private static final Event NULL_SHOW_EVENT = new Event(Severity.ERROR, "SHOW_NULL", "Show mustn't be null.");

    /**
     * Event for show with null ID
     */
    private static final Event NULL_SHOW_ID_EVENT = new Event(Severity.ERROR, "SHOW_ID_NULL", "ID mustn't be null.");

    /**
     * Event for not existing show
     */
    private static final Event NOT_EXIST_SHOW_EVENT = new Event(Severity.ERROR, "SHOW_NOT_EXIST", "Show doesn't exist.");

    /**
     * Event for invalid IMDB code
     */
    private static final Event INVALID_IMDB_CODE_EVENT = new Event(Severity.ERROR, "SHOW_IMDB_CODE_NOT_VALID",
            "IMDB code must be between 1 and 9999999 or -1.");

    /**
     * Event for genre with null ID
     */
    private static final Event NULL_GENRE_ID_EVENT = new Event(Severity.ERROR, "GENRE_ID_NULL", "ID mustn't be null.");

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
    public void newData() {
        final Result<Void> result = showFacade.newData();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(ShowUtils.getShowsCount(entityManager), is(0));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(0));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(0));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#getAll()}.
     */
    @Test
    public void getAll() {
        final Result<List<Show>> result = showFacade.getAll();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));
        ShowUtils.assertShowListDeepEquals(result.getData(), ShowUtils.getShows());

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#get(Integer)}.
     */
    @Test
    public void get() {
        for (int i = 1; i <= ShowUtils.SHOWS_COUNT; i++) {
            final Result<Show> result = showFacade.get(i);

            assertThat(result, is(notNullValue()));
            assertThat(result.getEvents(), is(notNullValue()));
            assertThat(result.getStatus(), is(Status.OK));
            assertThat(result.getEvents().isEmpty(), is(true));
            ShowUtils.assertShowDeepEquals(result.getData(), ShowUtils.getShow(i));
        }

        final Result<Show> result = showFacade.get(Integer.MAX_VALUE);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#get(Integer)} with null show.
     */
    @Test
    public void get_NullShow() {
        final Result<Show> result = showFacade.get(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "ID_NULL", "ID mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#add(Show)}.
     */
    @Test
    @DirtiesContext
    public void add() {
        final Show show = ShowUtils.newShow(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        final cz.vhromada.catalog.domain.Show expectedShow = ShowUtils.newShowDomain(ShowUtils.SHOWS_COUNT + 1);
        expectedShow.setGenres(CollectionUtils.newList(GenreUtils.getGenreDomain(1)));

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        final cz.vhromada.catalog.domain.Show addedShow = ShowUtils.getShow(entityManager, ShowUtils.SHOWS_COUNT + 1);
        ShowUtils.assertShowDeepEquals(expectedShow, addedShow);
        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT + 1));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with null show.
     */
    @Test
    public void add_NullShow() {
        final Result<Void> result = showFacade.add(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SHOW_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with not null ID.
     */
    @Test
    public void add_NotNullId() {
        final Result<Void> result = showFacade.add(ShowUtils.newShow(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_ID_NOT_NULL", "ID must be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with null czech name.
     */
    @Test
    public void add_NullCzechName() {
        final Show show = ShowUtils.newShow(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        show.setCzechName(null);

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_CZECH_NAME_NULL", "Czech name mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with empty string as czech name.
     */
    @Test
    public void add_EmptyCzechName() {
        final Show show = ShowUtils.newShow(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        show.setCzechName("");

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_CZECH_NAME_EMPTY", "Czech name mustn't be empty string.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with null original name.
     */
    @Test
    public void add_NullOriginalName() {
        final Show show = ShowUtils.newShow(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        show.setOriginalName(null);

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_ORIGINAL_NAME_NULL", "Original name mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with empty string as original name.
     */
    @Test
    public void add_EmptyOriginalName() {
        final Show show = ShowUtils.newShow(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        show.setOriginalName("");

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_ORIGINAL_NAME_EMPTY", "Original name mustn't be empty string.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with null URL to ČSFD page about show.
     */
    @Test
    public void add_NullCsfd() {
        final Show show = ShowUtils.newShow(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        show.setCsfd(null);

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_CSFD_NULL", "URL to ČSFD page about show mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with bad minimal IMDB code.
     */
    @Test
    public void add_BadMinimalImdb() {
        final Show show = ShowUtils.newShow(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        show.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(INVALID_IMDB_CODE_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with bad divider IMDB code.
     */
    @Test
    public void add_BadDividerImdb() {
        final Show show = ShowUtils.newShow(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        show.setImdbCode(0);

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(INVALID_IMDB_CODE_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with bad maximal IMDB code.
     */
    @Test
    public void add_BadMaximalImdb() {
        final Show show = ShowUtils.newShow(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        show.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(INVALID_IMDB_CODE_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with null URL to english Wikipedia page about show.
     */
    @Test
    public void add_NullWikiEn() {
        final Show show = ShowUtils.newShow(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        show.setWikiEn(null);

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_WIKI_EN_NULL", "URL to english Wikipedia page about show mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with null URL to czech Wikipedia page about show.
     */
    @Test
    public void add_NullWikiCz() {
        final Show show = ShowUtils.newShow(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        show.setWikiCz(null);

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_WIKI_CZ_NULL", "URL to czech Wikipedia page about show mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with null path to file with show's picture.
     */
    @Test
    public void add_NullPicture() {
        final Show show = ShowUtils.newShow(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        show.setPicture(null);

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_PICTURE_NULL", "Picture mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with null note.
     */
    @Test
    public void add_NullNote() {
        final Show show = ShowUtils.newShow(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        show.setNote(null);

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_NOTE_NULL", "Note mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with null genres.
     */
    @Test
    public void add_NullGenres() {
        final Show show = ShowUtils.newShow(null);
        show.setGenres(null);

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_GENRES_NULL", "Genres mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with genres with null value.
     */
    @Test
    public void add_BadGenres() {
        final Show show = ShowUtils.newShow(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), null));

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_GENRES_CONTAIN_NULL", "Genres mustn't contain null value.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with genres with genre with null ID.
     */
    @Test
    public void add_NullGenreId() {
        final Show show = ShowUtils.newShow(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), GenreUtils.newGenre(null)));

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_GENRE_ID_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with genres with genre with null name.
     */
    @Test
    public void add_NullGenreName() {
        final Show show = ShowUtils.newShow(null);
        final Genre badGenre = GenreUtils.newGenre(1);
        badGenre.setName(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), badGenre));

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GENRE_NAME_NULL", "Name mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with genres with genre with empty string as name.
     */
    @Test
    public void add_EmptyGenreName() {
        final Show show = ShowUtils.newShow(null);
        final Genre badGenre = GenreUtils.newGenre(1);
        badGenre.setName("");
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), badGenre));

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GENRE_NAME_EMPTY", "Name mustn't be empty string.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#update(Show)}.
     */
    @Test
    @DirtiesContext
    public void update() {
        final Show show = ShowUtils.newShow(1);
        show.setGenres(CollectionUtils.newList(GenreUtils.getGenre(4)));

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        final cz.vhromada.catalog.domain.Show updatedShow = ShowUtils.getShow(entityManager, 1);
        ShowUtils.assertShowDeepEquals(show, updatedShow);
        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with null show.
     */
    @Test
    public void update_NullShow() {
        final Result<Void> result = showFacade.update(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SHOW_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with null ID.
     */
    @Test
    public void update_NullId() {
        final Show show = ShowUtils.newShow(1);
        show.setId(null);

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SHOW_ID_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with null czech name.
     */
    @Test
    public void update_NullCzechName() {
        final Show show = ShowUtils.newShow(1);
        show.setCzechName(null);

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_CZECH_NAME_NULL", "Czech name mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with empty string as czech name.
     */
    @Test
    public void update_EmptyCzechName() {
        final Show show = ShowUtils.newShow(1);
        show.setCzechName("");

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_CZECH_NAME_EMPTY", "Czech name mustn't be empty string.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with null original name.
     */
    @Test
    public void update_NullOriginalName() {
        final Show show = ShowUtils.newShow(1);
        show.setOriginalName(null);

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_ORIGINAL_NAME_NULL", "Original name mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with empty string as original name.
     */
    @Test
    public void update_EmptyOriginalName() {
        final Show show = ShowUtils.newShow(1);
        show.setOriginalName("");

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_ORIGINAL_NAME_EMPTY", "Original name mustn't be empty string.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with null URL to ČSFD page about show.
     */
    @Test
    public void update_NullCsfd() {
        final Show show = ShowUtils.newShow(1);
        show.setCsfd(null);

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_CSFD_NULL", "URL to ČSFD page about show mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with bad minimal IMDB code.
     */
    @Test
    public void update_BadMinimalImdb() {
        final Show show = ShowUtils.newShow(1);
        show.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(INVALID_IMDB_CODE_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with bad divider IMDB code.
     */
    @Test
    public void update_BadDividerImdb() {
        final Show show = ShowUtils.newShow(1);
        show.setImdbCode(0);

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(INVALID_IMDB_CODE_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with bad maximal IMDB code.
     */
    @Test
    public void update_BadMaximalImdb() {
        final Show show = ShowUtils.newShow(1);
        show.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(INVALID_IMDB_CODE_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with null URL to english Wikipedia page about show.
     */
    @Test
    public void update_NullWikiEn() {
        final Show show = ShowUtils.newShow(1);
        show.setWikiEn(null);

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_WIKI_EN_NULL", "URL to english Wikipedia page about show mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with null URL to czech Wikipedia page about show.
     */
    @Test
    public void update_NullWikiCz() {
        final Show show = ShowUtils.newShow(1);
        show.setWikiCz(null);

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_WIKI_CZ_NULL", "URL to czech Wikipedia page about show mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with null path to file with show's picture.
     */
    @Test
    public void update_NullPicture() {
        final Show show = ShowUtils.newShow(1);
        show.setPicture(null);

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_PICTURE_NULL", "Picture mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with null note.
     */
    @Test
    public void update_NullNote() {
        final Show show = ShowUtils.newShow(1);
        show.setNote(null);

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_NOTE_NULL", "Note mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with null genres.
     */
    @Test
    public void update_NullGenres() {
        final Show show = ShowUtils.newShow(1);
        show.setGenres(null);

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_GENRES_NULL", "Genres mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with genres with null value.
     */
    @Test
    public void update_BadGenres() {
        final Show show = ShowUtils.newShow(1);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), null));

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_GENRES_CONTAIN_NULL", "Genres mustn't contain null value.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with genres with genre with null ID.
     */
    @Test
    public void update_NullGenreId() {
        final Show show = ShowUtils.newShow(1);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), GenreUtils.newGenre(null)));

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_GENRE_ID_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with genres with genre with null name.
     */
    @Test
    public void update_NullGenreName() {
        final Show show = ShowUtils.newShow(1);
        final Genre badGenre = GenreUtils.newGenre(1);
        badGenre.setName(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), badGenre));

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GENRE_NAME_NULL", "Name mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with genres with genre with empty string as name.
     */
    @Test
    public void update_EmptyGenreName() {
        final Show show = ShowUtils.newShow(1);
        final Genre badGenre = GenreUtils.newGenre(1);
        badGenre.setName("");
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), badGenre));

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GENRE_NAME_EMPTY", "Name mustn't be empty string.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with bad ID.
     */
    @Test
    public void update_BadId() {
        final Show show = ShowUtils.newShow(1);
        show.setId(Integer.MAX_VALUE);

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_SHOW_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#remove(Show)}.
     */
    @Test
    @DirtiesContext
    public void remove() {
        final Result<Void> result = showFacade.remove(ShowUtils.newShow(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(ShowUtils.getShow(entityManager, 1), is(nullValue()));
        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT - 1));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT - SeasonUtils.SEASONS_PER_SHOW_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT - EpisodeUtils.EPISODES_PER_SHOW_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#remove(Show)} with null show.
     */
    @Test
    public void remove_NullShow() {
        final Result<Void> result = showFacade.remove(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SHOW_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#remove(Show)} with show with null ID.
     */
    @Test
    public void remove_NullId() {
        final Result<Void> result = showFacade.remove(ShowUtils.newShow(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SHOW_ID_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#remove(Show)} with show with bad ID.
     */
    @Test
    public void remove_BadId() {
        final Result<Void> result = showFacade.remove(ShowUtils.newShow(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_SHOW_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#duplicate(Show)}.
     */
    @Test
    @DirtiesContext
    public void duplicate() {
        final cz.vhromada.catalog.domain.Show show = ShowUtils.getShow(ShowUtils.SHOWS_COUNT);
        show.setId(ShowUtils.SHOWS_COUNT + 1);
        for (final Season season : show.getSeasons()) {
            final int index = show.getSeasons().indexOf(season);
            season.setId(SeasonUtils.SEASONS_COUNT + index + 1);
            for (final Episode episode : season.getEpisodes()) {
                episode.setId(EpisodeUtils.EPISODES_COUNT + EpisodeUtils.EPISODES_PER_SEASON_COUNT * index + season.getEpisodes().indexOf(episode) + 1);
            }
        }

        final Result<Void> result = showFacade.duplicate(ShowUtils.newShow(ShowUtils.SHOWS_COUNT));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        final cz.vhromada.catalog.domain.Show duplicatedShow = ShowUtils.getShow(entityManager, ShowUtils.SHOWS_COUNT + 1);
        ShowUtils.assertShowDeepEquals(show, duplicatedShow);
        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT + 1));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT + SeasonUtils.SEASONS_PER_SHOW_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT + EpisodeUtils.EPISODES_PER_SHOW_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#duplicate(Show)} with null show.
     */
    @Test
    public void duplicate_NullShow() {
        final Result<Void> result = showFacade.duplicate(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SHOW_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#duplicate(Show)} with show with null ID.
     */
    @Test
    public void duplicate_NullId() {
        final Result<Void> result = showFacade.duplicate(ShowUtils.newShow(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SHOW_ID_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#duplicate(Show)} with show with bad ID.
     */
    @Test
    public void duplicate_BadId() {
        final Result<Void> result = showFacade.duplicate(ShowUtils.newShow(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_SHOW_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#moveUp(Show)}.
     */
    @Test
    @DirtiesContext
    public void moveUp() {
        final cz.vhromada.catalog.domain.Show show1 = ShowUtils.getShow(1);
        show1.setPosition(1);
        final cz.vhromada.catalog.domain.Show show2 = ShowUtils.getShow(2);
        show2.setPosition(0);

        final Result<Void> result = showFacade.moveUp(ShowUtils.newShow(2));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        ShowUtils.assertShowDeepEquals(show1, ShowUtils.getShow(entityManager, 1));
        ShowUtils.assertShowDeepEquals(show2, ShowUtils.getShow(entityManager, 2));
        for (int i = 3; i <= ShowUtils.SHOWS_COUNT; i++) {
            ShowUtils.assertShowDeepEquals(ShowUtils.getShow(i), ShowUtils.getShow(entityManager, i));
        }
        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#moveUp(Show)} with null show.
     */
    @Test
    public void moveUp_NullShow() {
        final Result<Void> result = showFacade.moveUp(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SHOW_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#moveUp(Show)} with show with null ID.
     */
    @Test
    public void moveUp_NullId() {
        final Result<Void> result = showFacade.moveUp(ShowUtils.newShow(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SHOW_ID_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#moveUp(Show)} with not movable show.
     */
    @Test
    public void moveUp_NotMovableShow() {
        final Result<Void> result = showFacade.moveUp(ShowUtils.newShow(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_NOT_MOVABLE", "Show can't be moved up.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#moveUp(Show)} with show with bad ID.
     */
    @Test
    public void moveUp_BadId() {
        final Result<Void> result = showFacade.moveUp(ShowUtils.newShow(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_SHOW_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#moveDown(Show)}.
     */
    @Test
    @DirtiesContext
    public void moveDown() {
        final cz.vhromada.catalog.domain.Show show1 = ShowUtils.getShow(1);
        show1.setPosition(1);
        final cz.vhromada.catalog.domain.Show show2 = ShowUtils.getShow(2);
        show2.setPosition(0);

        final Result<Void> result = showFacade.moveDown(ShowUtils.newShow(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        ShowUtils.assertShowDeepEquals(show1, ShowUtils.getShow(entityManager, 1));
        ShowUtils.assertShowDeepEquals(show2, ShowUtils.getShow(entityManager, 2));
        for (int i = 3; i <= ShowUtils.SHOWS_COUNT; i++) {
            ShowUtils.assertShowDeepEquals(ShowUtils.getShow(i), ShowUtils.getShow(entityManager, i));
        }
        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#moveDown(Show)} with null show.
     */
    @Test
    public void moveDown_NullShow() {
        final Result<Void> result = showFacade.moveDown(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SHOW_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#moveDown(Show)} with show with null ID.
     */
    @Test
    public void moveDown_NullId() {
        final Result<Void> result = showFacade.moveDown(ShowUtils.newShow(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SHOW_ID_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#moveDown(Show)} with not movable show.
     */
    @Test
    public void moveDown_NotMovableShow() {
        final Result<Void> result = showFacade.moveDown(ShowUtils.newShow(ShowUtils.SHOWS_COUNT));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_NOT_MOVABLE", "Show can't be moved down.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#moveDown(Show)} with show with bad ID.
     */
    @Test
    public void moveDown_BadId() {
        final Result<Void> result = showFacade.moveDown(ShowUtils.newShow(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_SHOW_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#updatePositions()}.
     */
    @Test
    @DirtiesContext
    public void updatePositions() {
        final Result<Void> result = showFacade.updatePositions();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        for (int i = 1; i <= ShowUtils.SHOWS_COUNT; i++) {
            ShowUtils.assertShowDeepEquals(ShowUtils.getShow(i), ShowUtils.getShow(entityManager, i));
        }
        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#getTotalLength()}.
     */
    @Test
    public void getTotalLength() {
        final Time length = new Time(1998);

        final Result<Time> result = showFacade.getTotalLength();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(length));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#getSeasonsCount()}.
     */
    @Test
    public void getSeasonsCount() {
        final Result<Integer> result = showFacade.getSeasonsCount();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(SeasonUtils.SEASONS_COUNT));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link ShowFacade#getEpisodesCount()}.
     */
    @Test
    public void getEpisodesCount() {
        final Result<Integer> result = showFacade.getEpisodesCount();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

}
