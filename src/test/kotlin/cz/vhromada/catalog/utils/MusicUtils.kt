package cz.vhromada.catalog.utils

import cz.vhromada.catalog.entity.Music
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import java.util.ArrayList
import javax.persistence.EntityManager

/**
 * Updates music fields.
 *
 * @return updated music
 */
fun cz.vhromada.catalog.domain.Music.updated(): cz.vhromada.catalog.domain.Music {
    return copy(name = "Name", wikiEn = "enWiki", wikiCz = "czWiki", mediaCount = 1, note = "Note", audit = AuditUtils.newAudit())
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
    fun getMusic(): List<cz.vhromada.catalog.domain.Music> {
        val music = ArrayList<cz.vhromada.catalog.domain.Music>()
        for (i in 0 until MUSIC_COUNT) {
            music.add(getMusic(i + 1))
        }

        return music
    }

    /**
     * Returns music.
     *
     * @param id ID
     * @return music
     */
    fun newMusicDomain(id: Int?): cz.vhromada.catalog.domain.Music {
        return cz.vhromada.catalog.domain.Music(
                id = id,
                name = "",
                wikiEn = null,
                wikiCz = null,
                mediaCount = 0,
                note = null,
                position = if (id == null) null else id - 1,
                songs = emptyList(),
                audit = null)
                .updated()
    }

    /**
     * Returns music with songs.
     *
     * @param id ID
     * @return music with songs
     */
    fun newMusicWithSongs(id: Int?): cz.vhromada.catalog.domain.Music {
        return newMusicDomain(id)
                .copy(songs = listOf(SongUtils.newSongDomain(id)))
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
                position = if (id == null) null else id - 1)
                .updated()
    }

    /**
     * Returns music for index.
     *
     * @param index index
     * @return music for index
     */
    fun getMusic(index: Int): cz.vhromada.catalog.domain.Music {
        return cz.vhromada.catalog.domain.Music(
                id = index,
                name = "$MUSIC$index name",
                wikiEn = "$MUSIC$index English Wikipedia",
                wikiCz = "$MUSIC$index Czech Wikipedia",
                mediaCount = index * 10,
                note = if (index == 2) MUSIC + "2 note" else "",
                position = index - 1,
                songs = SongUtils.getSongs(index),
                audit = AuditUtils.getAudit())
    }

    /**
     * Returns music.
     *
     * @param entityManager entity manager
     * @param id            music ID
     * @return music
     */
    fun getMusic(entityManager: EntityManager, id: Int): cz.vhromada.catalog.domain.Music? {
        return entityManager.find(cz.vhromada.catalog.domain.Music::class.java, id)
    }

    /**
     * Returns music with updated fields.
     *
     * @param id            music ID
     * @param entityManager entity manager
     * @return music with updated fields
     */
    fun updateMusic(entityManager: EntityManager, id: Int): cz.vhromada.catalog.domain.Music {
        return getMusic(entityManager, id)!!
                .updated()
                .copy(position = POSITION)
    }

    /**
     * Returns count of music.
     *
     * @param entityManager entity manager
     * @return count of music
     */
    @Suppress("CheckStyle")
    fun getMusicCount(entityManager: EntityManager): Int {
        return entityManager.createQuery("SELECT COUNT(m.id) FROM Music m", java.lang.Long::class.java).singleResult.toInt()
    }

    /**
     * Asserts music deep equals.
     *
     * @param expected expected music
     * @param actual   actual music
     */
    fun assertMusicDeepEquals(expected: List<cz.vhromada.catalog.domain.Music?>?, actual: List<cz.vhromada.catalog.domain.Music?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertThat(expected!!.size).isEqualTo(actual!!.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertMusicDeepEquals(expected[i], actual[i])
            }
        }
    }

    /**
     * Asserts music deep equals.
     *
     * @param expected expected music
     * @param actual   actual music
     */
    fun assertMusicDeepEquals(expected: cz.vhromada.catalog.domain.Music?, actual: cz.vhromada.catalog.domain.Music?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(expected!!.id).isEqualTo(actual!!.id)
            it.assertThat(expected.name).isEqualTo(actual.name)
            it.assertThat(expected.wikiEn).isEqualTo(actual.wikiEn)
            it.assertThat(expected.wikiCz).isEqualTo(actual.wikiCz)
            it.assertThat(expected.mediaCount).isEqualTo(actual.mediaCount)
            it.assertThat(expected.note).isEqualTo(actual.note)
            it.assertThat(expected.position).isEqualTo(actual.position)
            SongUtils.assertSongsDeepEquals(expected.songs, actual.songs)
            AuditUtils.assertAuditDeepEquals(expected.audit, actual.audit)
        }
    }

    /**
     * Asserts music deep equals.
     *
     * @param expected expected list of music
     * @param actual   actual music
     */
    fun assertMusicListDeepEquals(expected: List<Music?>?, actual: List<cz.vhromada.catalog.domain.Music?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertThat(expected!!.size).isEqualTo(actual!!.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertMusicDeepEquals(expected[i], actual[i])
            }
        }
    }

    /**
     * Asserts music deep equals.
     *
     * @param expected expected music
     * @param actual   actual music
     */
    fun assertMusicDeepEquals(expected: Music?, actual: cz.vhromada.catalog.domain.Music?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.id).isEqualTo(expected!!.id)
            it.assertThat(actual.name).isEqualTo(expected.name)
            it.assertThat(actual.wikiEn).isEqualTo(expected.wikiEn)
            it.assertThat(actual.wikiCz).isEqualTo(expected.wikiCz)
            it.assertThat(actual.mediaCount).isEqualTo(expected.mediaCount)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

}
