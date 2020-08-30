package com.github.vhromada.catalog.web.common

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
        return SongFO(id = CatalogUtils.ID,
                name = CatalogUtils.NAME,
                length = TimeUtils.getTimeFO(),
                note = CatalogUtils.NOTE,
                position = CatalogUtils.POSITION)
    }

    /**
     * Returns song.
     *
     * @return song
     */
    fun getSong(): Song {
        return Song(id = CatalogUtils.ID,
                name = CatalogUtils.NAME,
                length = CatalogUtils.LENGTH,
                note = CatalogUtils.NOTE,
                position = CatalogUtils.POSITION)
    }

    /**
     * Asserts song deep equals.
     *
     * @param expected expected FO for song
     * @param actual   actual song
     */
    fun assertSongDeepEquals(expected: SongFO?, actual: Song?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.id).isEqualTo(expected!!.id)
            it.assertThat(actual.name).isEqualTo(expected.name)
            TimeUtils.assertTimeDeepEquals(expected.length, actual.length)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

}
