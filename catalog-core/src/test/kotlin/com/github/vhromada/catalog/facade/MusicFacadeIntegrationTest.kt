package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.utils.AuditUtils
import com.github.vhromada.catalog.utils.MusicUtils
import com.github.vhromada.catalog.utils.SongUtils
import com.github.vhromada.catalog.utils.fillAudit
import com.github.vhromada.common.entity.Time
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
 * A class represents integration test for class [MusicFacade].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
@Transactional
@Rollback
class MusicFacadeIntegrationTest {

    /**
     * Instance of [EntityManager]
     */
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [MusicFacade]
     */
    @Autowired
    private lateinit var facade: MusicFacade

    /**
     * Test method for [MusicFacade.get].
     */
    @Test
    fun get() {
        for (i in 1..MusicUtils.MUSIC_COUNT) {
            val result = facade.get(id = i)

            assertSoftly {
                it.assertThat(result.status).isEqualTo(Status.OK)
                it.assertThat(result.data).isNotNull
                it.assertThat(result.events()).isEmpty()
            }
            MusicUtils.assertMusicDeepEquals(expected = MusicUtils.getMusicDomain(index = i), actual = result.data!!)
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.get] with bad ID.
     */
    @Test
    fun getBadId() {
        val result = facade.get(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(MUSIC_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.update].
     */
    @Test
    fun update() {
        val music = MusicUtils.newMusic(id = 1)
        val storedMusic = MusicUtils.getMusicDomain(index = 1)
        val expectedMusic = MusicUtils.newMusicDomain(id = 1)
            .copy(songs = storedMusic.songs)
            .fillAudit(audit = AuditUtils.updatedAudit())

        val result = facade.update(data = music)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        MusicUtils.assertMusicDeepEquals(expected = expectedMusic, actual = MusicUtils.getMusic(entityManager = entityManager, id = 1)!!)

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.update] with music with null ID.
     */
    @Test
    fun updateNullId() {
        val music = MusicUtils.newMusic(id = 1)
            .copy(id = null)

        val result = facade.update(data = music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_ID_NULL", message = "ID mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.update] with music with null position.
     */
    @Test
    fun updateNullPosition() {
        val music = MusicUtils.newMusic(id = 1)
            .copy(position = null)

        val result = facade.update(data = music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_POSITION_NULL", message = "Position mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.update] with music with null name.
     */
    @Test
    fun updateNullName() {
        val music = MusicUtils.newMusic(id = 1)
            .copy(name = null)

        val result = facade.update(data = music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_NAME_NULL", message = "Name mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.update] with music with empty string as name.
     */
    @Test
    fun updateEmptyName() {
        val music = MusicUtils.newMusic(id = 1)
            .copy(name = "")

        val result = facade.update(data = music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.update] with music with null URL to english Wikipedia about music.
     */
    @Test
    fun updateNullWikiEn() {
        val music = MusicUtils.newMusic(id = 1)
            .copy(wikiEn = null)

        val result = facade.update(data = music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_WIKI_EN_NULL", message = "URL to english Wikipedia page about music mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.update] with music with null URL to czech Wikipedia about music.
     */
    @Test
    fun updateNullWikiCz() {
        val music = MusicUtils.newMusic(id = 1)
            .copy(wikiCz = null)

        val result = facade.update(data = music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_WIKI_CZ_NULL", message = "URL to czech Wikipedia page about music mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.update] with music with null count of media.
     */
    @Test
    fun updateNullMediaCount() {
        val music = MusicUtils.newMusic(id = 1)
            .copy(mediaCount = null)

        val result = facade.update(data = music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_MEDIA_COUNT_NULL", message = "Count of media mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.update] with music with not positive count of media.
     */
    @Test
    fun updateNotPositiveMediaCount() {
        val music = MusicUtils.newMusic(id = 1)
            .copy(mediaCount = 0)

        val result = facade.update(data = music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_MEDIA_COUNT_NOT_POSITIVE", message = "Count of media must be positive number.")))
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.update] with music with null note.
     */
    @Test
    fun updateNullNote() {
        val music = MusicUtils.newMusic(id = 1)
            .copy(note = null)

        val result = facade.update(data = music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_NOTE_NULL", message = "Note mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.update] with music with bad ID.
     */
    @Test
    fun updateBadId() {
        val result = facade.update(data = MusicUtils.newMusic(id = Int.MAX_VALUE))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(MUSIC_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.remove].
     */
    @Test
    fun remove() {
        val result = facade.remove(id = 1)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        assertThat(MusicUtils.getMusic(entityManager = entityManager, id = 1)).isNull()

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT - 1)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT - SongUtils.SONGS_PER_MUSIC_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.remove] with music with bad ID.
     */
    @Test
    fun removeBadId() {
        val result = facade.remove(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(MUSIC_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.duplicate].
     */
    @Test
    @DirtiesContext
    fun duplicate() {
        var expectedMusic = MusicUtils.getMusicDomain(index = MusicUtils.MUSIC_COUNT)
        val expectedSongs = expectedMusic.songs.mapIndexed { index, song ->
            song.copy(id = SongUtils.SONGS_COUNT + index + 1)
                .fillAudit(audit = AuditUtils.newAudit())
        }.toMutableList()
        expectedMusic = expectedMusic.copy(id = MusicUtils.MUSIC_COUNT + 1, songs = expectedSongs)
            .fillAudit(audit = AuditUtils.newAudit())

        val result = facade.duplicate(id = MusicUtils.MUSIC_COUNT)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        MusicUtils.assertMusicDeepEquals(expected = expectedMusic, actual = MusicUtils.getMusic(entityManager = entityManager, id = MusicUtils.MUSIC_COUNT + 1)!!)

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT + 1)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT + SongUtils.SONGS_PER_MUSIC_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.duplicate] with music with bad ID.
     */
    @Test
    fun duplicateBadId() {
        val result = facade.duplicate(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(MUSIC_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.moveUp].
     */
    @Test
    fun moveUp() {
        val result = facade.moveUp(id = 2)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        val music1 = MusicUtils.getMusicDomain(index = 1)
            .copy(position = 11)
            .fillAudit(audit = AuditUtils.updatedAudit())
        val music2 = MusicUtils.getMusicDomain(index = 2)
            .copy(position = 10)
            .fillAudit(audit = AuditUtils.updatedAudit())
        MusicUtils.assertMusicDeepEquals(expected = music1, actual = MusicUtils.getMusic(entityManager = entityManager, id = 1)!!)
        MusicUtils.assertMusicDeepEquals(expected = music2, actual = MusicUtils.getMusic(entityManager = entityManager, id = 2)!!)
        for (i in 3..MusicUtils.MUSIC_COUNT) {
            MusicUtils.assertMusicDeepEquals(expected = MusicUtils.getMusicDomain(i), actual = MusicUtils.getMusic(entityManager = entityManager, id = i)!!)
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.moveUp] with not movable music.
     */
    @Test
    fun moveUpNotMovable() {
        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_NOT_MOVABLE", message = "Music can't be moved up.")))
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.moveUp] with music with bad ID.
     */
    @Test
    fun moveUpBadId() {
        val result = facade.moveUp(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(MUSIC_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.moveDown].
     */
    @Test
    fun moveDown() {
        val result = facade.moveDown(id = 1)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        val music1 = MusicUtils.getMusicDomain(index = 1)
            .copy(position = 11)
            .fillAudit(audit = AuditUtils.updatedAudit())
        val music2 = MusicUtils.getMusicDomain(index = 2)
            .copy(position = 10)
            .fillAudit(audit = AuditUtils.updatedAudit())
        MusicUtils.assertMusicDeepEquals(expected = music1, actual = MusicUtils.getMusic(entityManager = entityManager, id = 1)!!)
        MusicUtils.assertMusicDeepEquals(expected = music2, actual = MusicUtils.getMusic(entityManager = entityManager, id = 2)!!)
        for (i in 3..MusicUtils.MUSIC_COUNT) {
            MusicUtils.assertMusicDeepEquals(expected = MusicUtils.getMusicDomain(i), actual = MusicUtils.getMusic(entityManager = entityManager, id = i)!!)
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.moveDown] with not movable music.
     */
    @Test
    fun moveDownNotMovable() {
        val result = facade.moveDown(id = MusicUtils.MUSIC_COUNT)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_NOT_MOVABLE", message = "Music can't be moved down.")))
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.moveDown] with music with bad ID.
     */
    @Test
    fun moveDownBadId() {
        val result = facade.moveDown(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(MUSIC_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.newData].
     */
    @Test
    fun newData() {
        val result = facade.newData()
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(0)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(0)
        }
    }

    /**
     * Test method for [MusicFacade.getAll].
     */
    @Test
    fun getAll() {
        val result = facade.getAll()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isNotNull
            it.assertThat(result.events()).isEmpty()
        }
        MusicUtils.assertMusicListDeepEquals(expected = MusicUtils.getMusicList(), actual = result.data!!)

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.add].
     */
    @Test
    @DirtiesContext
    fun add() {
        val expectedMusic = MusicUtils.newMusicDomain(id = MusicUtils.MUSIC_COUNT + 1)
            .fillAudit(audit = AuditUtils.newAudit())

        val result = facade.add(MusicUtils.newMusic(id = null))
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        MusicUtils.assertMusicDeepEquals(expected = expectedMusic, actual = MusicUtils.getMusic(entityManager = entityManager, id = MusicUtils.MUSIC_COUNT + 1)!!)

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT + 1)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.add] with music with not null ID.
     */
    @Test
    fun addNotNullId() {
        val music = MusicUtils.newMusic(id = null)
            .copy(id = Int.MAX_VALUE)

        val result = facade.add(data = music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_ID_NOT_NULL", message = "ID must be null.")))
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.add] with music with not null position.
     */
    @Test
    fun addNotNullPosition() {
        val music = MusicUtils.newMusic(id = null)
            .copy(position = Int.MAX_VALUE)

        val result = facade.add(data = music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_POSITION_NOT_NULL", message = "Position must be null.")))
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.add] with music with null name.
     */
    @Test
    fun addNullName() {
        val music = MusicUtils.newMusic(id = null)
            .copy(name = null)

        val result = facade.add(data = music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_NAME_NULL", message = "Name mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.add] with music with empty string as name.
     */
    @Test
    fun addEmptyName() {
        val music = MusicUtils.newMusic(id = null)
            .copy(name = "")

        val result = facade.add(data = music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.add] with music with null URL to english Wikipedia about music.
     */
    @Test
    fun addNullWikiEn() {
        val music = MusicUtils.newMusic(id = null)
            .copy(wikiEn = null)

        val result = facade.add(data = music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_WIKI_EN_NULL", message = "URL to english Wikipedia page about music mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.add] with music with null URL to czech Wikipedia about music.
     */
    @Test
    fun addNullWikiCz() {
        val music = MusicUtils.newMusic(id = null)
            .copy(wikiCz = null)

        val result = facade.add(data = music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_WIKI_CZ_NULL", message = "URL to czech Wikipedia page about music mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.add] with music with null count of media.
     */
    @Test
    fun addNullMediaCount() {
        val music = MusicUtils.newMusic(id = null)
            .copy(mediaCount = null)

        val result = facade.add(data = music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_MEDIA_COUNT_NULL", message = "Count of media mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.add] with music with not positive count of media.
     */
    @Test
    fun addNotPositiveMediaCount() {
        val music = MusicUtils.newMusic(id = null)
            .copy(mediaCount = 0)

        val result = facade.add(data = music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_MEDIA_COUNT_NOT_POSITIVE", message = "Count of media must be positive number.")))
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.add] with music with null note.
     */
    @Test
    fun addNullNote() {
        val music = MusicUtils.newMusic(id = null)
            .copy(note = null)

        val result = facade.add(data = music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_NOTE_NULL", message = "Note mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.updatePositions].
     */
    @Test
    fun updatePositions() {
        val result = facade.updatePositions()
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        for (i in 1..MusicUtils.MUSIC_COUNT) {
            var expectedMusic = MusicUtils.getMusicDomain(index = i)
            val expectedSongs = expectedMusic.songs.mapIndexed { index, song ->
                song.copy(position = index)
                    .fillAudit(audit = AuditUtils.updatedAudit())
            }.toMutableList()
            expectedMusic = expectedMusic.copy(position = i - 1, songs = expectedSongs)
                .fillAudit(audit = AuditUtils.updatedAudit())
            MusicUtils.assertMusicDeepEquals(expected = expectedMusic, actual = MusicUtils.getMusic(entityManager = entityManager, id = i)!!)
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.getTotalMediaCount].
     */
    @Test
    fun getTotalMediaCount() {
        val result = facade.getTotalMediaCount()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(60)
            it.assertThat(result.events()).isEmpty()
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.getTotalLength].
     */
    @Test
    fun getTotalLength() {
        val result = facade.getTotalLength()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(Time(length = 666))
            it.assertThat(result.events()).isEmpty()
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for [MusicFacade.getSongsCount].
     */
    @Test
    fun getSongsCount() {
        val result = facade.getSongsCount()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(result.events()).isEmpty()
        }

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    companion object {

        /**
         * Event for not existing music
         */
        private val MUSIC_NOT_EXIST_EVENT = Event(severity = Severity.ERROR, key = "MUSIC_NOT_EXIST", message = "Music doesn't exist.")

    }

}
