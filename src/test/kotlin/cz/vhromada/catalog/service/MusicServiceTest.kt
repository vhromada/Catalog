package cz.vhromada.catalog.service

import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import cz.vhromada.catalog.domain.Music
import cz.vhromada.catalog.repository.MusicRepository
import cz.vhromada.catalog.utils.MusicUtils
import cz.vhromada.catalog.utils.SongUtils
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.test.service.MovableServiceTest
import org.mockito.Mock
import org.springframework.data.jpa.repository.JpaRepository

/**
 * A class represents test for class [MusicService].
 *
 * @author Vladimir Hromada
 */
class MusicServiceTest : MovableServiceTest<Music>() {

    /**
     * Instance of [MusicRepository]
     */
    @Mock
    private lateinit var repository: MusicRepository

    override fun getRepository(): JpaRepository<Music, Int> {
        return repository
    }

    override fun getService(): MovableService<Music> {
        return MusicService(repository, cache)
    }

    override fun getCacheKey(): String {
        return "music"
    }

    override fun getItem1(): Music {
        return setSongs(MusicUtils.newMusicDomain(1))
    }

    override fun getItem2(): Music {
        return setSongs(MusicUtils.newMusicDomain(2))
    }

    override fun getAddItem(): Music {
        return MusicUtils.newMusicDomain(null)
    }

    override fun getCopyItem(): Music {
        val music = MusicUtils.newMusicDomain(null)
        music.position = 0

        return setSongs(music)
    }

    override fun anyItem(): Music {
        return any()
    }

    override fun argumentCaptorItem(): KArgumentCaptor<Music> {
        return argumentCaptor()
    }

    override fun assertDataDeepEquals(expected: Music, actual: Music) {
        MusicUtils.assertMusicDeepEquals(expected, actual)
    }

    /**
     * Sets songs to music.
     *
     * @param music music
     * @return music with songs
     */
    private fun setSongs(music: Music): Music {
        val id = music.id
        val song = SongUtils.newSongDomain(id)
        if (id == null) {
            song.position = 0
        }

        return music.copy(songs = listOf(song))
    }

}
