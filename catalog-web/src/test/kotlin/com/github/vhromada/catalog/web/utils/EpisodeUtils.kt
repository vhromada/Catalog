package com.github.vhromada.catalog.web.utils

import com.github.vhromada.catalog.entity.Episode
import com.github.vhromada.catalog.web.fo.EpisodeFO
import org.assertj.core.api.SoftAssertions.assertSoftly

/**
 * A class represents utility class for episodes.
 *
 * @author Vladimir Hromada
 */
object EpisodeUtils {

    /**
     * Returns FO for episode.
     *
     * @return FO for episode
     */
    fun getEpisodeFO(): EpisodeFO {
        return EpisodeFO(
            id = TestConstants.ID,
            number = TestConstants.NUMBER.toString(),
            name = TestConstants.NAME,
            length = TimeUtils.getTimeFO(),
            note = TestConstants.NOTE,
            position = TestConstants.POSITION
        )
    }

    /**
     * Returns episode.
     *
     * @return episode
     */
    fun getEpisode(): Episode {
        return Episode(
            id = TestConstants.ID,
            number = TestConstants.NUMBER,
            name = TestConstants.NAME,
            length = TestConstants.LENGTH,
            note = TestConstants.NOTE,
            position = TestConstants.POSITION
        )
    }

    /**
     * Asserts episode deep equals.
     *
     * @param expected expected FO for episode
     * @param actual   actual episode
     */
    fun assertEpisodeDeepEquals(expected: EpisodeFO, actual: Episode) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.number).isEqualTo(expected.number!!.toInt())
            it.assertThat(actual.name).isEqualTo(expected.name)
            TimeUtils.assertTimeDeepEquals(softly = it, expected = expected.length, actual = actual.length)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

    /**
     * Asserts episode deep equals.
     *
     * @param expected expected episode
     * @param actual   actual FO for episode
     */
    fun assertEpisodeDeepEquals(expected: Episode, actual: EpisodeFO) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.number).isEqualTo(expected.number.toString())
            it.assertThat(actual.name).isEqualTo(expected.name)
            TimeUtils.assertTimeDeepEquals(softly = it, expected = expected.length, actual = actual.length)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

}
