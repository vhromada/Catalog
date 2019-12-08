package cz.vhromada.catalog.facade.impl

import cz.vhromada.catalog.CatalogTestConfiguration
import cz.vhromada.catalog.entity.Season
import cz.vhromada.catalog.entity.Show
import cz.vhromada.catalog.facade.SeasonFacade
import cz.vhromada.catalog.utils.EpisodeUtils
import cz.vhromada.catalog.utils.GenreUtils
import cz.vhromada.catalog.utils.SeasonUtils
import cz.vhromada.catalog.utils.ShowUtils
import cz.vhromada.common.Language
import cz.vhromada.common.facade.MovableChildFacade
import cz.vhromada.common.test.facade.MovableChildFacadeIntegrationTest
import cz.vhromada.common.test.utils.TestConstants
import cz.vhromada.common.utils.Constants
import cz.vhromada.validation.result.Event
import cz.vhromada.validation.result.Severity
import cz.vhromada.validation.result.Status
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.test.context.ContextConfiguration
import javax.persistence.EntityManager

/**
 * A class represents integration test for class [SeasonFacadeImpl].
 *
 * @author Vladimir Hromada
 */
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
class SeasonFacadeImplIntegrationTest : MovableChildFacadeIntegrationTest<Season, cz.vhromada.catalog.domain.Season, Show>() {

    /**
     * Instance of [EntityManager]
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private lateinit var entityManager: EntityManager

    /**
     * Instance of (@link SeasonFacade}
     */
    @Autowired
    private lateinit var facade: SeasonFacade

    /**
     * Test method for [SeasonFacade.add] with season with null number of season.
     */
    @Test
    fun add_NullNumber() {
        val season = newChildData(null)
                .copy(number = null)

        val result = facade.add(ShowUtils.newShow(1), season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SEASON_NUMBER_NULL", "Number of season mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SeasonFacade.add] with season with not positive number of season.
     */
    @Test
    fun add_NotPositiveNumber() {
        val season = newChildData(null)
                .copy(number = 0)

        val result = facade.add(ShowUtils.newShow(1), season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SEASON_NUMBER_NOT_POSITIVE", "Number of season must be positive number.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SeasonFacade.add] with season with null starting year.
     */
    @Test
    fun add_NullStartYear() {
        val season = newChildData(null)
                .copy(startYear = null)

        val result = facade.add(ShowUtils.newShow(1), season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SEASON_START_YEAR_NULL", "Starting year mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SeasonFacade.add] with season with null ending year.
     */
    @Test
    fun add_NullEndYear() {
        val season = newChildData(null)
                .copy(endYear = null)

        val result = facade.add(ShowUtils.newShow(1), season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SEASON_END_YEAR_NULL", "Ending year mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SeasonFacade.add] with season with bad minimum starting year and bad minimum ending year.
     */
    @Test
    fun add_BadMinimumYears() {
        val season = newChildData(null)
                .copy(startYear = TestConstants.BAD_MIN_YEAR, endYear = TestConstants.BAD_MIN_YEAR)

        val result = facade.add(ShowUtils.newShow(1), season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_STARTING_YEAR_EVENT, INVALID_ENDING_YEAR_EVENT))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SeasonFacade.add] with season with bad maximum starting year and bad maximum ending year.
     */
    @Test
    fun add_BadMaximumYears() {
        val season = newChildData(null)
                .copy(startYear = TestConstants.BAD_MAX_YEAR, endYear = TestConstants.BAD_MAX_YEAR)

        val result = facade.add(ShowUtils.newShow(1), season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_STARTING_YEAR_EVENT, INVALID_ENDING_YEAR_EVENT))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SeasonFacade.add] with season with starting year greater than ending year.
     */
    @Test
    fun add_BadYears() {
        var season = newChildData(null)
        season = season.copy(startYear = season.endYear!! + 1)

        val result = facade.add(ShowUtils.newShow(1), season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SEASON_YEARS_NOT_VALID", "Starting year mustn't be greater than ending year.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SeasonFacade.add] with season with null language.
     */
    @Test
    fun add_NullLanguage() {
        val season = newChildData(null)
                .copy(language = null)

        val result = facade.add(ShowUtils.newShow(1), season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SEASON_LANGUAGE_NULL", "Language mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SeasonFacade.add] with season with null subtitles.
     */
    @Test
    fun add_NullSubtitles() {
        val season = newChildData(null)
                .copy(subtitles = null)

        val result = facade.add(ShowUtils.newShow(1), season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SEASON_SUBTITLES_NULL", "Subtitles mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SeasonFacade.add] with season with subtitles with null value.
     */
    @Test
    fun add_BadSubtitles() {
        val season = newChildData(null)
                .copy(subtitles = listOf(Language.CZ, null))

        val result = facade.add(ShowUtils.newShow(1), season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SEASON_SUBTITLES_CONTAIN_NULL", "Subtitles mustn't contain null value.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SeasonFacade.add] with season with null note.
     */
    @Test
    fun add_NullNote() {
        val season = newChildData(null)
                .copy(note = null)

        val result = facade.add(ShowUtils.newShow(1), season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SEASON_NOTE_NULL", "Note mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SeasonFacade.update] with season with null number of season.
     */
    @Test
    fun update_NullNumber() {
        val season = newChildData(1)
                .copy(number = null)

        val result = facade.update(season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SEASON_NUMBER_NULL", "Number of season mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SeasonFacade.update] with season with not positive number of season.
     */
    @Test
    fun update_NotPositiveNumber() {
        val season = newChildData(1)
                .copy(number = 0)

        val result = facade.update(season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SEASON_NUMBER_NOT_POSITIVE", "Number of season must be positive number.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SeasonFacade.update] with season with null starting year.
     */
    @Test
    fun update_NullStartYear() {
        val season = newChildData(1)
                .copy(startYear = null)

        val result = facade.update(season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SEASON_START_YEAR_NULL", "Starting year mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SeasonFacade.update] with season with null ending year.
     */
    @Test
    fun update_NullEndYear() {
        val season = newChildData(1)
                .copy(endYear = null)

        val result = facade.update(season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SEASON_END_YEAR_NULL", "Ending year mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SeasonFacade.update] with season with bad minimum starting year and bad minimum ending year.
     */
    @Test
    fun update_BadMinimumYears() {
        val season = newChildData(1)
                .copy(startYear = TestConstants.BAD_MIN_YEAR, endYear = TestConstants.BAD_MIN_YEAR)

        val result = facade.update(season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_STARTING_YEAR_EVENT, INVALID_ENDING_YEAR_EVENT))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SeasonFacade.update] with season with bad maximum starting year and bad maximum ending year.
     */
    @Test
    fun update_BadMaximumYears() {
        val season = newChildData(1)
                .copy(startYear = TestConstants.BAD_MAX_YEAR, endYear = TestConstants.BAD_MAX_YEAR)

        val result = facade.update(season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_STARTING_YEAR_EVENT, INVALID_ENDING_YEAR_EVENT))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SeasonFacade.update] with season with starting year greater than ending year.
     */
    @Test
    fun update_BadYears() {
        var season = newChildData(4)
        season = season.copy(startYear = season.endYear!! + 1)

        val result = facade.update(season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SEASON_YEARS_NOT_VALID", "Starting year mustn't be greater than ending year.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SeasonFacade.update] with season with null language.
     */
    @Test
    fun update_NullLanguage() {
        val season = newChildData(1)
                .copy(language = null)

        val result = facade.update(season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SEASON_LANGUAGE_NULL", "Language mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SeasonFacade.update] with season with null subtitles.
     */
    @Test
    fun update_NullSubtitles() {
        val season = newChildData(1)
                .copy(subtitles = null)

        val result = facade.update(season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SEASON_SUBTITLES_NULL", "Subtitles mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SeasonFacade.update] with season with subtitles with null value.
     */
    @Test
    fun update_BadSubtitles() {
        val season = newChildData(1)
                .copy(subtitles = listOf(Language.CZ, null))

        val result = facade.update(season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SEASON_SUBTITLES_CONTAIN_NULL", "Subtitles mustn't contain null value.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SeasonFacade.update] with season with null note.
     */
    @Test
    fun update_NullNote() {
        val season = newChildData(1)
                .copy(note = null)

        val result = facade.update(season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SEASON_NOTE_NULL", "Note mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    override fun getFacade(): MovableChildFacade<Season, Show> {
        return facade
    }

    override fun getDefaultParentDataCount(): Int {
        return ShowUtils.SHOWS_COUNT
    }

    override fun getDefaultChildDataCount(): Int {
        return SeasonUtils.SEASONS_COUNT
    }

    override fun getRepositoryParentDataCount(): Int {
        return ShowUtils.getShowsCount(entityManager)
    }

    override fun getRepositoryChildDataCount(): Int {
        return SeasonUtils.getSeasonsCount(entityManager)
    }

    override fun getDataList(parentId: Int): List<cz.vhromada.catalog.domain.Season> {
        return SeasonUtils.getSeasons(parentId)
    }

    override fun getDomainData(index: Int): cz.vhromada.catalog.domain.Season {
        return SeasonUtils.getSeason(index)
    }

    override fun newParentData(id: Int?): Show {
        return ShowUtils.newShow(id)
    }

    override fun newChildData(id: Int?): Season {
        return SeasonUtils.newSeason(id)
    }

    override fun newDomainData(id: Int): cz.vhromada.catalog.domain.Season {
        return SeasonUtils.newSeasonDomain(id)
    }

    override fun getRepositoryData(id: Int): cz.vhromada.catalog.domain.Season? {
        return SeasonUtils.getSeason(entityManager, id)
    }

    override fun getParentName(): String {
        return "Show"
    }

    override fun getChildName(): String {
        return "Season"
    }

    override fun assertDataListDeepEquals(expected: List<Season>, actual: List<cz.vhromada.catalog.domain.Season>) {
        SeasonUtils.assertSeasonListDeepEquals(expected, actual)
    }

    override fun assertDataDeepEquals(expected: Season, actual: cz.vhromada.catalog.domain.Season) {
        SeasonUtils.assertSeasonDeepEquals(expected, actual)
    }

    override fun assertDataDomainDeepEquals(expected: cz.vhromada.catalog.domain.Season, actual: cz.vhromada.catalog.domain.Season) {
        SeasonUtils.assertSeasonDeepEquals(expected, actual)
    }

    override fun getExpectedDuplicatedData(): cz.vhromada.catalog.domain.Season {
        val season = super.getExpectedDuplicatedData()
        for (episode in season.episodes) {
            episode.id = EpisodeUtils.EPISODES_COUNT + season.episodes.indexOf(episode) + 1
        }

        return season
    }

    override fun assertRemoveRepositoryData() {
        assertSoftly {
            it.assertThat(getRepositoryChildDataCount()).isEqualTo(getDefaultChildDataCount() - 1)
            it.assertThat(getRepositoryParentDataCount()).isEqualTo(getDefaultParentDataCount())
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT - EpisodeUtils.EPISODES_PER_SEASON_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    override fun assertDuplicateRepositoryData() {
        assertSoftly {
            it.assertThat(getRepositoryChildDataCount()).isEqualTo(getDefaultChildDataCount() + 1)
            it.assertThat(getRepositoryParentDataCount()).isEqualTo(getDefaultParentDataCount())
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT + EpisodeUtils.EPISODES_PER_SEASON_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    override fun assertReferences() {
        super.assertReferences()

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    companion object {

        /**
         * Event for invalid starting year
         */
        private val INVALID_STARTING_YEAR_EVENT = Event(Severity.ERROR, "SEASON_START_YEAR_NOT_VALID",
                "Starting year must be between ${Constants.MIN_YEAR} and ${Constants.CURRENT_YEAR}.")

        /**
         * Event for invalid ending year
         */
        private val INVALID_ENDING_YEAR_EVENT = Event(Severity.ERROR, "SEASON_END_YEAR_NOT_VALID",
                "Ending year must be between ${Constants.MIN_YEAR} and ${Constants.CURRENT_YEAR}.")

    }

}