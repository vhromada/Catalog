package cz.vhromada.catalog.facade.impl;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.common.Language;
import cz.vhromada.catalog.domain.Episode;
import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.catalog.facade.CatalogChildFacade;
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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;

/**
 * A class represents integration test for class {@link SeasonFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@DirtiesContext
class SeasonFacadeImplIntegrationTest extends AbstractChildFacadeIntegrationTest<Season, cz.vhromada.catalog.domain.Season, Show> {

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
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with not positive number of season.
     */
    @Test
    void add_NotPositiveNumber() {
        final Season season = newChildData(null);
        season.setNumber(0);

        final Result<Void> result = seasonFacade.add(ShowUtils.newShow(1), season);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SEASON_NUMBER_NOT_POSITIVE", "Number of season must be positive number.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with bad minimum starting year and bad minimum ending year.
     */
    @Test
    void add_BadMinimumYears() {
        final Season season = newChildData(null);
        season.setStartYear(TestConstants.BAD_MIN_YEAR);
        season.setEndYear(TestConstants.BAD_MIN_YEAR);

        final Result<Void> result = seasonFacade.add(ShowUtils.newShow(1), season);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Arrays.asList(INVALID_STARTING_YEAR_EVENT, INVALID_ENDING_YEAR_EVENT));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with bad maximum starting year and bad maximum ending year.
     */
    @Test
    void add_BadMaximumYears() {
        final Season season = newChildData(null);
        season.setStartYear(TestConstants.BAD_MAX_YEAR);
        season.setEndYear(TestConstants.BAD_MAX_YEAR);

        final Result<Void> result = seasonFacade.add(ShowUtils.newShow(1), season);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Arrays.asList(INVALID_STARTING_YEAR_EVENT, INVALID_ENDING_YEAR_EVENT));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with starting year greater than ending year.
     */
    @Test
    void add_BadYears() {
        final Season season = newChildData(null);
        season.setStartYear(season.getEndYear() + 1);

        final Result<Void> result = seasonFacade.add(ShowUtils.newShow(1), season);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SEASON_YEARS_NOT_VALID",
                "Starting year mustn't be greater than ending year.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with null language.
     */
    @Test
    void add_NullLanguage() {
        final Season season = newChildData(null);
        season.setLanguage(null);

        final Result<Void> result = seasonFacade.add(ShowUtils.newShow(1), season);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SEASON_LANGUAGE_NULL", "Language mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with null subtitles.
     */
    @Test
    void add_NullSubtitles() {
        final Season season = newChildData(null);
        season.setSubtitles(null);

        final Result<Void> result = seasonFacade.add(ShowUtils.newShow(1), season);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SEASON_SUBTITLES_NULL", "Subtitles mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with subtitles with null value.
     */
    @Test
    void add_BadSubtitles() {
        final Season season = newChildData(null);
        season.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        final Result<Void> result = seasonFacade.add(ShowUtils.newShow(1), season);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SEASON_SUBTITLES_CONTAIN_NULL", "Subtitles mustn't contain null value.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with null note.
     */
    @Test
    void add_NullNote() {
        final Season season = newChildData(null);
        season.setNote(null);

        final Result<Void> result = seasonFacade.add(ShowUtils.newShow(1), season);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SEASON_NOTE_NULL", "Note mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with not positive number of season.
     */
    @Test
    void update_NotPositiveNumber() {
        final Season season = newChildData(1);
        season.setNumber(0);

        final Result<Void> result = seasonFacade.update(season);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SEASON_NUMBER_NOT_POSITIVE", "Number of season must be positive number.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with bad minimum starting year and bad minimum ending year.
     */
    @Test
    void update_BadMinimumYears() {
        final Season season = newChildData(1);
        season.setStartYear(TestConstants.BAD_MIN_YEAR);
        season.setEndYear(TestConstants.BAD_MIN_YEAR);

        final Result<Void> result = seasonFacade.update(season);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Arrays.asList(INVALID_STARTING_YEAR_EVENT, INVALID_ENDING_YEAR_EVENT));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with bad maximum starting year and bad maximum ending year.
     */
    @Test
    void update_BadMaximumYears() {
        final Season season = newChildData(1);
        season.setStartYear(TestConstants.BAD_MAX_YEAR);
        season.setEndYear(TestConstants.BAD_MAX_YEAR);

        final Result<Void> result = seasonFacade.update(season);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Arrays.asList(INVALID_STARTING_YEAR_EVENT, INVALID_ENDING_YEAR_EVENT));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with starting year greater than ending year.
     */
    @Test
    void update_BadYears() {
        final Season season = newChildData(1);
        season.setStartYear(season.getEndYear() + 1);

        final Result<Void> result = seasonFacade.update(season);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SEASON_YEARS_NOT_VALID",
                "Starting year mustn't be greater than ending year.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with null language.
     */
    @Test
    void update_NullLanguage() {
        final Season season = newChildData(1);
        season.setLanguage(null);

        final Result<Void> result = seasonFacade.update(season);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SEASON_LANGUAGE_NULL", "Language mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with null subtitles.
     */
    @Test
    void update_NullSubtitles() {
        final Season season = newChildData(1);
        season.setSubtitles(null);

        final Result<Void> result = seasonFacade.update(season);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SEASON_SUBTITLES_NULL", "Subtitles mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with subtitles with null value.
     */
    @Test
    void update_BadSubtitles() {
        final Season season = newChildData(1);
        season.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        final Result<Void> result = seasonFacade.update(season);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SEASON_SUBTITLES_CONTAIN_NULL", "Subtitles mustn't contain null value.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with null note.
     */
    @Test
    void update_NullNote() {
        final Season season = newChildData(1);
        season.setNote(null);

        final Result<Void> result = seasonFacade.update(season);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SEASON_NOTE_NULL", "Note mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    @Override
    protected CatalogChildFacade<Season, Show> getCatalogChildFacade() {
        return seasonFacade;
    }

    @Override
    protected Integer getDefaultParentDataCount() {
        return ShowUtils.SHOWS_COUNT;
    }

    @Override
    protected Integer getDefaultChildDataCount() {
        return SeasonUtils.SEASONS_COUNT;
    }

    @Override
    protected Integer getRepositoryParentDataCount() {
        return ShowUtils.getShowsCount(entityManager);
    }

    @Override
    protected Integer getRepositoryChildDataCount() {
        return SeasonUtils.getSeasonsCount(entityManager);
    }

    @Override
    protected List<cz.vhromada.catalog.domain.Season> getDataList(final Integer parentId) {
        return SeasonUtils.getSeasons(parentId);
    }

    @Override
    protected cz.vhromada.catalog.domain.Season getDomainData(final Integer index) {
        return SeasonUtils.getSeason(index);
    }

    @Override
    protected Show newParentData(final Integer id) {
        return ShowUtils.newShow(id);
    }

    @Override
    protected Season newChildData(final Integer id) {
        return SeasonUtils.newSeason(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Season newDomainData(final Integer id) {
        return SeasonUtils.newSeasonDomain(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Season getRepositoryData(final Integer id) {
        return SeasonUtils.getSeason(entityManager, id);
    }

    @Override
    protected String getParentName() {
        return "Show";
    }

    @Override
    protected String getChildName() {
        return "Season";
    }

    @Override
    protected void assertDataListDeepEquals(final List<Season> expected, final List<cz.vhromada.catalog.domain.Season> actual) {
        SeasonUtils.assertSeasonListDeepEquals(expected, actual);
    }

    @Override
    protected void assertDataDeepEquals(final Season expected, final cz.vhromada.catalog.domain.Season actual) {
        SeasonUtils.assertSeasonDeepEquals(expected, actual);

    }

    @Override
    protected void assertDataDomainDeepEquals(final cz.vhromada.catalog.domain.Season expected, final cz.vhromada.catalog.domain.Season actual) {
        SeasonUtils.assertSeasonDeepEquals(expected, actual);
    }

    @Override
    protected cz.vhromada.catalog.domain.Season getExpectedDuplicatedData() {
        final cz.vhromada.catalog.domain.Season season = super.getExpectedDuplicatedData();
        for (final Episode episode : season.getEpisodes()) {
            episode.setId(EpisodeUtils.EPISODES_COUNT + season.getEpisodes().indexOf(episode) + 1);
        }

        return season;
    }

    @Override
    protected void assertRemoveRepositoryData() {
        assertSoftly(softly -> {
            softly.assertThat(getRepositoryChildDataCount()).isEqualTo(getDefaultChildDataCount() - 1);
            softly.assertThat(getRepositoryParentDataCount()).isEqualTo(getDefaultParentDataCount());
            softly.assertThat(EpisodeUtils.getEpisodesCount(entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT - EpisodeUtils.EPISODES_PER_SEASON_COUNT);
            softly.assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT);
        });
    }

    @Override
    protected void assertDuplicateRepositoryData() {
        assertSoftly(softly -> {
            softly.assertThat(getRepositoryChildDataCount()).isEqualTo(getDefaultChildDataCount() + 1);
            softly.assertThat(getRepositoryParentDataCount()).isEqualTo(getDefaultParentDataCount());
            softly.assertThat(EpisodeUtils.getEpisodesCount(entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT + EpisodeUtils.EPISODES_PER_SEASON_COUNT);
            softly.assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT);
        });
    }

    @Override
    protected void assertReferences() {
        super.assertReferences();

        assertSoftly(softly -> {
            softly.assertThat(EpisodeUtils.getEpisodesCount(entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT);
            softly.assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT);
        });
    }

}
