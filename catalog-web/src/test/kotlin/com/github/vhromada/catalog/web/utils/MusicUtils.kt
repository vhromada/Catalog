package com.github.vhromada.catalog.web.utils

import com.github.vhromada.catalog.entity.Music
import com.github.vhromada.catalog.web.fo.MusicFO
import org.assertj.core.api.SoftAssertions.assertSoftly

/**
 * A class represents utility class for music.
 *
 * @author Vladimir Hromada
 */
object MusicUtils {

    /**
     * Returns FO for music.
     *
     * @return FO for music
     */
    fun getMusicFO(): MusicFO {
        return MusicFO(
            id = TestConstants.ID,
            name = TestConstants.NAME,
            wikiEn = TestConstants.EN_WIKI,
            wikiCz = TestConstants.CZ_WIKI,
            mediaCount = TestConstants.MEDIA.toString(),
            note = TestConstants.NOTE,
            position = TestConstants.POSITION
        )
    }

    /**
     * Returns music.
     *
     * @return music
     */
    fun getMusic(): Music {
        return Music(
            id = TestConstants.ID,
            name = TestConstants.NAME,
            wikiEn = TestConstants.EN_WIKI,
            wikiCz = TestConstants.CZ_WIKI,
            mediaCount = TestConstants.MEDIA,
            note = TestConstants.NOTE,
            position = TestConstants.POSITION
        )
    }

    /**
     * Asserts music deep equals.
     *
     * @param expected expected FO for music
     * @param actual   actual music
     */
    fun assertMusicDeepEquals(expected: MusicFO, actual: Music) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.name).isEqualTo(expected.name)
            it.assertThat(actual.wikiEn).isEqualTo(expected.wikiEn)
            it.assertThat(actual.wikiCz).isEqualTo(expected.wikiCz)
            it.assertThat(actual.mediaCount).isEqualTo(expected.mediaCount!!.toInt())
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

    /**
     * Asserts music deep equals.
     *
     * @param expected expected music
     * @param actual   actual FO for music
     */
    fun assertMusicDeepEquals(expected: Music, actual: MusicFO) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.name).isEqualTo(expected.name)
            it.assertThat(actual.wikiEn).isEqualTo(expected.wikiEn)
            it.assertThat(actual.wikiCz).isEqualTo(expected.wikiCz)
            it.assertThat(actual.mediaCount).isEqualTo(expected.mediaCount.toString())
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

}
