package cz.vhromada.catalog.facade.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Episode;
import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.facade.EpisodeFacade;
import cz.vhromada.catalog.utils.EpisodeUtils;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.catalog.utils.SeasonUtils;
import cz.vhromada.catalog.utils.ShowUtils;
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
 * A class represents integration test for class {@link EpisodeFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
@DirtiesContext
public class EpisodeFacadeImplIntegrationTest {

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
     * Event for null episode
     */
    private static final Event NULL_EPISODE_EVENT = new Event(Severity.ERROR, "EPISODE_NULL", "Episode mustn't be null.");

    /**
     * Event for episode with null ID
     */
    private static final Event NULL_EPISODE_ID_EVENT = new Event(Severity.ERROR, "EPISODE_ID_NULL", "ID mustn't be null.");

    /**
     * Event for not existing episode
     */
    private static final Event NOT_EXIST_EPISODE_EVENT = new Event(Severity.ERROR, "EPISODE_NOT_EXIST", "Episode doesn't exist.");

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private EntityManager entityManager;

    /**
     * Instance of (@link EpisodeFacade}
     */
    @Autowired
    private EpisodeFacade episodeFacade;

    /**
     * Test method for {@link EpisodeFacade#get(Integer)}.
     */
    @Test
    public void get() {
        for (int i = 1; i <= EpisodeUtils.EPISODES_COUNT; i++) {
            final Result<Episode> result = episodeFacade.get(i);

            assertThat(result, is(notNullValue()));
            assertThat(result.getEvents(), is(notNullValue()));
            assertThat(result.getStatus(), is(Status.OK));
            assertThat(result.getEvents().isEmpty(), is(true));
            EpisodeUtils.assertEpisodeDeepEquals(result.getData(), EpisodeUtils.getEpisode(i));
        }

        final Result<Episode> result = episodeFacade.get(Integer.MAX_VALUE);

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
     * Test method for {@link EpisodeFacade#get(Integer)} with null episode.
     */
    @Test
    public void get_NullEpisode() {
        final Result<Episode> result = episodeFacade.get(null);

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
     * Test method for {@link EpisodeFacade#add(Season, Episode)}.
     */
    @Test
    @DirtiesContext
    public void add() {
        final cz.vhromada.catalog.domain.Episode expectedEpisode = EpisodeUtils.newEpisodeDomain(EpisodeUtils.EPISODES_COUNT + 1);
        expectedEpisode.setPosition(Integer.MAX_VALUE);

        final Result<Void> result = episodeFacade.add(SeasonUtils.newSeason(1), EpisodeUtils.newEpisode(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        final cz.vhromada.catalog.domain.Episode addedEpisode = EpisodeUtils.getEpisode(entityManager, EpisodeUtils.EPISODES_COUNT + 1);
        EpisodeUtils.assertEpisodeDeepEquals(expectedEpisode, addedEpisode);
        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT + 1));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with null season.
     */
    @Test
    public void add_NullSeason() {
        final Result<Void> result = episodeFacade.add(null, EpisodeUtils.newEpisode(null));

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
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with season with null ID.
     */
    @Test
    public void add_NullId() {
        final Result<Void> result = episodeFacade.add(SeasonUtils.newSeason(null), EpisodeUtils.newEpisode(null));

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
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with episode with not existing season.
     */
    @Test
    public void add_NotExistingSeason() {
        final Result<Void> result = episodeFacade.add(SeasonUtils.newSeason(Integer.MAX_VALUE), EpisodeUtils.newEpisode(null));

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
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with null episode.
     */
    @Test
    public void add_NullEpisode() {
        final Result<Void> result = episodeFacade.add(SeasonUtils.newSeason(1), null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_EPISODE_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with episode with null ID.
     */
    @Test
    public void add_NotNullId() {
        final Result<Void> result = episodeFacade.add(SeasonUtils.newSeason(1), EpisodeUtils.newEpisode(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_ID_NOT_NULL", "ID must be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with episode with not positive number of episode.
     */
    @Test
    public void add_NotPositiveNumber() {
        final Episode episode = EpisodeUtils.newEpisode(null);
        episode.setNumber(0);

        final Result<Void> result = episodeFacade.add(SeasonUtils.newSeason(1), episode);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_NUMBER_NOT_POSITIVE", "Number of episode must be positive number.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with episode with null name.
     */
    @Test
    public void add_NullName() {
        final Episode episode = EpisodeUtils.newEpisode(null);
        episode.setName(null);

        final Result<Void> result = episodeFacade.add(SeasonUtils.newSeason(1), episode);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_NAME_NULL", "Name mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with episode with empty string as name.
     */
    @Test
    public void add_EmptyName() {
        final Episode episode = EpisodeUtils.newEpisode(null);
        episode.setName("");

        final Result<Void> result = episodeFacade.add(SeasonUtils.newSeason(1), episode);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_NAME_EMPTY", "Name mustn't be empty string.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with episode with negative length.
     */
    @Test
    public void add_NegativeLength() {
        final Episode episode = EpisodeUtils.newEpisode(null);
        episode.setLength(-1);

        final Result<Void> result = episodeFacade.add(SeasonUtils.newSeason(1), episode);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_LENGTH_NEGATIVE", "Length of episode mustn't be negative number.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with episode with null note.
     */
    @Test
    public void add_NullNote() {
        final Episode episode = EpisodeUtils.newEpisode(null);
        episode.setNote(null);

        final Result<Void> result = episodeFacade.add(SeasonUtils.newSeason(1), episode);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_NOTE_NULL", "Note mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)}.
     */
    @Test
    @DirtiesContext
    public void update() {
        final Episode episode = EpisodeUtils.newEpisode(1);

        final Result<Void> result = episodeFacade.update(episode);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        final cz.vhromada.catalog.domain.Episode updatedEpisode = EpisodeUtils.getEpisode(entityManager, 1);
        EpisodeUtils.assertEpisodeDeepEquals(episode, updatedEpisode);
        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)} with null episode.
     */
    @Test
    public void update_NullEpisode() {
        final Result<Void> result = episodeFacade.update(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_EPISODE_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)} with episode with null ID.
     */
    @Test
    public void update_NullId() {
        final Result<Void> result = episodeFacade.update(EpisodeUtils.newEpisode(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_EPISODE_ID_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)} with episode with not positive number of episode.
     */
    @Test
    public void update_NotPositiveNumber() {
        final Episode episode = EpisodeUtils.newEpisode(1);
        episode.setNumber(0);

        final Result<Void> result = episodeFacade.update(episode);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_NUMBER_NOT_POSITIVE", "Number of episode must be positive number.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)} with episode with null name.
     */
    @Test
    public void update_NullName() {
        final Episode episode = EpisodeUtils.newEpisode(1);
        episode.setName(null);

        final Result<Void> result = episodeFacade.update(episode);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_NAME_NULL", "Name mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)} with episode with empty string as name.
     */
    @Test
    public void update_EmptyName() {
        final Episode episode = EpisodeUtils.newEpisode(1);
        episode.setName("");

        final Result<Void> result = episodeFacade.update(episode);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_NAME_EMPTY", "Name mustn't be empty string.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)} with episode with negative length.
     */
    @Test
    public void update_NegativeLength() {
        final Episode episode = EpisodeUtils.newEpisode(1);
        episode.setLength(-1);

        final Result<Void> result = episodeFacade.update(episode);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_LENGTH_NEGATIVE", "Length of episode mustn't be negative number.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)} with episode with null note.
     */
    @Test
    public void update_NullNote() {
        final Episode episode = EpisodeUtils.newEpisode(1);
        episode.setNote(null);

        final Result<Void> result = episodeFacade.update(episode);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_NOTE_NULL", "Note mustn't be null.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)} with bad ID.
     */
    @Test
    public void update_BadId() {
        final Result<Void> result = episodeFacade.update(EpisodeUtils.newEpisode(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_EPISODE_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#remove(Episode)}.
     */
    @Test
    @DirtiesContext
    public void remove() {
        final Result<Void> result = episodeFacade.remove(EpisodeUtils.newEpisode(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(EpisodeUtils.getEpisode(entityManager, 1), is(nullValue()));
        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT - 1));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#remove(Episode)} with null episode.
     */
    @Test
    public void remove_NullEpisode() {
        final Result<Void> result = episodeFacade.remove(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_EPISODE_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#remove(Episode)} with episode with null ID.
     */
    @Test
    public void remove_NullId() {
        final Result<Void> result = episodeFacade.remove(EpisodeUtils.newEpisode(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_EPISODE_ID_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#remove(Episode)} with episode with bad ID.
     */
    @Test
    public void remove_BadId() {
        final Result<Void> result = episodeFacade.remove(EpisodeUtils.newEpisode(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_EPISODE_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#duplicate(Episode)}.
     */
    @Test
    @DirtiesContext
    public void duplicate() {
        final cz.vhromada.catalog.domain.Episode episode = EpisodeUtils.getEpisode(1);
        episode.setId(EpisodeUtils.EPISODES_COUNT + 1);

        final Result<Void> result = episodeFacade.duplicate(EpisodeUtils.newEpisode(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        final cz.vhromada.catalog.domain.Episode duplicatedEpisode = EpisodeUtils.getEpisode(entityManager, EpisodeUtils.EPISODES_COUNT + 1);
        EpisodeUtils.assertEpisodeDeepEquals(episode, duplicatedEpisode);
        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT + 1));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#duplicate(Episode)} with null episode.
     */
    @Test
    public void duplicate_NullEpisode() {
        final Result<Void> result = episodeFacade.duplicate(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_EPISODE_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#duplicate(Episode)} with episode with null ID.
     */
    @Test
    public void duplicate_NullId() {
        final Result<Void> result = episodeFacade.duplicate(EpisodeUtils.newEpisode(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_EPISODE_ID_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#duplicate(Episode)} with episode with bad ID.
     */
    @Test
    public void duplicate_BadId() {
        final Result<Void> result = episodeFacade.duplicate(EpisodeUtils.newEpisode(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_EPISODE_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(Episode)}.
     */
    @Test
    @DirtiesContext
    public void moveUp() {
        final cz.vhromada.catalog.domain.Episode episode1 = EpisodeUtils.getEpisode(1);
        episode1.setPosition(1);
        final cz.vhromada.catalog.domain.Episode episode2 = EpisodeUtils.getEpisode(2);
        episode2.setPosition(0);

        final Result<Void> result = episodeFacade.moveUp(EpisodeUtils.newEpisode(2));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        EpisodeUtils.assertEpisodeDeepEquals(episode1, EpisodeUtils.getEpisode(entityManager, 1));
        EpisodeUtils.assertEpisodeDeepEquals(episode2, EpisodeUtils.getEpisode(entityManager, 2));
        for (int i = 3; i <= EpisodeUtils.EPISODES_COUNT; i++) {
            EpisodeUtils.assertEpisodeDeepEquals(EpisodeUtils.getEpisode(i), EpisodeUtils.getEpisode(entityManager, i));
        }
        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(Episode)} with null episode.
     */
    @Test
    public void moveUp_NullEpisode() {
        final Result<Void> result = episodeFacade.moveUp(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_EPISODE_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(Episode)} with episode with null ID.
     */
    @Test
    public void moveUp_NullId() {
        final Result<Void> result = episodeFacade.moveUp(EpisodeUtils.newEpisode(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_EPISODE_ID_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(Episode)} with not movable episode.
     */
    @Test
    public void moveUp_NotMovableEpisode() {
        final Result<Void> result = episodeFacade.moveUp(EpisodeUtils.newEpisode(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_NOT_MOVABLE", "Episode can't be moved up.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(Episode)} with episode with bad ID.
     */
    @Test
    public void moveUp_BadId() {
        final Result<Void> result = episodeFacade.moveUp(EpisodeUtils.newEpisode(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_EPISODE_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(Episode)}.
     */
    @Test
    @DirtiesContext
    public void moveDown() {
        final cz.vhromada.catalog.domain.Episode episode1 = EpisodeUtils.getEpisode(1);
        episode1.setPosition(1);
        final cz.vhromada.catalog.domain.Episode episode2 = EpisodeUtils.getEpisode(2);
        episode2.setPosition(0);

        final Result<Void> result = episodeFacade.moveDown(EpisodeUtils.newEpisode(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        EpisodeUtils.assertEpisodeDeepEquals(episode1, EpisodeUtils.getEpisode(entityManager, 1));
        EpisodeUtils.assertEpisodeDeepEquals(episode2, EpisodeUtils.getEpisode(entityManager, 2));
        for (int i = 3; i <= EpisodeUtils.EPISODES_COUNT; i++) {
            EpisodeUtils.assertEpisodeDeepEquals(EpisodeUtils.getEpisode(i), EpisodeUtils.getEpisode(entityManager, i));
        }
        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(Episode)} with null episode.
     */
    @Test
    public void moveDown_NullEpisode() {
        final Result<Void> result = episodeFacade.moveDown(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_EPISODE_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(Episode)} with episode with null ID.
     */
    @Test
    public void moveDown_NullId() {
        final Result<Void> result = episodeFacade.moveDown(EpisodeUtils.newEpisode(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_EPISODE_ID_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(Episode)} with not movable episode.
     */
    @Test
    public void moveDown_NotMovableEpisode() {
        final Result<Void> result = episodeFacade.moveDown(EpisodeUtils.newEpisode(EpisodeUtils.EPISODES_COUNT));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_NOT_MOVABLE", "Episode can't be moved down.")));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(Episode)} with episode with bad ID.
     */
    @Test
    public void moveDown_BadId() {
        final Result<Void> result = episodeFacade.moveDown(EpisodeUtils.newEpisode(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_EPISODE_EVENT));

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#find(Season)}.
     */
    @Test
    public void find() {
        for (int i = 0; i < SeasonUtils.SEASONS_COUNT; i++) {
            final int showNumber = i / SeasonUtils.SEASONS_PER_SHOW_COUNT + 1;
            final int seasonNumber = i % SeasonUtils.SEASONS_PER_SHOW_COUNT + 1;

            final Result<List<Episode>> result = episodeFacade.find(SeasonUtils.newSeason(i + 1));

            assertThat(result, is(notNullValue()));
            assertThat(result.getEvents(), is(notNullValue()));
            assertThat(result.getStatus(), is(Status.OK));
            assertThat(result.getEvents().isEmpty(), is(true));
            EpisodeUtils.assertEpisodeListDeepEquals(result.getData(), EpisodeUtils.getEpisodes(showNumber, seasonNumber));
        }

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(SeasonUtils.getSeasonsCount(entityManager), is(SeasonUtils.SEASONS_COUNT));
        assertThat(EpisodeUtils.getEpisodesCount(entityManager), is(EpisodeUtils.EPISODES_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#find(Season)} with null season.
     */
    @Test
    public void find_NullSeason() {
        final Result<List<Episode>> result = episodeFacade.find(null);

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
     * Test method for {@link EpisodeFacade#find(Season)} with season with null ID.
     */
    @Test
    public void find_NullId() {
        final Result<List<Episode>> result = episodeFacade.find(SeasonUtils.newSeason(null));

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
     * Test method for {@link EpisodeFacade#find(Season)} with bad ID.
     */
    @Test
    public void find_BadId() {
        final Result<List<Episode>> result = episodeFacade.find(SeasonUtils.newSeason(Integer.MAX_VALUE));

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

}
