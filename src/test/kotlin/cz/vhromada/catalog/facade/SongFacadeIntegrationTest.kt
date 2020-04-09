package cz.vhromada.catalog.facade

import cz.vhromada.catalog.CatalogTestConfiguration
import cz.vhromada.catalog.entity.Music
import cz.vhromada.catalog.entity.Song
import cz.vhromada.catalog.utils.MusicUtils
import cz.vhromada.catalog.utils.SongUtils
import cz.vhromada.common.facade.MovableChildFacade
import cz.vhromada.common.result.Event
import cz.vhromada.common.result.Severity
import cz.vhromada.common.result.Status
import cz.vhromada.common.test.facade.MovableChildFacadeIntegrationTest
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.test.context.ContextConfiguration
import javax.persistence.EntityManager

/**
 * A class represents integration test for class [SongFacade].
 *
 * @author Vladimir Hromada
 */
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
class SongFacadeIntegrationTest : MovableChildFacadeIntegrationTest<Song, cz.vhromada.catalog.domain.Song, Music>() {

    /**
     * Instance of [EntityManager]
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private lateinit var entityManager: EntityManager

    /**
     * Instance of (@link SongFacade}
     */
    @Autowired
    private lateinit var facade: SongFacade

    /**
     * Test method for [SongFacade.add] with song with null name.
     */
    @Test
    fun addNullName() {
        val song = newChildData(null)
                .copy(name = null)

        val result = facade.add(MusicUtils.newMusic(1), song)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SONG_NAME_NULL", "Name mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SongFacade.add] with song with empty string as name.
     */
    @Test
    fun addEmptyName() {
        val song = newChildData(null)
                .copy(name = "")

        val result = facade.add(MusicUtils.newMusic(1), song)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SONG_NAME_EMPTY", "Name mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SongFacade.add] with song with null length.
     */
    @Test
    fun addNullLength() {
        val song = newChildData(null)
                .copy(length = null)

        val result = facade.add(MusicUtils.newMusic(1), song)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SONG_LENGTH_NULL", "Length of song mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SongFacade.add] with song with negative length.
     */
    @Test
    fun addNegativeLength() {
        val song = newChildData(null)
                .copy(length = -1)

        val result = facade.add(MusicUtils.newMusic(1), song)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SONG_LENGTH_NEGATIVE", "Length of song mustn't be negative number.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SongFacade.add] with song with null note.
     */
    @Test
    fun addNullNote() {
        val song = newChildData(null)
                .copy(note = null)

        val result = facade.add(MusicUtils.newMusic(1), song)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SONG_NOTE_NULL", "Note mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SongFacade.update] with song with null name.
     */
    @Test
    fun updateNullName() {
        val song = newChildData(1)
                .copy(name = null)

        val result = facade.update(song)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SONG_NAME_NULL", "Name mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SongFacade.update] with song with empty string as name.
     */
    @Test
    fun updateEmptyName() {
        val song = newChildData(1)
                .copy(name = "")

        val result = facade.update(song)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SONG_NAME_EMPTY", "Name mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SongFacade.update] with song with null length.
     */
    @Test
    fun updateNullLength() {
        val song = newChildData(1)
                .copy(length = null)

        val result = facade.update(song)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SONG_LENGTH_NULL", "Length of song mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SongFacade.update] with song with negative length.
     */
    @Test
    fun updateNegativeLength() {
        val song = newChildData(1)
                .copy(length = -1)

        val result = facade.update(song)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SONG_LENGTH_NEGATIVE", "Length of song mustn't be negative number.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [SongFacade.update] with song with null note.
     */
    @Test
    fun updateNullNote() {
        val song = newChildData(1)
                .copy(note = null)

        val result = facade.update(song)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SONG_NOTE_NULL", "Note mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    override fun getFacade(): MovableChildFacade<Song, Music> {
        return facade
    }

    override fun getDefaultParentDataCount(): Int {
        return MusicUtils.MUSIC_COUNT
    }

    override fun getDefaultChildDataCount(): Int {
        return SongUtils.SONGS_COUNT
    }

    override fun getRepositoryParentDataCount(): Int {
        return MusicUtils.getMusicCount(entityManager)
    }

    override fun getRepositoryChildDataCount(): Int {
        return SongUtils.getSongsCount(entityManager)
    }

    override fun getDataList(parentId: Int): List<cz.vhromada.catalog.domain.Song> {
        return SongUtils.getSongs(parentId)
    }

    override fun getDomainData(index: Int): cz.vhromada.catalog.domain.Song {
        return SongUtils.getSong(index)
    }

    override fun newParentData(id: Int?): Music {
        return MusicUtils.newMusic(id)
    }

    override fun newChildData(id: Int?): Song {
        return SongUtils.newSong(id)
    }

    override fun newDomainData(id: Int): cz.vhromada.catalog.domain.Song {
        return SongUtils.newSongDomain(id)
    }

    override fun getRepositoryData(id: Int): cz.vhromada.catalog.domain.Song? {
        return SongUtils.getSong(entityManager, id)
    }

    override fun getParentName(): String {
        return "Music"
    }

    override fun getChildName(): String {
        return "Song"
    }

    override fun assertDataListDeepEquals(expected: List<Song>, actual: List<cz.vhromada.catalog.domain.Song>) {
        SongUtils.assertSongListDeepEquals(expected, actual)
    }

    override fun assertDataDeepEquals(expected: Song, actual: cz.vhromada.catalog.domain.Song) {
        SongUtils.assertSongDeepEquals(expected, actual)

    }

    override fun assertDataDomainDeepEquals(expected: cz.vhromada.catalog.domain.Song, actual: cz.vhromada.catalog.domain.Song) {
        SongUtils.assertSongDeepEquals(expected, actual)
    }

}
