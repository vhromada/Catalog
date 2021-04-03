package com.github.vhromada.catalog.web.utils

import com.github.vhromada.catalog.entity.Season
import com.github.vhromada.catalog.web.fo.SeasonFO
import com.github.vhromada.common.entity.Language
import org.assertj.core.api.SoftAssertions.assertSoftly

/**
 * A class represents utility class for seasons.
 *
 * @author Vladimir Hromada
 */
object SeasonUtils {

    /**
     * Returns FO for season.
     *
     * @return FO for season
     */
    fun getSeasonFO(): SeasonFO {
        return SeasonFO(
            id = TestConstants.ID,
            number = TestConstants.NUMBER.toString(),
            startYear = TestConstants.YEAR.toString(),
            endYear = (TestConstants.YEAR + 1).toString(),
            language = Language.EN,
            subtitles = listOf(Language.CZ),
            note = TestConstants.NOTE,
            position = TestConstants.POSITION
        )
    }

    /**
     * Returns season.
     *
     * @return season
     */
    fun getSeason(): Season {
        return Season(
            id = TestConstants.ID,
            number = TestConstants.NUMBER,
            startYear = TestConstants.YEAR,
            endYear = TestConstants.YEAR + 1,
            language = Language.EN,
            subtitles = listOf(Language.CZ),
            note = TestConstants.NOTE,
            position = TestConstants.POSITION
        )
    }

    /**
     * Asserts season deep equals.
     *
     * @param expected expected FO for season
     * @param actual   actual season
     */
    fun assertSeasonDeepEquals(expected: SeasonFO, actual: Season) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.number).isEqualTo(expected.number!!.toInt())
            it.assertThat(actual.startYear).isEqualTo(expected.startYear!!.toInt())
            it.assertThat(actual.endYear).isEqualTo(expected.endYear!!.toInt())
            it.assertThat(actual.language).isEqualTo(expected.language)
            it.assertThat(actual.subtitles).isEqualTo(expected.subtitles)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

    /**
     * Asserts season deep equals.
     *
     * @param expected expected season
     * @param actual   actual FO for season
     */
    fun assertSeasonDeepEquals(expected: Season, actual: SeasonFO) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.number).isEqualTo(expected.number.toString())
            it.assertThat(actual.startYear).isEqualTo(expected.startYear.toString())
            it.assertThat(actual.endYear).isEqualTo(expected.endYear.toString())
            it.assertThat(actual.language).isEqualTo(expected.language)
            it.assertThat(actual.subtitles).isEqualTo(expected.subtitles)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

}
