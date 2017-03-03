package cz.vhromada.catalog.facade.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.entity.Episode;
import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.facade.CatalogChildFacade;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * A class represents integration test for class {@link EpisodeFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
public class EpisodeFacadeImplIntegrationTest extends AbstractChildFacadeIntegrationTest<Episode, cz.vhromada.catalog.domain.Episode, Season> {

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
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with episode with not positive number of episode.
     */
    @Test
    public void add_NotPositiveNumber() {
        final Episode episode = newChildData(null);
        episode.setNumber(0);

        final Result<Void> result = episodeFacade.add(SeasonUtils.newSeason(1), episode);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_NUMBER_NOT_POSITIVE", "Number of episode must be positive number.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with episode with null name.
     */
    @Test
    public void add_NullName() {
        final Episode episode = newChildData(null);
        episode.setName(null);

        final Result<Void> result = episodeFacade.add(SeasonUtils.newSeason(1), episode);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_NAME_NULL", "Name mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with episode with empty string as name.
     */
    @Test
    public void add_EmptyName() {
        final Episode episode = newChildData(null);
        episode.setName("");

        final Result<Void> result = episodeFacade.add(SeasonUtils.newSeason(1), episode);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_NAME_EMPTY", "Name mustn't be empty string.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with episode with negative length.
     */
    @Test
    public void add_NegativeLength() {
        final Episode episode = newChildData(null);
        episode.setLength(-1);

        final Result<Void> result = episodeFacade.add(SeasonUtils.newSeason(1), episode);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_LENGTH_NEGATIVE", "Length of episode mustn't be negative number.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with episode with null note.
     */
    @Test
    public void add_NullNote() {
        final Episode episode = newChildData(null);
        episode.setNote(null);

        final Result<Void> result = episodeFacade.add(SeasonUtils.newSeason(1), episode);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_NOTE_NULL", "Note mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)} with episode with not positive number of episode.
     */
    @Test
    public void update_NotPositiveNumber() {
        final Episode episode = newChildData(1);
        episode.setNumber(0);

        final Result<Void> result = episodeFacade.update(episode);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_NUMBER_NOT_POSITIVE", "Number of episode must be positive number.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)} with episode with null name.
     */
    @Test
    public void update_NullName() {
        final Episode episode = newChildData(1);
        episode.setName(null);

        final Result<Void> result = episodeFacade.update(episode);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_NAME_NULL", "Name mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)} with episode with empty string as name.
     */
    @Test
    public void update_EmptyName() {
        final Episode episode = newChildData(1);
        episode.setName("");

        final Result<Void> result = episodeFacade.update(episode);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_NAME_EMPTY", "Name mustn't be empty string.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)} with episode with negative length.
     */
    @Test
    public void update_NegativeLength() {
        final Episode episode = newChildData(1);
        episode.setLength(-1);

        final Result<Void> result = episodeFacade.update(episode);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_LENGTH_NEGATIVE", "Length of episode mustn't be negative number.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)} with episode with null note.
     */
    @Test
    public void update_NullNote() {
        final Episode episode = newChildData(1);
        episode.setNote(null);

        final Result<Void> result = episodeFacade.update(episode);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_NOTE_NULL", "Note mustn't be null.")));

        assertDefaultRepositoryData();
    }

    @Override
    protected CatalogChildFacade<Episode, Season> getCatalogChildFacade() {
        return episodeFacade;
    }

    @Override
    protected Integer getDefaultParentDataCount() {
        return SeasonUtils.SEASONS_COUNT;
    }

    @Override
    protected Integer getDefaultChildDataCount() {
        return EpisodeUtils.EPISODES_COUNT;
    }

    @Override
    protected Integer getRepositoryParentDataCount() {
        return SeasonUtils.getSeasonsCount(entityManager);
    }

    @Override
    protected Integer getRepositoryChildDataCount() {
        return EpisodeUtils.getEpisodesCount(entityManager);
    }

    @Override
    protected List<cz.vhromada.catalog.domain.Episode> getDataList(final Integer parentId) {
        final int showNumber = (parentId - 1) / SeasonUtils.SEASONS_PER_SHOW_COUNT + 1;
        final int seasonNumber = (parentId - 1) % SeasonUtils.SEASONS_PER_SHOW_COUNT + 1;

        return EpisodeUtils.getEpisodes(showNumber, seasonNumber);
    }

    @Override
    protected cz.vhromada.catalog.domain.Episode getDomainData(final Integer index) {
        return EpisodeUtils.getEpisode(index);
    }

    @Override
    protected Season newParentData(final Integer id) {
        return SeasonUtils.newSeason(id);
    }

    @Override
    protected Episode newChildData(final Integer id) {
        return EpisodeUtils.newEpisode(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Episode newDomainData(final Integer id) {
        return EpisodeUtils.newEpisodeDomain(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Episode getRepositoryData(final Integer id) {
        return EpisodeUtils.getEpisode(entityManager, id);
    }

    @Override
    protected String getParentName() {
        return "Season";
    }

    @Override
    protected String getChildName() {
        return "Episode";
    }

    @Override
    protected void assertDataListDeepEquals(final List<Episode> expected, final List<cz.vhromada.catalog.domain.Episode> actual) {
        EpisodeUtils.assertEpisodeListDeepEquals(expected, actual);
    }

    @Override
    protected void assertDataDeepEquals(final Episode expected, final cz.vhromada.catalog.domain.Episode actual) {
        EpisodeUtils.assertEpisodeDeepEquals(expected, actual);

    }

    @Override
    protected void assertDataDomainDeepEquals(final cz.vhromada.catalog.domain.Episode expected, final cz.vhromada.catalog.domain.Episode actual) {
        EpisodeUtils.assertEpisodeDeepEquals(expected, actual);
    }

    @Override
    protected void assertReferences() {
        super.assertReferences();

        assertThat(ShowUtils.getShowsCount(entityManager), is(ShowUtils.SHOWS_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

}
