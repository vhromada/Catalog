package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.utils.AuditUtils
import com.github.vhromada.catalog.utils.MusicUtils
import com.github.vhromada.catalog.utils.SongUtils
import com.github.vhromada.catalog.utils.fillAudit
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.result.Status
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * A class represents integration test for class [SongFacade].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
@Transactional
@Rollback
class SongFacadeIntegrationTest {

    /**
     * Instance of [EntityManager]
     */
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [SongFacade]
     */
    @Autowired
    private lateinit var facade: SongFacade

    /**
     * Test method for [SongFacade.get].
     */
    @Test
    fun get() {
        for (i in 1..SongUtils.SONGS_COUNT) {
            val result = facade.get(id = i)

            assertSoftly {
                it.assertThat(result.status).isEqualTo(Status.OK)
                it.assertThat(result.data).isNotNull
                it.assertThat(result.events()).isEmpty()
            }
            SongUtils.assertSongDeepEquals(expected = SongUtils.getSongDomain(index = i), actual = result.data!!)
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.get] with bad ID.
     */
    @Test
    fun getBadId() {
        val result = facade.get(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(SONG_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.update].
     */
    @Test
    fun update() {
        val song = SongUtils.newSong(id = 1)
        val expectedSong = SongUtils.newSongDomain(id = 1)
            .fillAudit(audit = AuditUtils.updatedAudit())
        expectedSong.music = MusicUtils.getMusic(entityManager = entityManager, id = 1)

        val result = facade.update(data = song)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        SongUtils.assertSongDeepEquals(expected = expectedSong, actual = SongUtils.getSong(entityManager = entityManager, id = 1)!!)

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.update] with song with null ID.
     */
    @Test
    fun updateNullId() {
        val song = SongUtils.newSong(id = 1)
            .copy(id = null)

        val result = facade.update(data = song)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_ID_NULL", message = "ID mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.update] with song with null position.
     */
    @Test
    fun updateNullPosition() {
        val song = SongUtils.newSong(id = 1)
            .copy(position = null)

        val result = facade.update(data = song)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_POSITION_NULL", message = "Position mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.update] with song with null name.
     */
    @Test
    fun updateNullName() {
        val song = SongUtils.newSong(id = 1)
            .copy(name = null)

        val result = facade.update(data = song)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_NAME_NULL", message = "Name mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.update] with song with empty string as name.
     */
    @Test
    fun updateEmptyName() {
        val song = SongUtils.newSong(id = 1)
            .copy(name = "")

        val result = facade.update(data = song)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.update] with song with null length.
     */
    @Test
    fun updateNullLength() {
        val song = SongUtils.newSong(id = 1)
            .copy(length = null)

        val result = facade.update(data = song)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_LENGTH_NULL", message = "Length of song mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.update] with song with negative length.
     */
    @Test
    fun updateNegativeLength() {
        val song = SongUtils.newSong(id = 1)
            .copy(length = -1)

        val result = facade.update(data = song)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_LENGTH_NEGATIVE", message = "Length of song mustn't be negative number.")))
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.update] with song with null note.
     */
    @Test
    fun updateNullNote() {
        val song = SongUtils.newSong(id = 1)
            .copy(note = null)

        val result = facade.update(data = song)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_NOTE_NULL", message = "Note mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.update] with song with bad ID.
     */
    @Test
    fun updateBadId() {
        val result = facade.update(data = SongUtils.newSong(id = Int.MAX_VALUE))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(SONG_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.remove].
     */
    @Test
    fun remove() {
        val result = facade.remove(id = 1)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        assertThat(SongUtils.getSong(entityManager = entityManager, id = 1)).isNull()

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT - 1)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.remove] with song with bad ID.
     */
    @Test
    fun removeBadId() {
        val result = facade.remove(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(SONG_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.duplicate].
     */
    @Test
    @DirtiesContext
    fun duplicate() {
        val expectedSong = SongUtils.getSongDomain(index = SongUtils.SONGS_COUNT)
            .copy(id = SongUtils.SONGS_COUNT + 1)
            .fillAudit(audit = AuditUtils.newAudit())
        expectedSong.music = MusicUtils.getMusic(entityManager = entityManager, id = MusicUtils.MUSIC_COUNT)

        val result = facade.duplicate(id = SongUtils.SONGS_COUNT)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        SongUtils.assertSongDeepEquals(expected = expectedSong, actual = SongUtils.getSong(entityManager = entityManager, id = SongUtils.SONGS_COUNT + 1)!!)

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT + 1)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.duplicate] with song with bad ID.
     */
    @Test
    fun duplicateBadId() {
        val result = facade.duplicate(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(SONG_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.moveUp].
     */
    @Test
    fun moveUp() {
        val result = facade.moveUp(id = 2)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        val song1 = SongUtils.getSongDomain(index = 1)
            .copy(position = 11)
            .fillAudit(audit = AuditUtils.updatedAudit())
        val song2 = SongUtils.getSongDomain(index = 2)
            .copy(position = 10)
            .fillAudit(audit = AuditUtils.updatedAudit())
        SongUtils.assertSongDeepEquals(expected = song1, actual = SongUtils.getSong(entityManager = entityManager, id = 1)!!)
        SongUtils.assertSongDeepEquals(expected = song2, actual = SongUtils.getSong(entityManager = entityManager, id = 2)!!)
        for (i in 3..SongUtils.SONGS_COUNT) {
            SongUtils.assertSongDeepEquals(expected = SongUtils.getSongDomain(i), actual = SongUtils.getSong(entityManager = entityManager, id = i)!!)
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.moveUp] with not movable song.
     */
    @Test
    fun moveUpNotMovable() {
        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_NOT_MOVABLE", message = "Song can't be moved up.")))
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.moveUp] with song with bad ID.
     */
    @Test
    fun moveUpBadId() {
        val result = facade.moveUp(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(SONG_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.moveDown].
     */
    @Test
    fun moveDown() {
        val result = facade.moveDown(id = 1)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        val song1 = SongUtils.getSongDomain(index = 1)
            .copy(position = 11)
            .fillAudit(audit = AuditUtils.updatedAudit())
        val song2 = SongUtils.getSongDomain(index = 2)
            .copy(position = 10)
            .fillAudit(audit = AuditUtils.updatedAudit())
        SongUtils.assertSongDeepEquals(expected = song1, actual = SongUtils.getSong(entityManager = entityManager, id = 1)!!)
        SongUtils.assertSongDeepEquals(expected = song2, actual = SongUtils.getSong(entityManager = entityManager, id = 2)!!)
        for (i in 3..SongUtils.SONGS_COUNT) {
            SongUtils.assertSongDeepEquals(expected = SongUtils.getSongDomain(i), actual = SongUtils.getSong(entityManager = entityManager, id = i)!!)
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.moveDown] with not movable song.
     */
    @Test
    fun moveDownNotMovable() {
        val result = facade.moveDown(id = SongUtils.SONGS_COUNT)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_NOT_MOVABLE", message = "Song can't be moved down.")))
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.moveDown] with song with bad ID.
     */
    @Test
    fun moveDownBadId() {
        val result = facade.moveDown(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(SONG_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.add].
     */
    @Test
    @DirtiesContext
    fun add() {
        val expectedSong = SongUtils.newSongDomain(id = SongUtils.SONGS_COUNT + 1)
            .fillAudit(audit = AuditUtils.newAudit())
        expectedSong.music = MusicUtils.getMusic(entityManager = entityManager, id = 1)

        val result = facade.add(parent = 1, data = SongUtils.newSong(id = null))
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        SongUtils.assertSongDeepEquals(expected = expectedSong, actual = SongUtils.getSong(entityManager = entityManager, id = SongUtils.SONGS_COUNT + 1)!!)

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT + 1)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.add] with bad music ID.
     */
    @Test
    fun addBadMusicId() {
        val result = facade.add(parent = Int.MAX_VALUE, data = SongUtils.newSong(id = null))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(MUSIC_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.add] with song with not null ID.
     */
    @Test
    fun addNotNullId() {
        val song = SongUtils.newSong(id = null)
            .copy(id = Int.MAX_VALUE)

        val result = facade.add(parent = 1, data = song)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_ID_NOT_NULL", message = "ID must be null.")))
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.add] with song with not null position.
     */
    @Test
    fun addNotNullPosition() {
        val song = SongUtils.newSong(id = null)
            .copy(position = Int.MAX_VALUE)

        val result = facade.add(parent = 1, data = song)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_POSITION_NOT_NULL", message = "Position must be null.")))
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.add] with song with null name.
     */
    @Test
    fun addNullName() {
        val song = SongUtils.newSong(id = null)
            .copy(name = null)

        val result = facade.add(parent = 1, data = song)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_NAME_NULL", message = "Name mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.add] with song with empty string as name.
     */
    @Test
    fun addEmptyName() {
        val song = SongUtils.newSong(id = null)
            .copy(name = "")

        val result = facade.add(parent = 1, data = song)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.add] with song with null length.
     */
    @Test
    fun addNullLength() {
        val song = SongUtils.newSong(id = null)
            .copy(length = null)

        val result = facade.add(parent = 1, data = song)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_LENGTH_NULL", message = "Length of song mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.add] with song with negative length.
     */
    @Test
    fun addNegativeLength() {
        val song = SongUtils.newSong(id = null)
            .copy(length = -1)

        val result = facade.add(parent = 1, data = song)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_LENGTH_NEGATIVE", message = "Length of song mustn't be negative number.")))
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.add] with song with null note.
     */
    @Test
    fun addNullNote() {
        val song = SongUtils.newSong(id = null)
            .copy(note = null)

        val result = facade.add(parent = 1, data = song)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_NOTE_NULL", message = "Note mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.find].
     */
    @Test
    fun find() {
        for (i in 1..MusicUtils.MUSIC_COUNT) {
            val result = facade.find(parent = i)

            assertSoftly {
                it.assertThat(result.status).isEqualTo(Status.OK)
                it.assertThat(result.data).isNotNull
                it.assertThat(result.events()).isEmpty()
            }
            SongUtils.assertSongListDeepEquals(expected = SongUtils.getSongs(music = i), actual = result.data!!)
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for [SongFacade.find] with bad music ID.
     */
    @Test
    fun findBadMusicId() {
        val result = facade.find(parent = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(MUSIC_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    companion object {

        /**
         * Event for not existing song
         */
        private val SONG_NOT_EXIST_EVENT = Event(severity = Severity.ERROR, key = "SONG_NOT_EXIST", message = "Song doesn't exist.")

        /**
         * Event for not existing music
         */
        private val MUSIC_NOT_EXIST_EVENT = Event(severity = Severity.ERROR, key = "MUSIC_NOT_EXIST", message = "Music doesn't exist.")

    }

}
