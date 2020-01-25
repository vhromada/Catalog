package cz.vhromada.catalog.validator

import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import cz.vhromada.catalog.domain.Show
import cz.vhromada.catalog.entity.Season
import cz.vhromada.catalog.utils.SeasonUtils
import cz.vhromada.catalog.utils.ShowUtils
import cz.vhromada.common.Language
import cz.vhromada.common.result.Event
import cz.vhromada.common.result.Severity
import cz.vhromada.common.result.Status
import cz.vhromada.common.test.utils.TestConstants
import cz.vhromada.common.test.validator.MovableValidatorTest
import cz.vhromada.common.utils.Constants
import cz.vhromada.common.validator.AbstractMovableValidator
import cz.vhromada.common.validator.MovableValidator
import cz.vhromada.common.validator.ValidationType
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test

/**
 * A class represents test for class [SeasonValidator].
 *
 * @author Vladimir Hromada
 */
class SeasonValidatorTest : MovableValidatorTest<Season, Show>() {

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null number of season.
     */
    @Test
    fun validateDeepNullNumber() {
        val season = getValidatingData(1)
                .copy(number = null)

        val result = getValidator().validate(season, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SEASON_NUMBER_NULL", "Number of season mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with not positive number of season.
     */
    @Test
    fun validateDeepNotPositiveNumber() {
        val season = getValidatingData(1)
                .copy(number = 0)

        val result = getValidator().validate(season, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SEASON_NUMBER_NOT_POSITIVE", "Number of season must be positive number.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null starting year.
     */
    @Test
    fun validateDeepNullStartingYear() {
        val season = SeasonUtils.newSeason(1)
                .copy(startYear = null)

        val result = getValidator().validate(season, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SEASON_START_YEAR_NULL", "Starting year mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null ending year.
     */
    @Test
    fun validateDeepNullEndingYear() {
        val season = SeasonUtils.newSeason(1)
                .copy(endYear = null)

        val result = getValidator().validate(season, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SEASON_END_YEAR_NULL", "Ending year mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with bad minimum starting year and bad minimum ending year.
     */
    @Test
    fun validateDeepBadMinimumYears() {
        val season = SeasonUtils.newSeason(1)
                .copy(startYear = TestConstants.BAD_MIN_YEAR, endYear = TestConstants.BAD_MIN_YEAR)

        val result = getValidator().validate(season, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_STARTING_YEAR_EVENT, INVALID_ENDING_YEAR_EVENT))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with bad maximum starting year and bad maximum ending year.
     */
    @Test
    fun validateDeepBadMaximumYears() {
        val season = SeasonUtils.newSeason(1)
                .copy(startYear = TestConstants.BAD_MAX_YEAR, endYear = TestConstants.BAD_MAX_YEAR)

        val result = getValidator().validate(season, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_STARTING_YEAR_EVENT, INVALID_ENDING_YEAR_EVENT))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with starting year greater than ending year.
     */
    @Test
    fun validateDeepBadYears() {
        var season = SeasonUtils.newSeason(1)
        season = season.copy(startYear = season.endYear!! + 1)

        val result = getValidator().validate(season, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SEASON_YEARS_NOT_VALID", "Starting year mustn't be greater than ending year.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with null language.
     */
    @Test
    fun validateDeepNullLanguage() {
        val season = getValidatingData(1)
                .copy(language = null)

        val result = getValidator().validate(season, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SEASON_LANGUAGE_NULL", "Language mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with null subtitles.
     */
    @Test
    fun validateDeepNullSubtitles() {
        val season = getValidatingData(1)
                .copy(subtitles = null)

        val result = getValidator().validate(season, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SEASON_SUBTITLES_NULL", "Subtitles mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with subtitles with null value.
     */
    @Test
    fun validateDeepBadSubtitles() {
        val season = getValidatingData(1)
                .copy(subtitles = listOf(Language.CZ, null))

        val result = getValidator().validate(season, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SEASON_SUBTITLES_CONTAIN_NULL", "Subtitles mustn't contain null value.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with null note.
     */
    @Test
    fun validateDeepNullNote() {
        val season = getValidatingData(1)
                .copy(note = null)

        val result = getValidator().validate(season, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SEASON_NOTE_NULL", "Note mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    override fun getValidator(): MovableValidator<Season> {
        return SeasonValidator(service)
    }

    override fun getValidatingData(id: Int?): Season {
        return SeasonUtils.newSeason(id)
    }

    override fun getValidatingData(id: Int?, position: Int?): Season {
        return SeasonUtils.newSeason(id)
                .copy(position = position)
    }

    override fun getRepositoryData(validatingData: Season): Show {
        return ShowUtils.newShowWithSeasons(validatingData.id)
    }

    override fun getItem1(): Show {
        return ShowUtils.newShowDomain(null)
    }

    override fun getItem2(): Show {
        return ShowUtils.newShowDomain(null)
    }

    override fun getName(): String {
        return "Season"
    }

    override fun initExistsMock(validatingData: Season, exists: Boolean) {
        val show = if (exists) ShowUtils.newShowWithSeasons(validatingData.id) else ShowUtils.newShowDomain(Integer.MAX_VALUE)

        whenever(service.getAll()).thenReturn(listOf(show))
    }

    override fun verifyExistsMock(validatingData: Season) {
        verify(service).getAll()
        verifyNoMoreInteractions(service)
    }

    override fun initMovingMock(validatingData: Season, up: Boolean, valid: Boolean) {
        val seasons = if (up && valid || !up && !valid) {
            listOf(SeasonUtils.newSeasonDomain(1), SeasonUtils.newSeasonDomain(validatingData.id))
        } else {
            listOf(SeasonUtils.newSeasonDomain(validatingData.id), SeasonUtils.newSeasonDomain(Integer.MAX_VALUE))
        }
        val show = ShowUtils.newShowDomain(1)
                .copy(seasons = seasons)

        whenever(service.getAll()).thenReturn(listOf(show))
    }

    override fun verifyMovingMock(validatingData: Season) {
        verify(service, times(2)).getAll()
        verifyNoMoreInteractions(service)
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
