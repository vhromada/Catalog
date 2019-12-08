package cz.vhromada.catalog.utils

import cz.vhromada.catalog.entity.Song
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import javax.persistence.EntityManager

/**
 * Updates song fields.
 *
 * @return updated song
 */
fun cz.vhromada.catalog.domain.Song.updated(): cz.vhromada.catalog.domain.Song {
    return copy(name = "Name", length = 5, note = "Note")
}

/**
 * Updates song fields.
 *
 * @return updated song
 */
fun Song.updated(): Song {
    return copy(name = "Name", length = 5, note = "Note")
}

/**
 * A class represents utility class for songs.
 *
 * @author Vladimir Hromada
 */
object SongUtils {

    /**
     * Count of songs
     */
    const val SONGS_COUNT = 9

    /**
     * Count of songs in music
     */
    const val SONGS_PER_MUSIC_COUNT = 3

    /**
     * Multipliers for length
     */
    private val LENGTH_MULTIPLIERS = intArrayOf(1, 10, 100)

    /**
     * Returns song.
     *
     * @param id ID
     * @return song
     */
    fun newSongDomain(id: Int?): cz.vhromada.catalog.domain.Song {
        return cz.vhromada.catalog.domain.Song(id = id, name = "", length = 0, note = null, position = if (id == null) null else id - 1)
                .updated()
    }

    /**
     * Returns song.
     *
     * @param id ID
     * @return song
     */
    fun newSong(id: Int?): Song {
        return Song(id = id, name = "", length = 0, note = null, position = if (id == null) null else id - 1)
                .updated()
    }

    /**
     * Returns songs for music.
     *
     * @param music music
     * @return songs for music
     */
    fun getSongs(music: Int): List<cz.vhromada.catalog.domain.Song> {
        val songs = mutableListOf<cz.vhromada.catalog.domain.Song>()
        for (i in 1..SONGS_PER_MUSIC_COUNT) {
            songs.add(getSong(music, i))
        }

        return songs
    }

    /**
     * Returns song for index.
     *
     * @param index song index
     * @return song for index
     */
    fun getSong(index: Int): cz.vhromada.catalog.domain.Song {
        val musicNumber = (index - 1) / SONGS_PER_MUSIC_COUNT + 1
        val songNumber = (index - 1) % SONGS_PER_MUSIC_COUNT + 1

        return getSong(musicNumber, songNumber)
    }

    /**
     * Returns song for indexes.
     *
     * @param musicIndex music index
     * @param songIndex  song index
     * @return song for indexes
     */
    private fun getSong(musicIndex: Int, songIndex: Int): cz.vhromada.catalog.domain.Song {
        return cz.vhromada.catalog.domain.Song(
                id = (musicIndex - 1) * SONGS_PER_MUSIC_COUNT + songIndex,
                name = "Music $musicIndex Song $songIndex",
                length = songIndex * LENGTH_MULTIPLIERS[musicIndex - 1],
                note = if (songIndex == 2) "Music $musicIndex Song 2 note" else "",
                position = songIndex - 1)
    }

    /**
     * Returns song.
     *
     * @param entityManager entity manager
     * @param id            song ID
     * @return song
     */
    fun getSong(entityManager: EntityManager, id: Int): cz.vhromada.catalog.domain.Song? {
        return entityManager.find(cz.vhromada.catalog.domain.Song::class.java, id)
    }

    /**
     * Returns count of songs.
     *
     * @param entityManager entity manager
     * @return count of songs
     */
    @Suppress("CheckStyle")
    fun getSongsCount(entityManager: EntityManager): Int {
        return entityManager.createQuery("SELECT COUNT(s.id) FROM Song s", java.lang.Long::class.java).singleResult.toInt()
    }

    /**
     * Asserts songs deep equals.
     *
     * @param expected expected songs
     * @param actual   actual songs
     */
    fun assertSongsDeepEquals(expected: List<cz.vhromada.catalog.domain.Song?>?, actual: List<cz.vhromada.catalog.domain.Song?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertThat(expected!!.size).isEqualTo(actual!!.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertSongDeepEquals(expected[i], actual[i])
            }
        }
    }

    /**
     * Asserts song deep equals.
     *
     * @param expected expected song
     * @param actual   actual song
     */
    fun assertSongDeepEquals(expected: cz.vhromada.catalog.domain.Song?, actual: cz.vhromada.catalog.domain.Song?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(expected!!.id).isEqualTo(actual!!.id)
            it.assertThat(expected.name).isEqualTo(actual.name)
            it.assertThat(expected.length).isEqualTo(actual.length)
            it.assertThat(expected.note).isEqualTo(actual.note)
            it.assertThat(expected.position).isEqualTo(actual.position)
        }
    }

    /**
     * Asserts songs deep equals.
     *
     * @param expected expected list of song
     * @param actual   actual songs
     */
    fun assertSongListDeepEquals(expected: List<Song?>?, actual: List<cz.vhromada.catalog.domain.Song?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertThat(expected!!.size).isEqualTo(actual!!.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertSongDeepEquals(expected[i], actual[i])
            }
        }
    }

    /**
     * Asserts song deep equals.
     *
     * @param expected expected song
     * @param actual   actual song
     */
    fun assertSongDeepEquals(expected: Song?, actual: cz.vhromada.catalog.domain.Song?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.id).isEqualTo(expected!!.id)
            it.assertThat(actual.name).isEqualTo(expected.name)
            it.assertThat(actual.length).isEqualTo(expected.length)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

}
