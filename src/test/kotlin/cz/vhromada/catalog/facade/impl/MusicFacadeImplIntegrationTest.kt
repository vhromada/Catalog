package cz.vhromada.catalog.facade.impl

import cz.vhromada.catalog.CatalogTestConfiguration
import cz.vhromada.catalog.entity.Music
import cz.vhromada.catalog.facade.MusicFacade
import cz.vhromada.catalog.utils.MusicUtils
import cz.vhromada.catalog.utils.SongUtils
import cz.vhromada.common.Time
import cz.vhromada.common.facade.MovableParentFacade
import cz.vhromada.common.test.facade.MovableParentFacadeIntegrationTest
import cz.vhromada.validation.result.Event
import cz.vhromada.validation.result.Severity
import cz.vhromada.validation.result.Status
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.test.context.ContextConfiguration
import javax.persistence.EntityManager

/**
 * A class represents integration test for class [MusicFacadeImpl].
 *
 * @author Vladimir Hromada
 */
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
class MusicFacadeImplIntegrationTest : MovableParentFacadeIntegrationTest<Music, cz.vhromada.catalog.domain.Music>() {

    /**
     * Instance of [EntityManager]
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [MusicFacade]
     */
    @Autowired
    private lateinit var facade: MusicFacade

    /**
     * Test method for [MusicFacade.add] with music with null name.
     */
    @Test
    fun add_NullName() {
        val music = newData(null)
                .copy(name = null)

        val result = facade.add(music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MUSIC_NAME_NULL", "Name mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MusicFacade.add] with music with empty string as name.
     */
    @Test
    fun add_EmptyName() {
        val music = newData(null)
                .copy(name = "")

        val result = facade.add(music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MUSIC_NAME_EMPTY", "Name mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MusicFacade.add] with music with null URL to english Wikipedia about music.
     */
    @Test
    fun add_NullWikiEn() {
        val music = newData(null)
                .copy(wikiEn = null)

        val result = facade.add(music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MUSIC_WIKI_EN_NULL",
                    "URL to english Wikipedia page about music mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MusicFacade.add] with music with null URL to czech Wikipedia about music.
     */
    @Test
    fun add_NullWikiCz() {
        val music = newData(null)
                .copy(wikiCz = null)

        val result = facade.add(music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MUSIC_WIKI_CZ_NULL",
                    "URL to czech Wikipedia page about music mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MusicFacade.add] with music with null count of media.
     */
    @Test
    fun add_NullMediaCount() {
        val music = newData(null)
                .copy(mediaCount = null)

        val result = facade.add(music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MUSIC_MEDIA_COUNT_NULL", "Count of media mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MusicFacade.add] with music with not positive count of media.
     */
    @Test
    fun add_NotPositiveMediaCount() {
        val music = newData(null)
                .copy(mediaCount = 0)

        val result = facade.add(music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MUSIC_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MusicFacade.add] with music with null note.
     */
    @Test
    fun add_NullNote() {
        val music = newData(null)
                .copy(note = null)

        val result = facade.add(music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MUSIC_NOTE_NULL", "Note mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MusicFacade.update] with music with null name.
     */
    @Test
    fun update_NullName() {
        val music = newData(1)
                .copy(name = null)

        val result = facade.update(music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MUSIC_NAME_NULL", "Name mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MusicFacade.update] with music with empty string as name.
     */
    @Test
    fun update_EmptyName() {
        val music = newData(1)
                .copy(name = "")

        val result = facade.update(music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MUSIC_NAME_EMPTY", "Name mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MusicFacade.update] with music with null URL to english Wikipedia about music.
     */
    @Test
    fun update_NullWikiEn() {
        val music = newData(1)
                .copy(wikiEn = null)

        val result = facade.update(music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MUSIC_WIKI_EN_NULL",
                    "URL to english Wikipedia page about music mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MusicFacade.update] with music with null URL to czech Wikipedia about music.
     */
    @Test
    fun update_NullWikiCz() {
        val music = newData(1)
                .copy(wikiCz = null)

        val result = facade.update(music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MUSIC_WIKI_CZ_NULL",
                    "URL to czech Wikipedia page about music mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MusicFacade.update] with music with null count of media.
     */
    @Test
    fun update_NullMediaCount() {
        val music = newData(1)
                .copy(mediaCount = null)

        val result = facade.update(music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MUSIC_MEDIA_COUNT_NULL", "Count of media mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MusicFacade.update] with music with not positive count of media.
     */
    @Test
    fun update_NotPositiveMediaCount() {
        val music = newData(1)
                .copy(mediaCount = 0)

        val result = facade.update(music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MUSIC_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MusicFacade.update] with music with null note.
     */
    @Test
    fun update_NullNote() {
        val music = newData(1)
                .copy(note = null)

        val result = facade.update(music)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MUSIC_NOTE_NULL", "Note mustn't be null.")))
        }

        assertDefaultRepositoryData()
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

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MusicFacade.getTotalLength].
     */
    @Test
    fun getTotalLength() {
        val result = facade.getTotalLength()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(Time(666))
            it.assertThat(result.events()).isEmpty()
        }

        assertDefaultRepositoryData()
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

        assertDefaultRepositoryData()
    }

    override fun getFacade(): MovableParentFacade<Music> {
        return facade
    }

    override fun getDefaultDataCount(): Int {
        return MusicUtils.MUSIC_COUNT
    }

    override fun getRepositoryDataCount(): Int {
        return MusicUtils.getMusicCount(entityManager)
    }

    override fun getDataList(): List<cz.vhromada.catalog.domain.Music> {
        return MusicUtils.getMusic()
    }

    override fun getDomainData(index: Int): cz.vhromada.catalog.domain.Music {
        return MusicUtils.getMusic(index)
    }

    override fun newData(id: Int?): Music {
        return MusicUtils.newMusic(id)
    }

    override fun newDomainData(id: Int): cz.vhromada.catalog.domain.Music {
        return MusicUtils.newMusicDomain(id)
    }

    override fun getRepositoryData(id: Int): cz.vhromada.catalog.domain.Music? {
        return MusicUtils.getMusic(entityManager, id)
    }

    override fun getName(): String {
        return "Music"
    }

    override fun clearReferencedData() {}

    override fun assertDataListDeepEquals(expected: List<Music>, actual: List<cz.vhromada.catalog.domain.Music>) {
        MusicUtils.assertMusicListDeepEquals(expected, actual)
    }

    override fun assertDataDeepEquals(expected: Music, actual: cz.vhromada.catalog.domain.Music) {
        MusicUtils.assertMusicDeepEquals(expected, actual)
    }

    override fun assertDataDomainDeepEquals(expected: cz.vhromada.catalog.domain.Music, actual: cz.vhromada.catalog.domain.Music) {
        MusicUtils.assertMusicDeepEquals(expected, actual)
    }

    override fun assertDefaultRepositoryData() {
        super.assertDefaultRepositoryData()

        assertReferences()
    }

    override fun assertNewRepositoryData() {
        super.assertNewRepositoryData()

        assertThat(SongUtils.getSongsCount(entityManager)).isEqualTo(0)
    }

    override fun assertAddRepositoryData() {
        super.assertAddRepositoryData()

        assertReferences()
    }

    override fun assertUpdateRepositoryData() {
        super.assertUpdateRepositoryData()

        assertReferences()
    }

    override fun assertRemoveRepositoryData() {
        super.assertRemoveRepositoryData()

        assertThat(SongUtils.getSongsCount(entityManager)).isEqualTo(SongUtils.SONGS_COUNT - SongUtils.SONGS_PER_MUSIC_COUNT)
    }

    override fun assertDuplicateRepositoryData() {
        super.assertDuplicateRepositoryData()

        assertThat(SongUtils.getSongsCount(entityManager)).isEqualTo(SongUtils.SONGS_COUNT + SongUtils.SONGS_PER_MUSIC_COUNT)
    }

    override fun getExpectedDuplicatedData(): cz.vhromada.catalog.domain.Music {
        val music = super.getExpectedDuplicatedData()
        for (song in music.songs) {
            song.id = SongUtils.SONGS_COUNT + music.songs.indexOf(song) + 1
        }

        return music
    }

    /**
     * Asserts references.
     */
    private fun assertReferences() {
        assertThat(SongUtils.getSongsCount(entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
    }

}
