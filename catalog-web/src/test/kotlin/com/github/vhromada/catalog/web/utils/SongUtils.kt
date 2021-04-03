package com.github.vhromada.catalog.web.utils

import com.github.vhromada.catalog.entity.Song
import com.github.vhromada.catalog.web.fo.SongFO
import org.assertj.core.api.SoftAssertions.assertSoftly

/**
 * A class represents utility class for songs.
 *
 * @author Vladimir Hromada
 */
object SongUtils {

    /**
     * Returns FO for song.
     *
     * @return FO for song
     */
    fun getSongFO(): SongFO {
        return SongFO(
            id = TestConstants.ID,
            name = TestConstants.NAME,
            length = TimeUtils.getTimeFO(),
            note = TestConstants.NOTE,
            position = TestConstants.POSITION
        )
    }

    /**
     * Returns song.
     *
     * @return song
     */
    fun getSong(): Song {
        return Song(
            id = TestConstants.ID,
            name = TestConstants.NAME,
            length = TestConstants.LENGTH,
            note = TestConstants.NOTE,
            position = TestConstants.POSITION
        )
    }

    /**
     * Asserts song deep equals.
     *
     * @param expected expected FO for song
     * @param actual   actual song
     */
    fun assertSongDeepEquals(expected: SongFO, actual: Song) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.name).isEqualTo(expected.name)
            TimeUtils.assertTimeDeepEquals(softly = it, expected = expected.length, actual = actual.length)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

    /**
     * Asserts song deep equals.
     *
     * @param expected expected song
     * @param actual   actual FO for song
     */
    fun assertSongDeepEquals(expected: Song, actual: SongFO) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.name).isEqualTo(expected.name)
            TimeUtils.assertTimeDeepEquals(softly = it, expected = expected.length, actual = actual.length)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

}
