package cz.vhromada.catalog.facade.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.domain.Episode;
import cz.vhromada.catalog.domain.Season;
import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.catalog.facade.CatalogParentFacade;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * A class represents integration test for class {@link ShowFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
public class ShowFacadeImplIntegrationTest extends AbstractParentFacadeIntegrationTest<Show, cz.vhromada.catalog.domain.Show> {

    /**
     * Event for invalid IMDB code
     */
    private static final Event INVALID_IMDB_CODE_EVENT = new Event(Severity.ERROR, "SHOW_IMDB_CODE_NOT_VALID",
            "IMDB code must be between 1 and 9999999 or -1.");

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
     * Test method for {@link ShowFacade#add(Show)} with show with null czech name.
     */
    @Test
    public void add_NullCzechName() {
        final Show show = newData(null);
        show.setCzechName(null);

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_CZECH_NAME_NULL", "Czech name mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with empty string as czech name.
     */
    @Test
    public void add_EmptyCzechName() {
        final Show show = newData(null);
        show.setCzechName("");

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_CZECH_NAME_EMPTY", "Czech name mustn't be empty string.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with null original name.
     */
    @Test
    public void add_NullOriginalName() {
        final Show show = newData(null);
        show.setOriginalName(null);

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_ORIGINAL_NAME_NULL", "Original name mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with empty string as original name.
     */
    @Test
    public void add_EmptyOriginalName() {
        final Show show = newData(null);
        show.setOriginalName("");

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_ORIGINAL_NAME_EMPTY", "Original name mustn't be empty string.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with null URL to ČSFD page about show.
     */
    @Test
    public void add_NullCsfd() {
        final Show show = newData(null);
        show.setCsfd(null);

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_CSFD_NULL", "URL to ČSFD page about show mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with bad minimal IMDB code.
     */
    @Test
    public void add_BadMinimalImdb() {
        final Show show = newData(null);
        show.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(INVALID_IMDB_CODE_EVENT));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with bad divider IMDB code.
     */
    @Test
    public void add_BadDividerImdb() {
        final Show show = newData(null);
        show.setImdbCode(0);

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(INVALID_IMDB_CODE_EVENT));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with bad maximal IMDB code.
     */
    @Test
    public void add_BadMaximalImdb() {
        final Show show = newData(null);
        show.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(INVALID_IMDB_CODE_EVENT));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with null URL to english Wikipedia page about show.
     */
    @Test
    public void add_NullWikiEn() {
        final Show show = newData(null);
        show.setWikiEn(null);

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_WIKI_EN_NULL", "URL to english Wikipedia page about show mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with null URL to czech Wikipedia page about show.
     */
    @Test
    public void add_NullWikiCz() {
        final Show show = newData(null);
        show.setWikiCz(null);

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_WIKI_CZ_NULL", "URL to czech Wikipedia page about show mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with null path to file with show's picture.
     */
    @Test
    public void add_NullPicture() {
        final Show show = newData(null);
        show.setPicture(null);

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_PICTURE_NULL", "Picture mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with null note.
     */
    @Test
    public void add_NullNote() {
        final Show show = newData(null);
        show.setNote(null);

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_NOTE_NULL", "Note mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with null genres.
     */
    @Test
    public void add_NullGenres() {
        final Show show = newData(null);
        show.setGenres(null);

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_GENRES_NULL", "Genres mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with genres with null value.
     */
    @Test
    public void add_BadGenres() {
        final Show show = newData(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), null));

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_GENRES_CONTAIN_NULL", "Genres mustn't contain null value.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with genres with genre with null ID.
     */
    @Test
    public void add_NullGenreId() {
        final Show show = newData(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), GenreUtils.newGenre(null)));

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GENRE_ID_NULL", "ID mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with genres with genre with null name.
     */
    @Test
    public void add_NullGenreName() {
        final Show show = newData(null);
        final Genre badGenre = GenreUtils.newGenre(1);
        badGenre.setName(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), badGenre));

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GENRE_NAME_NULL", "Name mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with show with genres with genre with empty string as name.
     */
    @Test
    public void add_EmptyGenreName() {
        final Show show = newData(null);
        final Genre badGenre = GenreUtils.newGenre(1);
        badGenre.setName("");
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), badGenre));

        final Result<Void> result = showFacade.add(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GENRE_NAME_EMPTY", "Name mustn't be empty string.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with null czech name.
     */
    @Test
    public void update_NullCzechName() {
        final Show show = newData(1);
        show.setCzechName(null);

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_CZECH_NAME_NULL", "Czech name mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with empty string as czech name.
     */
    @Test
    public void update_EmptyCzechName() {
        final Show show = newData(1);
        show.setCzechName("");

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_CZECH_NAME_EMPTY", "Czech name mustn't be empty string.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with null original name.
     */
    @Test
    public void update_NullOriginalName() {
        final Show show = newData(1);
        show.setOriginalName(null);

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_ORIGINAL_NAME_NULL", "Original name mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with empty string as original name.
     */
    @Test
    public void update_EmptyOriginalName() {
        final Show show = newData(1);
        show.setOriginalName("");

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_ORIGINAL_NAME_EMPTY", "Original name mustn't be empty string.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with null URL to ČSFD page about show.
     */
    @Test
    public void update_NullCsfd() {
        final Show show = newData(1);
        show.setCsfd(null);

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_CSFD_NULL", "URL to ČSFD page about show mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with bad minimal IMDB code.
     */
    @Test
    public void update_BadMinimalImdb() {
        final Show show = newData(1);
        show.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(INVALID_IMDB_CODE_EVENT));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with bad divider IMDB code.
     */
    @Test
    public void update_BadDividerImdb() {
        final Show show = newData(1);
        show.setImdbCode(0);

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(INVALID_IMDB_CODE_EVENT));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with bad maximal IMDB code.
     */
    @Test
    public void update_BadMaximalImdb() {
        final Show show = newData(1);
        show.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(INVALID_IMDB_CODE_EVENT));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with null URL to english Wikipedia page about show.
     */
    @Test
    public void update_NullWikiEn() {
        final Show show = newData(1);
        show.setWikiEn(null);

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_WIKI_EN_NULL", "URL to english Wikipedia page about show mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with null URL to czech Wikipedia page about show.
     */
    @Test
    public void update_NullWikiCz() {
        final Show show = newData(1);
        show.setWikiCz(null);

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_WIKI_CZ_NULL", "URL to czech Wikipedia page about show mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with null path to file with show's picture.
     */
    @Test
    public void update_NullPicture() {
        final Show show = newData(1);
        show.setPicture(null);

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_PICTURE_NULL", "Picture mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with null note.
     */
    @Test
    public void update_NullNote() {
        final Show show = newData(1);
        show.setNote(null);

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_NOTE_NULL", "Note mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with null genres.
     */
    @Test
    public void update_NullGenres() {
        final Show show = newData(1);
        show.setGenres(null);

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_GENRES_NULL", "Genres mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with genres with null value.
     */
    @Test
    public void update_BadGenres() {
        final Show show = newData(1);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), null));

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_GENRES_CONTAIN_NULL", "Genres mustn't contain null value.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with genres with genre with null ID.
     */
    @Test
    public void update_NullGenreId() {
        final Show show = newData(1);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), GenreUtils.newGenre(null)));

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GENRE_ID_NULL", "ID mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with genres with genre with null name.
     */
    @Test
    public void update_NullGenreName() {
        final Show show = newData(1);
        final Genre badGenre = GenreUtils.newGenre(1);
        badGenre.setName(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), badGenre));

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GENRE_NAME_NULL", "Name mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with show with genres with genre with empty string as name.
     */
    @Test
    public void update_EmptyGenreName() {
        final Show show = newData(1);
        final Genre badGenre = GenreUtils.newGenre(1);
        badGenre.setName("");
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), badGenre));

        final Result<Void> result = showFacade.update(show);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GENRE_NAME_EMPTY", "Name mustn't be empty string.")));

        assertDefaultRepositoryData();
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

        assertDefaultRepositoryData();
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

        assertDefaultRepositoryData();
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

        assertDefaultRepositoryData();
    }

    @Override
    protected CatalogParentFacade<Show> getCatalogParentFacade() {
        return showFacade;
    }

    @Override
    protected Integer getDefaultDataCount() {
        return ShowUtils.SHOWS_COUNT;
    }

    @Override
    protected Integer getRepositoryDataCount() {
        return ShowUtils.getShowsCount(entityManager);
    }

    @Override
    protected List<cz.vhromada.catalog.domain.Show> getDataList() {
        return ShowUtils.getShows();
    }

    @Override
    protected cz.vhromada.catalog.domain.Show getDomainData(final Integer index) {
        return ShowUtils.getShow(index);
    }

    @Override
    protected Show newData(final Integer id) {
        final Show show = ShowUtils.newShow(id);
        if (id == null || Integer.MAX_VALUE == id) {
            show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1)));
        }

        return show;
    }

    @Override
    protected cz.vhromada.catalog.domain.Show newDomainData(final Integer id) {
        return ShowUtils.newShowDomain(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Show getRepositoryData(final Integer id) {
        return ShowUtils.getShow(entityManager, id);
    }

    @Override
    protected String getName() {
        return "Show";
    }

    @Override
    protected void clearReferencedData() {
    }

    @Override
    protected void assertDataListDeepEquals(final List<Show> expected, final List<cz.vhromada.catalog.domain.Show> actual) {
        ShowUtils.assertShowListDeepEquals(expected, actual);
    }

    @Override
    protected void assertDataDeepEquals(final Show expected, final cz.vhromada.catalog.domain.Show actual) {
        ShowUtils.assertShowDeepEquals(expected, actual);
    }

    @Override
    protected void assertDataDomainDeepEquals(final cz.vhromada.catalog.domain.Show expected, final cz.vhromada.catalog.domain.Show actual) {
        ShowUtils.assertShowDeepEquals(expected, actual);
    }

    @Override
    protected void assertDefaultRepositoryData() {
        super.assertDefaultRepositoryData();

        assertReferences();
    }

    @Override
    protected void assertNewRepositoryData() {
        super.assertNewRepositoryData();

        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(0));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(0));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    @Override
    protected void assertAddRepositoryData() {
        super.assertAddRepositoryData();

        assertReferences();
    }

    @Override
    protected void assertUpdateRepositoryData() {
        super.assertUpdateRepositoryData();

        assertReferences();
    }

    @Override
    protected void assertRemoveRepositoryData() {
        super.assertRemoveRepositoryData();

        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT - SeasonUtils.SEASONS_PER_SHOW_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT - EpisodeUtils.EPISODES_PER_SHOW_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    @Override
    protected void assertDuplicateRepositoryData() {
        super.assertDuplicateRepositoryData();

        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT + SeasonUtils.SEASONS_PER_SHOW_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT + EpisodeUtils.EPISODES_PER_SHOW_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    @Override
    protected Show getUpdateData(final Integer id) {
        final Show show = super.getUpdateData(id);
        show.setGenres(CollectionUtils.newList(GenreUtils.getGenre(4)));

        return show;
    }

    @Override
    protected cz.vhromada.catalog.domain.Show getExpectedAddData() {
        final cz.vhromada.catalog.domain.Show show = super.getExpectedAddData();
        show.setGenres(CollectionUtils.newList(GenreUtils.getGenreDomain(1)));

        return show;
    }

    @Override
    protected cz.vhromada.catalog.domain.Show getExpectedDuplicatedData() {
        final cz.vhromada.catalog.domain.Show show = super.getExpectedDuplicatedData();
        for (final Season season : show.getSeasons()) {
            final int index = show.getSeasons().indexOf(season);
            season.setId(SeasonUtils.SEASONS_COUNT + index + 1);
            for (final Episode episode : season.getEpisodes()) {
                episode.setId(EpisodeUtils.EPISODES_COUNT + EpisodeUtils.EPISODES_PER_SEASON_COUNT * index + season.getEpisodes().indexOf(episode) + 1);
            }
        }

        return show;
    }

    /**
     * Asserts references.
     */
    private void assertReferences() {
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

}
