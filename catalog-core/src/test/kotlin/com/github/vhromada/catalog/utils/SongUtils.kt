package com.github.vhromada.catalog.utils

import com.github.vhromada.catalog.entity.Song
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import javax.persistence.EntityManager

/**
 * Updates song fields.
 *
 * @return updated song
 */
fun com.github.vhromada.catalog.domain.Song.updated(): com.github.vhromada.catalog.domain.Song {
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
     * Position
     */
    const val POSITION = 10

    /**
     * Multipliers for length
     */
    private val LENGTH_MULTIPLIERS = intArrayOf(1, 10, 100)

    /**
     * Returns songs for music.
     *
     * @param music music
     * @return songs for music
     */
    fun getSongs(music: Int): MutableList<com.github.vhromada.catalog.domain.Song> {
        val songs = mutableListOf<com.github.vhromada.catalog.domain.Song>()
        for (i in 1..SONGS_PER_MUSIC_COUNT) {
            songs.add(getSongDomain(musicIndex = music, songIndex = i))
        }

        return songs
    }

    /**
     * Returns song.
     *
     * @param id ID
     * @return song
     */
    fun newSongDomain(id: Int?): com.github.vhromada.catalog.domain.Song {
        return com.github.vhromada.catalog.domain.Song(id = id, name = "", length = 0, note = null, position = if (id == null) null else id - 1)
            .updated()
    }

    /**
     * Returns song with music.
     *
     * @param id ID
     * @return song with music
     */
    fun newSongDomainWithMusic(id: Int): com.github.vhromada.catalog.domain.Song {
        val song = newSongDomain(id = id)
        song.music = MusicUtils.newMusicDomain(id = id)
        return song
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
     * Returns song for index.
     *
     * @param index song index
     * @return song for index
     */
    fun getSongDomain(index: Int): com.github.vhromada.catalog.domain.Song {
        val musicNumber = (index - 1) / SONGS_PER_MUSIC_COUNT + 1
        val songNumber = (index - 1) % SONGS_PER_MUSIC_COUNT + 1

        return getSongDomain(musicIndex = musicNumber, songIndex = songNumber)
    }

    /**
     * Returns song for indexes.
     *
     * @param musicIndex music index
     * @param songIndex  song index
     * @return song for indexes
     */
    private fun getSongDomain(musicIndex: Int, songIndex: Int): com.github.vhromada.catalog.domain.Song {
        return com.github.vhromada.catalog.domain.Song(
            id = (musicIndex - 1) * SONGS_PER_MUSIC_COUNT + songIndex,
            name = "Music $musicIndex Song $songIndex",
            length = songIndex * LENGTH_MULTIPLIERS[musicIndex - 1],
            note = if (songIndex == 2) "Music $musicIndex Song 2 note" else "",
            position = songIndex + 9
        ).fillAudit(audit = AuditUtils.getAudit())
    }

    /**
     * Returns song.
     *
     * @param entityManager entity manager
     * @param id            song ID
     * @return song
     */
    fun getSong(entityManager: EntityManager, id: Int): com.github.vhromada.catalog.domain.Song? {
        return entityManager.find(com.github.vhromada.catalog.domain.Song::class.java, id)
    }

    /**
     * Returns song with updated fields.
     *
     * @param entityManager entity manager
     * @param id            song ID
     * @return song with updated fields
     */
    fun updateSong(entityManager: EntityManager, id: Int): com.github.vhromada.catalog.domain.Song {
        val storedSong = getSong(entityManager = entityManager, id = id)!!
        val song = storedSong
            .updated()
            .copy(position = POSITION)
            .fillAudit(audit = storedSong)
        song.music = storedSong.music
        return song
    }

    /**
     * Returns count of songs.
     *
     * @param entityManager entity manager
     * @return count of songs
     */
    @Suppress("JpaQlInspection")
    fun getSongsCount(entityManager: EntityManager): Int {
        return entityManager.createQuery("SELECT COUNT(s.id) FROM Song s", java.lang.Long::class.java).singleResult.toInt()
    }

    /**
     * Asserts songs deep equals.
     *
     * @param expected expected list of songs
     * @param actual   actual list of songs
     */
    fun assertDomainSongsDeepEquals(expected: List<com.github.vhromada.catalog.domain.Song>, actual: List<com.github.vhromada.catalog.domain.Song>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertSongDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts song deep equals.
     *
     * @param expected expected song
     * @param actual   actual song
     */
    fun assertSongDeepEquals(expected: com.github.vhromada.catalog.domain.Song, actual: com.github.vhromada.catalog.domain.Song) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.name).isEqualTo(expected.name)
            it.assertThat(actual.length).isEqualTo(expected.length)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
            AuditUtils.assertAuditDeepEquals(softly = it, expected = expected, actual = actual)
        }
        if (expected.music != null) {
            assertThat(actual.music).isNotNull
            assertThat(actual.music!!.songs).hasSameSizeAs(expected.music!!.songs)
            MusicUtils.assertMusicDeepEquals(expected = expected.music!!, actual = actual.music!!, checkSongs = false)
        }
    }

    /**
     * Asserts songs deep equals.
     *
     * @param expected expected list of songs
     * @param actual   actual list of songs
     */
    fun assertSongsDeepEquals(expected: List<Song>, actual: List<com.github.vhromada.catalog.domain.Song>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertSongDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts song deep equals.
     *
     * @param expected expected song
     * @param actual   actual song
     */
    fun assertSongDeepEquals(expected: Song, actual: com.github.vhromada.catalog.domain.Song) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.name).isEqualTo(expected.name)
            it.assertThat(actual.length).isEqualTo(expected.length)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
            it.assertThat(actual.createdUser).isNull()
            it.assertThat(actual.createdTime).isNull()
            it.assertThat(actual.updatedUser).isNull()
            it.assertThat(actual.updatedTime).isNull()
            it.assertThat(actual.music).isNull()
        }
    }

    /**
     * Asserts songs deep equals.
     *
     * @param expected expected list of songs
     * @param actual   actual list of songs
     */
    fun assertSongListDeepEquals(expected: List<com.github.vhromada.catalog.domain.Song>, actual: List<Song>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertSongDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts song deep equals.
     *
     * @param expected expected song
     * @param actual   actual song
     */
    fun assertSongDeepEquals(expected: com.github.vhromada.catalog.domain.Song, actual: Song) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.name).isEqualTo(expected.name)
            it.assertThat(actual.length).isEqualTo(expected.length)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

}
