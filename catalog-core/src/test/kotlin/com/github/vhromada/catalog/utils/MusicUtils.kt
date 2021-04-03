package com.github.vhromada.catalog.utils

import com.github.vhromada.catalog.entity.Music
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import java.util.ArrayList
import javax.persistence.EntityManager

/**
 * Updates music fields.
 *
 * @return updated music
 */
fun com.github.vhromada.catalog.domain.Music.updated(): com.github.vhromada.catalog.domain.Music {
    return copy(name = "Name", wikiEn = "enWiki", wikiCz = "czWiki", mediaCount = 1, note = "Note")
}

/**
 * Updates music fields.
 *
 * @return updated music
 */
fun Music.updated(): Music {
    return copy(name = "Name", wikiEn = "enWiki", wikiCz = "czWiki", mediaCount = 1, note = "Note")
}

/**
 * A class represents utility class for music.
 *
 * @author Vladimir Hromada
 */
object MusicUtils {

    /**
     * Count of music
     */
    const val MUSIC_COUNT = 3

    /**
     * Position
     */
    const val POSITION = 10

    /**
     * Music name
     */
    private const val MUSIC = "Music "

    /**
     * Returns music.
     *
     * @return music
     */
    fun getMusicList(): List<com.github.vhromada.catalog.domain.Music> {
        val music = ArrayList<com.github.vhromada.catalog.domain.Music>()
        for (i in 1..MUSIC_COUNT) {
            music.add(getMusicDomain(index = i))
        }

        return music
    }

    /**
     * Returns music.
     *
     * @param id ID
     * @return music
     */
    fun newMusicDomain(id: Int?): com.github.vhromada.catalog.domain.Music {
        return com.github.vhromada.catalog.domain.Music(
            id = id,
            name = "",
            wikiEn = null,
            wikiCz = null,
            mediaCount = 0,
            note = null,
            position = if (id == null) null else id - 1,
            songs = mutableListOf()
        ).updated()
    }

    /**
     * Returns music with songs.
     *
     * @param id ID
     * @return music with songs
     */
    fun newMusicDomainWithSongs(id: Int?): com.github.vhromada.catalog.domain.Music {
        return newMusicDomain(id = id)
            .copy(songs = mutableListOf(SongUtils.newSongDomain(id = id)))
    }

    /**
     * Returns music.
     *
     * @param id ID
     * @return music
     */
    fun newMusic(id: Int?): Music {
        return Music(
            id = id,
            name = "",
            wikiEn = null,
            wikiCz = null,
            mediaCount = 0,
            note = null,
            position = if (id == null) null else id - 1
        ).updated()
    }

    /**
     * Returns music for index.
     *
     * @param index index
     * @return music for index
     */
    fun getMusicDomain(index: Int): com.github.vhromada.catalog.domain.Music {
        return com.github.vhromada.catalog.domain.Music(
            id = index,
            name = "$MUSIC$index name",
            wikiEn = "$MUSIC$index English Wikipedia",
            wikiCz = "$MUSIC$index Czech Wikipedia",
            mediaCount = index * 10,
            note = if (index == 2) MUSIC + "2 note" else "",
            position = index + 9,
            songs = SongUtils.getSongs(music = index)
        ).fillAudit(audit = AuditUtils.getAudit())
    }

    /**
     * Returns music.
     *
     * @param entityManager entity manager
     * @param id            music ID
     * @return music
     */
    fun getMusic(entityManager: EntityManager, id: Int): com.github.vhromada.catalog.domain.Music? {
        return entityManager.find(com.github.vhromada.catalog.domain.Music::class.java, id)
    }

    /**
     * Returns music with updated fields.
     *
     * @param id            music ID
     * @param entityManager entity manager
     * @return music with updated fields
     */
    fun updateMusic(entityManager: EntityManager, id: Int): com.github.vhromada.catalog.domain.Music {
        val music = getMusic(entityManager = entityManager, id = id)!!
        return music
            .updated()
            .copy(position = POSITION)
            .fillAudit(audit = music)
    }

    /**
     * Returns count of music.
     *
     * @param entityManager entity manager
     * @return count of music
     */
    @Suppress("JpaQlInspection")
    fun getMusicCount(entityManager: EntityManager): Int {
        return entityManager.createQuery("SELECT COUNT(m.id) FROM Music m", java.lang.Long::class.java).singleResult.toInt()
    }

    /**
     * Asserts music deep equals.
     *
     * @param expected expected list of music
     * @param actual   actual list of music
     */
    fun assertDomainMusicDeepEquals(expected: List<com.github.vhromada.catalog.domain.Music>, actual: List<com.github.vhromada.catalog.domain.Music>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertMusicDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts music deep equals.
     *
     * @param expected   expected music
     * @param actual     actual music
     * @param checkSongs true if songs should be checked
     */
    fun assertMusicDeepEquals(expected: com.github.vhromada.catalog.domain.Music, actual: com.github.vhromada.catalog.domain.Music, checkSongs: Boolean = true) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.name).isEqualTo(expected.name)
            it.assertThat(actual.wikiEn).isEqualTo(expected.wikiEn)
            it.assertThat(actual.wikiCz).isEqualTo(expected.wikiCz)
            it.assertThat(actual.mediaCount).isEqualTo(expected.mediaCount)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
            AuditUtils.assertAuditDeepEquals(softly = it, expected = expected, actual = actual)
        }
        if (checkSongs) {
            SongUtils.assertDomainSongsDeepEquals(expected = expected.songs, actual = actual.songs)
        }
    }

    /**
     * Asserts music deep equals.
     *
     * @param expected expected list of music
     * @param actual   actual list of music
     */
    fun assertMusicDeepEquals(expected: List<Music>, actual: List<com.github.vhromada.catalog.domain.Music>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertMusicDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts music deep equals.
     *
     * @param expected expected music
     * @param actual   actual music
     */
    fun assertMusicDeepEquals(expected: Music, actual: com.github.vhromada.catalog.domain.Music) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.name).isEqualTo(expected.name)
            it.assertThat(actual.wikiEn).isEqualTo(expected.wikiEn)
            it.assertThat(actual.wikiCz).isEqualTo(expected.wikiCz)
            it.assertThat(actual.mediaCount).isEqualTo(expected.mediaCount)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
            it.assertThat(actual.songs).isEmpty()
            it.assertThat(actual.createdUser).isNull()
            it.assertThat(actual.createdTime).isNull()
            it.assertThat(actual.updatedUser).isNull()
            it.assertThat(actual.updatedTime).isNull()
        }
    }

    /**
     * Asserts music deep equals.
     *
     * @param expected expected list of music
     * @param actual   actual list of music
     */
    fun assertMusicListDeepEquals(expected: List<com.github.vhromada.catalog.domain.Music>, actual: List<Music>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertMusicDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts music deep equals.
     *
     * @param expected expected music
     * @param actual   actual music
     */
    fun assertMusicDeepEquals(expected: com.github.vhromada.catalog.domain.Music, actual: Music) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.name).isEqualTo(expected.name)
            it.assertThat(actual.wikiEn).isEqualTo(expected.wikiEn)
            it.assertThat(actual.wikiCz).isEqualTo(expected.wikiCz)
            it.assertThat(actual.mediaCount).isEqualTo(expected.mediaCount)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

}
