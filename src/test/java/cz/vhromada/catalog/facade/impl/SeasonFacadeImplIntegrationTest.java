package cz.vhromada.catalog.facade.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.common.Language;
import cz.vhromada.catalog.domain.Episode;
import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.catalog.facade.SeasonFacade;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.Constants;
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
 * A class represents integration test for class {@link SeasonFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
@DirtiesContext
public class SeasonFacadeImplIntegrationTest {

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
     * Event for null season
     */
    private static final Event NULL_SEASON_EVENT = new Event(Severity.ERROR, "SEASON_NULL", "Season mustn't be null.");

    /**
     * Event for season with null ID
     */
    private static final Event NULL_SEASON_ID_EVENT = new Event(Severity.ERROR, "SEASON_ID_NULL", "ID mustn't be null.");

    /**
     * Event for not existing season
     */
    private static final Event NOT_EXIST_SEASON_EVENT = new Event(Severity.ERROR, "SEASON_NOT_EXIST", "Season doesn't exist.");

    /**
     * Event for invalid starting year
     */
    private static final Event INVALID_STARTING_YEAR_EVENT = new Event(Severity.ERROR, "SEASON_START_YEAR_NOT_VALID", "Starting year must be between "
            + Constants.MIN_YEAR + " and " + Constants.CURRENT_YEAR + '.');

    /**
     * Event for invalid ending year
     */
    private static final Event INVALID_ENDING_YEAR_EVENT = new Event(Severity.ERROR, "SEASON_END_YEAR_NOT_VALID", "Ending year must be between "
            + Constants.MIN_YEAR + " and " + Constants.CURRENT_YEAR + '.');

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private EntityManager entityManager;

    /**
     * Instance of (@link SeasonFacade}
     */
    @Autowired
    private SeasonFacade seasonFacade;

    /**
     * Test method for {@link SeasonFacade#get(Integer)}.
     */
    @Test
    public void get() {
        for (int i = 1; i <= SeasonUtils.SEASONS_COUNT; i++) {
            final Result<Season> result = seasonFacade.get(i);

            assertThat(result, is(notNullValue()));
            assertThat(result.getEvents(), is(notNullValue()));
            assertThat(result.getStatus(), is(Status.OK));
            assertThat(result.getEvents().isEmpty(), is(true));
            SeasonUtils.assertSeasonDeepEquals(result.getData(), SeasonUtils.getSeason(i));
        }

        final Result<Season> result = seasonFacade.get(Integer.MAX_VALUE);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(nullValue()));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#get(Integer)} with null season.
     */
    @Test
    public void get_NullSeason() {
        final Result<Season> result = seasonFacade.get(null);

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
     * Test method for {@link SeasonFacade#add(Show, Season)}.
     */
    @Test
    @DirtiesContext
    public void add() {
        final cz.vhromada.catalog.domain.Season expectedSeason = SeasonUtils.newSeasonDomain(SeasonUtils.SEASONS_COUNT + 1);
        expectedSeason.setPosition(Integer.MAX_VALUE);

        final Result<Void> result = seasonFacade.add(ShowUtils.newShow(1), SeasonUtils.newSeason(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        final cz.vhromada.catalog.domain.Season addedSeason = SeasonUtils.getSeason(entityManager, SeasonUtils.SEASONS_COUNT + 1);
        SeasonUtils.assertSeasonDeepEquals(expectedSeason, addedSeason);
        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT + 1));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with null show.
     */
    @Test
    public void add_NullShow() {
        final Result<Void> result = seasonFacade.add(null, SeasonUtils.newSeason(null));

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
     * Test method for {@link SeasonFacade#add(Show, Season)} with show with null ID.
     */
    @Test
    public void add_NullId() {
        final Result<Void> result = seasonFacade.add(ShowUtils.newShow(null), SeasonUtils.newSeason(null));

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
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with not existing show.
     */
    @Test
    public void add_NotExistingShow() {
        final Result<Void> result = seasonFacade.add(ShowUtils.newShow(Integer.MAX_VALUE), SeasonUtils.newSeason(null));

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
     * Test method for {@link SeasonFacade#add(Show, Season)} with null season.
     */
    @Test
    public void add_NullSeason() {
        final Result<Void> result = seasonFacade.add(ShowUtils.newShow(1), null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SEASON_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with null ID.
     */
    @Test
    public void add_NotNullId() {
        final Result<Void> result = seasonFacade.add(ShowUtils.newShow(1), SeasonUtils.newSeason(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SEASON_ID_NOT_NULL", "ID must be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with not positive number of season.
     */
    @Test
    public void add_NotPositiveNumber() {
        final Season season = SeasonUtils.newSeason(null);
        season.setNumber(0);

        final Result<Void> result = seasonFacade.add(ShowUtils.newShow(1), season);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SEASON_NUMBER_NOT_POSITIVE", "Number of season must be positive number.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with bad minimum starting year and bad minimum ending year.
     */
    @Test
    public void add_BadMinimumYears() {
        final Season season = SeasonUtils.newSeason(null);
        season.setStartYear(TestConstants.BAD_MIN_YEAR);
        season.setEndYear(TestConstants.BAD_MIN_YEAR);

        final Result<Void> result = seasonFacade.add(ShowUtils.newShow(1), season);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(2));
        assertThat(result.getEvents().get(0), is(INVALID_STARTING_YEAR_EVENT));
        assertThat(result.getEvents().get(1), is(INVALID_ENDING_YEAR_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with bad maximum starting year and bad maximum ending year.
     */
    @Test
    public void add_BadMaximumYears() {
        final Season season = SeasonUtils.newSeason(null);
        season.setStartYear(TestConstants.BAD_MAX_YEAR);
        season.setEndYear(TestConstants.BAD_MAX_YEAR);

        final Result<Void> result = seasonFacade.add(ShowUtils.newShow(1), season);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(2));
        assertThat(result.getEvents().get(0), is(INVALID_STARTING_YEAR_EVENT));
        assertThat(result.getEvents().get(1), is(INVALID_ENDING_YEAR_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with starting year greater than ending year.
     */
    @Test
    public void add_BadYears() {
        final Season season = SeasonUtils.newSeason(null);
        season.setStartYear(season.getEndYear() + 1);

        final Result<Void> result = seasonFacade.add(ShowUtils.newShow(1), season);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SEASON_YEARS_NOT_VALID", "Starting year mustn't be greater than ending year.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with null language.
     */
    @Test
    public void add_NullLanguage() {
        final Season season = SeasonUtils.newSeason(null);
        season.setLanguage(null);

        final Result<Void> result = seasonFacade.add(ShowUtils.newShow(1), season);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SEASON_LANGUAGE_NULL", "Language mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with null subtitles.
     */
    @Test
    public void add_NullSubtitles() {
        final Season season = SeasonUtils.newSeason(null);
        season.setSubtitles(null);

        final Result<Void> result = seasonFacade.add(ShowUtils.newShow(1), season);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SEASON_SUBTITLES_NULL", "Subtitles mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with subtitles with null value.
     */
    @Test
    public void add_BadSubtitles() {
        final Season season = SeasonUtils.newSeason(null);
        season.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        final Result<Void> result = seasonFacade.add(ShowUtils.newShow(1), season);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SEASON_SUBTITLES_CONTAIN_NULL", "Subtitles mustn't contain null value.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with null note.
     */
    @Test
    public void add_NullNote() {
        final Season season = SeasonUtils.newSeason(null);
        season.setNote(null);

        final Result<Void> result = seasonFacade.add(ShowUtils.newShow(1), season);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SEASON_NOTE_NULL", "Note mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)}.
     */
    @Test
    @DirtiesContext
    public void update() {
        final Season season = SeasonUtils.newSeason(1);

        final Result<Void> result = seasonFacade.update(season);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        final cz.vhromada.catalog.domain.Season updatedSeason = SeasonUtils.getSeason(entityManager, 1);
        SeasonUtils.assertSeasonDeepEquals(season, updatedSeason);
        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with null season.
     */
    @Test
    public void update_NullSeason() {
        final Result<Void> result = seasonFacade.update(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SEASON_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with null ID.
     */
    @Test
    public void update_NullId() {
        final Result<Void> result = seasonFacade.update(SeasonUtils.newSeason(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SEASON_ID_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with not positive number of season.
     */
    @Test
    public void update_NotPositiveNumber() {
        final Season season = SeasonUtils.newSeason(1);
        season.setNumber(0);

        final Result<Void> result = seasonFacade.update(season);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SEASON_NUMBER_NOT_POSITIVE", "Number of season must be positive number.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with bad minimum starting year and bad minimum ending year.
     */
    @Test
    public void update_BadMinimumYears() {
        final Season season = SeasonUtils.newSeason(1);
        season.setStartYear(TestConstants.BAD_MIN_YEAR);
        season.setEndYear(TestConstants.BAD_MIN_YEAR);

        final Result<Void> result = seasonFacade.update(season);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(2));
        assertThat(result.getEvents().get(0), is(INVALID_STARTING_YEAR_EVENT));
        assertThat(result.getEvents().get(1), is(INVALID_ENDING_YEAR_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with bad maximum starting year and bad maximum ending year.
     */
    @Test
    public void update_BadMaximumYears() {
        final Season season = SeasonUtils.newSeason(1);
        season.setStartYear(TestConstants.BAD_MAX_YEAR);
        season.setEndYear(TestConstants.BAD_MAX_YEAR);

        final Result<Void> result = seasonFacade.update(season);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(2));
        assertThat(result.getEvents().get(0), is(INVALID_STARTING_YEAR_EVENT));
        assertThat(result.getEvents().get(1), is(INVALID_ENDING_YEAR_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with starting year greater than ending year.
     */
    @Test
    public void update_BadYears() {
        final Season season = SeasonUtils.newSeason(1);
        season.setStartYear(season.getEndYear() + 1);

        final Result<Void> result = seasonFacade.update(season);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SEASON_YEARS_NOT_VALID", "Starting year mustn't be greater than ending year.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with null language.
     */
    @Test
    public void update_NullLanguage() {
        final Season season = SeasonUtils.newSeason(1);
        season.setLanguage(null);

        final Result<Void> result = seasonFacade.update(season);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SEASON_LANGUAGE_NULL", "Language mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with null subtitles.
     */
    @Test
    public void update_NullSubtitles() {
        final Season season = SeasonUtils.newSeason(1);
        season.setSubtitles(null);

        final Result<Void> result = seasonFacade.update(season);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SEASON_SUBTITLES_NULL", "Subtitles mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with subtitles with null value.
     */
    @Test
    public void update_BadSubtitles() {
        final Season season = SeasonUtils.newSeason(1);
        season.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        final Result<Void> result = seasonFacade.update(season);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SEASON_SUBTITLES_CONTAIN_NULL", "Subtitles mustn't contain null value.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with null note.
     */
    @Test
    public void update_NullNote() {
        final Season season = SeasonUtils.newSeason(1);
        season.setNote(null);

        final Result<Void> result = seasonFacade.update(season);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SEASON_NOTE_NULL", "Note mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with bad ID.
     */
    @Test
    public void update_BadId() {
        final Result<Void> result = seasonFacade.update(SeasonUtils.newSeason(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_SEASON_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#remove(Season)}.
     */
    @Test
    @DirtiesContext
    public void remove() {
        final Result<Void> result = seasonFacade.remove(SeasonUtils.newSeason(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(SeasonUtils.getSeason(entityManager, 1), is(nullValue()));
        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT - 1));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT - EpisodeUtils.EPISODES_PER_SEASON_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#remove(Season)} with null season.
     */
    @Test
    public void remove_NullSeason() {
        final Result<Void> result = seasonFacade.remove(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SEASON_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#remove(Season)} with season with null ID.
     */
    @Test
    public void remove_NullId() {
        final Result<Void> result = seasonFacade.remove(SeasonUtils.newSeason(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SEASON_ID_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#remove(Season)} with season with bad ID.
     */
    @Test
    public void remove_BadId() {
        final Result<Void> result = seasonFacade.remove(SeasonUtils.newSeason(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_SEASON_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#duplicate(Season)}.
     */
    @Test
    @DirtiesContext
    public void duplicate() {
        final cz.vhromada.catalog.domain.Season season = SeasonUtils.getSeason(1);
        season.setId(SeasonUtils.SEASONS_COUNT + 1);
        for (final Episode episode : season.getEpisodes()) {
            episode.setId(EpisodeUtils.EPISODES_COUNT + season.getEpisodes().indexOf(episode) + 1);
        }

        final Result<Void> result = seasonFacade.duplicate(SeasonUtils.newSeason(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        final cz.vhromada.catalog.domain.Season duplicatedSeason = SeasonUtils.getSeason(entityManager, SeasonUtils.SEASONS_COUNT + 1);
        SeasonUtils.assertSeasonDeepEquals(season, duplicatedSeason);
        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT + 1));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT + EpisodeUtils.EPISODES_PER_SEASON_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#duplicate(Season)} with null season.
     */
    @Test
    public void duplicate_NullSeason() {
        final Result<Void> result = seasonFacade.duplicate(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SEASON_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#duplicate(Season)} with season with null ID.
     */
    @Test
    public void duplicate_NullId() {
        final Result<Void> result = seasonFacade.duplicate(SeasonUtils.newSeason(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SEASON_ID_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#duplicate(Season)} with season with bad ID.
     */
    @Test
    public void duplicate_BadId() {
        final Result<Void> result = seasonFacade.duplicate(SeasonUtils.newSeason(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_SEASON_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#moveUp(Season)}.
     */
    @Test
    @DirtiesContext
    public void moveUp() {
        final cz.vhromada.catalog.domain.Season season1 = SeasonUtils.getSeason(1, 1);
        season1.setPosition(1);
        final cz.vhromada.catalog.domain.Season season2 = SeasonUtils.getSeason(1, 2);
        season2.setPosition(0);

        final Result<Void> result = seasonFacade.moveUp(SeasonUtils.newSeason(2));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        SeasonUtils.assertSeasonDeepEquals(season1, SeasonUtils.getSeason(entityManager, 1));
        SeasonUtils.assertSeasonDeepEquals(season2, SeasonUtils.getSeason(entityManager, 2));
        for (int i = 3; i <= SeasonUtils.SEASONS_COUNT; i++) {
            SeasonUtils.assertSeasonDeepEquals(SeasonUtils.getSeason(i), SeasonUtils.getSeason(entityManager, i));
        }
        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#moveUp(Season)} with null season.
     */
    @Test
    public void moveUp_NullSeason() {
        final Result<Void> result = seasonFacade.moveUp(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SEASON_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#moveUp(Season)} with season with null ID.
     */
    @Test
    public void moveUp_NullId() {
        final Result<Void> result = seasonFacade.moveUp(SeasonUtils.newSeason(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SEASON_ID_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#moveUp(Season)} with not movable season.
     */
    @Test
    public void moveUp_NotMovableSeason() {
        final Result<Void> result = seasonFacade.moveUp(SeasonUtils.newSeason(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SEASON_NOT_MOVABLE", "Season can't be moved up.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#moveUp(Season)} with season with bad ID.
     */
    @Test
    public void moveUp_BadId() {
        final Result<Void> result = seasonFacade.moveUp(SeasonUtils.newSeason(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_SEASON_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#moveDown(Season)}.
     */
    @Test
    @DirtiesContext
    public void moveDown() {
        final cz.vhromada.catalog.domain.Season season1 = SeasonUtils.getSeason(1, 1);
        season1.setPosition(1);
        final cz.vhromada.catalog.domain.Season season2 = SeasonUtils.getSeason(1, 2);
        season2.setPosition(0);

        final Result<Void> result = seasonFacade.moveDown(SeasonUtils.newSeason(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        SeasonUtils.assertSeasonDeepEquals(season1, SeasonUtils.getSeason(entityManager, 1));
        SeasonUtils.assertSeasonDeepEquals(season2, SeasonUtils.getSeason(entityManager, 2));
        for (int i = 3; i <= SeasonUtils.SEASONS_COUNT; i++) {
            SeasonUtils.assertSeasonDeepEquals(SeasonUtils.getSeason(i), SeasonUtils.getSeason(entityManager, i));
        }
        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#moveDown(Season)} with null season.
     */
    @Test
    public void moveDown_NullSeason() {
        final Result<Void> result = seasonFacade.moveDown(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SEASON_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#moveDown(Season)} with season with null ID.
     */
    @Test
    public void moveDown_NullId() {
        final Result<Void> result = seasonFacade.moveDown(SeasonUtils.newSeason(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SEASON_ID_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#moveDown(Season)} with not movable season.
     */
    @Test
    public void moveDown_NotMovableSeason() {
        final Result<Void> result = seasonFacade.moveDown(SeasonUtils.newSeason(SeasonUtils.SEASONS_COUNT));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SEASON_NOT_MOVABLE", "Season can't be moved down.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#moveDown(Season)} with season with bad ID.
     */
    @Test
    public void moveDown_BadId() {
        final Result<Void> result = seasonFacade.moveDown(SeasonUtils.newSeason(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_SEASON_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#find(Show)}.
     */
    @Test
    public void find() {
        for (int i = 1; i <= ShowUtils.SHOWS_COUNT; i++) {
            final Result<List<Season>> result = seasonFacade.find(ShowUtils.newShow(i));

            assertThat(result, is(notNullValue()));
            assertThat(result.getEvents(), is(notNullValue()));
            assertThat(result.getStatus(), is(Status.OK));
            assertThat(result.getEvents().isEmpty(), is(true));
            SeasonUtils.assertSeasonListDeepEquals(result.getData(), SeasonUtils.getSeasons(i));
        }

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#find(Show)} with null show.
     */
    @Test
    public void find_NullShow() {
        final Result<List<Season>> result = seasonFacade.find(null);

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
     * Test method for {@link SeasonFacade#find(Show)} with show with null ID.
     */
    @Test
    public void find_NullId() {
        final Result<List<Season>> result = seasonFacade.find(ShowUtils.newShow(null));

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
     * Test method for {@link SeasonFacade#find(Show)} with bad ID.
     */
    @Test
    public void find_BadId() {
        final Result<List<Season>> result = seasonFacade.find(ShowUtils.newShow(Integer.MAX_VALUE));

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

}
