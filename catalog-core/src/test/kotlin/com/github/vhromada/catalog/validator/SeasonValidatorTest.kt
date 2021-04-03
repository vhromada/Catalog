package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.utils.SeasonUtils
import com.github.vhromada.catalog.utils.TestConstants
import com.github.vhromada.common.entity.Language
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.result.Status
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional

/**
 * A class represents test for class [SeasonValidator].
 *
 * @author Vladimir Hromada
 */
class SeasonValidatorTest {

    /**
     * Instance of [SeasonValidator]
     */
    private lateinit var validator: SeasonValidator

    /**
     * Initializes validator.
     */
    @BeforeEach
    fun setUp() {
        validator = SeasonValidator()
    }

    /**
     * Test method for [SeasonValidator.validate] with correct new season.
     */
    @Test
    fun validateNew() {
        val result = validator.validate(data = SeasonUtils.newSeason(id = null), update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [SeasonValidator.validate] with null new season.
     */
    @Test
    fun validateNewNull() {
        val result = validator.validate(data = null, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_NULL", message = "Season mustn't be null.")))
        }
    }

    /**
     * Test method for [SeasonValidator.validate] with new season with not null ID.
     */
    @Test
    fun validateNewNotNullId() {
        val season = SeasonUtils.newSeason(id = null)
            .copy(id = Int.MAX_VALUE)

        val result = validator.validate(data = season, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_ID_NOT_NULL", message = "ID must be null.")))
        }
    }

    /**
     * Test method for [SeasonValidator.validate] with new season with not null position.
     */
    @Test
    fun validateNewNotNullPosition() {
        val season = SeasonUtils.newSeason(id = null)
            .copy(position = Int.MAX_VALUE)

        val result = validator.validate(data = season, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_POSITION_NOT_NULL", message = "Position must be null.")))
        }
    }

    /**
     * Test method for [SeasonValidator.validate] with new season with null number of season.
     */
    @Test
    fun validateNewNullNumber() {
        val season = SeasonUtils.newSeason(id = null)
            .copy(number = null)

        val result = validator.validate(data = season, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_NUMBER_NULL", message = "Number of season mustn't be null.")))
        }
    }

    /**
     * Test method for [SeasonValidator.validate] with new season with not positive number of season.
     */
    @Test
    fun validateNewNotPositiveNumber() {
        val season = SeasonUtils.newSeason(id = null)
            .copy(number = 0)

        val result = validator.validate(data = season, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_NUMBER_NOT_POSITIVE", message = "Number of season must be positive number.")))
        }
    }

    /**
     * Test method for [SeasonValidator.validate] with new season with null starting year.
     */
    @Test
    fun validateNewNullStartingYear() {
        val season = SeasonUtils.newSeason(id = null)
            .copy(startYear = null)

        val result = validator.validate(data = season, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_START_YEAR_NULL", message = "Starting year mustn't be null.")))
        }
    }

    /**
     * Test method for [SeasonValidator.validate] with new season with null ending year.
     */
    @Test
    fun validateNewNullEndingYear() {
        val season = SeasonUtils.newSeason(id = null)
            .copy(endYear = null)

        val result = validator.validate(data = season, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_END_YEAR_NULL", message = "Ending year mustn't be null.")))
        }
    }

    /**
     * Test method for [SeasonValidator.validate] with new season with bad minimum starting year and bad minimum ending year.
     */
    @Test
    fun validateNewBadMinimumYears() {
        val season = SeasonUtils.newSeason(id = null)
            .copy(startYear = TestConstants.BAD_MIN_YEAR, endYear = TestConstants.BAD_MIN_YEAR)

        val result = validator.validate(data = season, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_STARTING_YEAR_EVENT, TestConstants.INVALID_ENDING_YEAR_EVENT))
        }
    }

    /**
     * Test method for [SeasonValidator.validate] with new season with bad maximum starting year and bad maximum ending year.
     */
    @Test
    fun validateNewBadMaximumYears() {
        val season = SeasonUtils.newSeason(id = null)
            .copy(startYear = TestConstants.BAD_MAX_YEAR, endYear = TestConstants.BAD_MAX_YEAR)

        val result = validator.validate(data = season, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_STARTING_YEAR_EVENT, TestConstants.INVALID_ENDING_YEAR_EVENT))
        }
    }

    /**
     * Test method for [SeasonValidator.validate] with new season with starting year greater than ending year.
     */
    @Test
    fun validateNewBadYears() {
        var season = SeasonUtils.newSeason(id = null)
        season = season.copy(startYear = season.endYear!! + 1)

        val result = validator.validate(data = season, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_YEARS_NOT_VALID", message = "Starting year mustn't be greater than ending year.")))
        }
    }

    /**
     * Test method for [SeasonValidator.validate]} with new season with null language.
     */
    @Test
    fun validateNewNullLanguage() {
        val season = SeasonUtils.newSeason(id = null)
            .copy(language = null)

        val result = validator.validate(data = season, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_LANGUAGE_NULL", message = "Language mustn't be null.")))
        }
    }

    /**
     * Test method for [SeasonValidator.validate]} with new season with null subtitles.
     */
    @Test
    fun validateNewNullSubtitles() {
        val season = SeasonUtils.newSeason(id = null)
            .copy(subtitles = null)

        val result = validator.validate(data = season, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_SUBTITLES_NULL", message = "Subtitles mustn't be null.")))
        }
    }

    /**
     * Test method for [SeasonValidator.validate]} with new season with subtitles with null value.
     */
    @Test
    fun validateNewBadSubtitles() {
        val season = SeasonUtils.newSeason(id = null)
            .copy(subtitles = listOf(Language.CZ, null))

        val result = validator.validate(data = season, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_SUBTITLES_CONTAIN_NULL", message = "Subtitles mustn't contain null value.")))
        }
    }

    /**
     * Test method for [SeasonValidator.validate]} with new season with null note.
     */
    @Test
    fun validateNewNullNote() {
        val season = SeasonUtils.newSeason(id = null)
            .copy(note = null)

        val result = validator.validate(data = season, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_NOTE_NULL", message = "Note mustn't be null.")))
        }
    }

    /**
     * Test method for [SeasonValidator.validate] with with update correct season.
     */
    @Test
    fun validateUpdate() {
        val result = validator.validate(data = SeasonUtils.newSeason(id = 1), update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [SeasonValidator.validate] with null update season.
     */
    @Test
    fun validateUpdateNull() {
        val result = validator.validate(data = null, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_NULL", message = "Season mustn't be null.")))
        }
    }

    /**
     * Test method for [SeasonValidator.validate] with update season with null ID.
     */
    @Test
    fun validateUpdateNullId() {
        val season = SeasonUtils.newSeason(id = 1)
            .copy(id = null)

        val result = validator.validate(data = season, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_ID_NULL", message = "ID mustn't be null.")))
        }
    }

    /**
     * Test method for [SeasonValidator.validate] with update season with null position.
     */
    @Test
    fun validateUpdateNullPosition() {
        val season = SeasonUtils.newSeason(id = 1)
            .copy(position = null)

        val result = validator.validate(data = season, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_POSITION_NULL", message = "Position mustn't be null.")))
        }
    }

    /**
     * Test method for [SeasonValidator.validate] with update season with null number of season.
     */
    @Test
    fun validateUpdateNullNumber() {
        val season = SeasonUtils.newSeason(id = 1)
            .copy(number = null)

        val result = validator.validate(data = season, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_NUMBER_NULL", message = "Number of season mustn't be null.")))
        }
    }

    /**
     * Test method for [SeasonValidator.validate] with update season with not positive number of season.
     */
    @Test
    fun validateUpdateNotPositiveNumber() {
        val season = SeasonUtils.newSeason(id = 1)
            .copy(number = 0)

        val result = validator.validate(data = season, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_NUMBER_NOT_POSITIVE", message = "Number of season must be positive number.")))
        }
    }

    /**
     * Test method for [SeasonValidator.validate] with update season with null starting year.
     */
    @Test
    fun validateUpdateNullStartingYear() {
        val season = SeasonUtils.newSeason(1)
            .copy(startYear = null)

        val result = validator.validate(data = season, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_START_YEAR_NULL", message = "Starting year mustn't be null.")))
        }
    }

    /**
     * Test method for [SeasonValidator.validate] with update season with null ending year.
     */
    @Test
    fun validateUpdateNullEndingYear() {
        val season = SeasonUtils.newSeason(1)
            .copy(endYear = null)

        val result = validator.validate(data = season, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_END_YEAR_NULL", message = "Ending year mustn't be null.")))
        }
    }

    /**
     * Test method for [SeasonValidator.validate] with update season with bad minimum starting year and bad minimum ending year.
     */
    @Test
    fun validateUpdateBadMinimumYears() {
        val season = SeasonUtils.newSeason(1)
            .copy(startYear = TestConstants.BAD_MIN_YEAR, endYear = TestConstants.BAD_MIN_YEAR)

        val result = validator.validate(data = season, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_STARTING_YEAR_EVENT, TestConstants.INVALID_ENDING_YEAR_EVENT))
        }
    }

    /**
     * Test method for [SeasonValidator.validate] with update season with bad maximum starting year and bad maximum ending year.
     */
    @Test
    fun validateUpdateBadMaximumYears() {
        val season = SeasonUtils.newSeason(1)
            .copy(startYear = TestConstants.BAD_MAX_YEAR, endYear = TestConstants.BAD_MAX_YEAR)

        val result = validator.validate(data = season, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_STARTING_YEAR_EVENT, TestConstants.INVALID_ENDING_YEAR_EVENT))
        }
    }

    /**
     * Test method for [SeasonValidator.validate] with update season with starting year greater than ending year.
     */
    @Test
    fun validateUpdateBadYears() {
        var season = SeasonUtils.newSeason(1)
        season = season.copy(startYear = season.endYear!! + 1)

        val result = validator.validate(data = season, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_YEARS_NOT_VALID", message = "Starting year mustn't be greater than ending year.")))
        }
    }

    /**
     * Test method for [SeasonValidator.validate]} with update season with null language.
     */
    @Test
    fun validateUpdateNullLanguage() {
        val season = SeasonUtils.newSeason(id = 1)
            .copy(language = null)

        val result = validator.validate(data = season, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_LANGUAGE_NULL", message = "Language mustn't be null.")))
        }
    }

    /**
     * Test method for [SeasonValidator.validate]} with update season with null subtitles.
     */
    @Test
    fun validateUpdateNullSubtitles() {
        val season = SeasonUtils.newSeason(id = 1)
            .copy(subtitles = null)

        val result = validator.validate(data = season, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_SUBTITLES_NULL", message = "Subtitles mustn't be null.")))
        }
    }

    /**
     * Test method for [SeasonValidator.validate]} with update season with subtitles with null value.
     */
    @Test
    fun validateUpdateBadSubtitles() {
        val season = SeasonUtils.newSeason(id = 1)
            .copy(subtitles = listOf(Language.CZ, null))

        val result = validator.validate(data = season, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_SUBTITLES_CONTAIN_NULL", message = "Subtitles mustn't contain null value.")))
        }
    }

    /**
     * Test method for [SeasonValidator.validate]} with update season with null note.
     */
    @Test
    fun validateUpdateNullNote() {
        val season = SeasonUtils.newSeason(id = 1)
            .copy(note = null)

        val result = validator.validate(data = season, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_NOTE_NULL", message = "Note mustn't be null.")))
        }
    }

    /**
     * Test method for [SeasonValidator.validateExists] with correct season.
     */
    @Test
    fun validateExists() {
        val result = validator.validateExists(data = Optional.of(SeasonUtils.newSeasonDomain(id = 1)))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [SeasonValidator.validateExists] with invalid season.
     */
    @Test
    fun validateExistsInvalid() {
        val result = validator.validateExists(data = Optional.empty())

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_NOT_EXIST", message = "Season doesn't exist.")))
        }
    }

    /**
     * Test method for [SeasonValidator.validateMovingData] with correct up season.
     */
    @Test
    fun validateMovingDataUp() {
        val seasons = listOf(SeasonUtils.newSeasonDomain(id = 1), SeasonUtils.newSeasonDomain(id = 2))

        val result = validator.validateMovingData(data = seasons[1], list = seasons, up = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [SeasonValidator.validateMovingData] with with invalid up season.
     */
    @Test
    fun validateMovingDataUpInvalid() {
        val seasons = listOf(SeasonUtils.newSeasonDomain(id = 1), SeasonUtils.newSeasonDomain(id = 2))

        val result = validator.validateMovingData(data = seasons[0], list = seasons, up = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_NOT_MOVABLE", message = "Season can't be moved up.")))
        }
    }

    /**
     * Test method for [SeasonValidator.validateMovingData] with correct down season.
     */
    @Test
    fun validateMovingDataDown() {
        val seasons = listOf(SeasonUtils.newSeasonDomain(id = 1), SeasonUtils.newSeasonDomain(id = 2))

        val result = validator.validateMovingData(data = seasons[0], list = seasons, up = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [SeasonValidator.validateMovingData] with with invalid down season.
     */
    @Test
    fun validateMovingDataDownInvalid() {
        val seasons = listOf(SeasonUtils.newSeasonDomain(id = 1), SeasonUtils.newSeasonDomain(id = 2))

        val result = validator.validateMovingData(data = seasons[1], list = seasons, up = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_NOT_MOVABLE", message = "Season can't be moved down.")))
        }
    }

}
