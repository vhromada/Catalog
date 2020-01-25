package cz.vhromada.catalog.facade.impl

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import cz.vhromada.catalog.entity.Music
import cz.vhromada.catalog.facade.MusicFacade
import cz.vhromada.catalog.utils.MusicUtils
import cz.vhromada.common.Time
import cz.vhromada.common.facade.MovableParentFacade
import cz.vhromada.common.result.Status
import cz.vhromada.common.test.facade.MovableParentFacadeTest
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test

/**
 * A class represents test for class [MusicFacadeImpl].
 *
 * @author Vladimir Hromada
 */
class MusicFacadeImplTest : MovableParentFacadeTest<Music, cz.vhromada.catalog.domain.Music>() {

    /**
     * Test method for [MusicFacade.getTotalMediaCount].
     */
    @Test
    fun getTotalMediaCount() {
        val music1 = MusicUtils.newMusicDomain(1)
        val music2 = MusicUtils.newMusicDomain(2)
        val expectedCount = music1.mediaCount + music2.mediaCount

        whenever(service.getAll()).thenReturn(listOf(music1, music2))

        val result = (getFacade() as MusicFacade).getTotalMediaCount()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(expectedCount)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).getAll()
        verifyNoMoreInteractions(service)
        verifyZeroInteractions(mapper, validator)
    }

    /**
     * Test method for [MusicFacade.getTotalLength].
     */
    @Test
    fun getTotalLength() {
        val musicList = listOf(MusicUtils.newMusicWithSongs(1), MusicUtils.newMusicWithSongs(2))
        var totalLength = 0
        for (music in musicList) {
            for (song in music.songs) {
                totalLength += song.length
            }
        }
        val expectedTotalLength = totalLength

        whenever(service.getAll()).thenReturn(musicList)

        val result = (getFacade() as MusicFacade).getTotalLength()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(Time(expectedTotalLength))
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).getAll()
        verifyNoMoreInteractions(service)
        verifyZeroInteractions(mapper, validator)
    }

    /**
     * Test method for [MusicFacade.getSongsCount].
     */
    @Test
    fun getSongsCount() {
        val music1 = MusicUtils.newMusicWithSongs(1)
        val music2 = MusicUtils.newMusicWithSongs(2)
        val expectedSongs = music1.songs.size + music2.songs.size

        whenever(service.getAll()).thenReturn(listOf(music1, music2))

        val result = (getFacade() as MusicFacade).getSongsCount()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(expectedSongs)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).getAll()
        verifyNoMoreInteractions(service)
        verifyZeroInteractions(mapper, validator)
    }

    override fun initUpdateMock(domain: cz.vhromada.catalog.domain.Music) {
        super.initUpdateMock(domain)

        whenever(service.get(any())).thenReturn(domain)
    }

    override fun verifyUpdateMock(entity: Music, domain: cz.vhromada.catalog.domain.Music) {
        super.verifyUpdateMock(entity, domain)

        verify(service).get(entity.id!!)
    }

    override fun getFacade(): MovableParentFacade<Music> {
        return MusicFacadeImpl(service, mapper, validator)
    }

    override fun newEntity(id: Int?): Music {
        return MusicUtils.newMusic(id)
    }

    override fun newDomain(id: Int?): cz.vhromada.catalog.domain.Music {
        return MusicUtils.newMusicDomain(id)
    }

    override fun anyDomain(): cz.vhromada.catalog.domain.Music {
        return any()
    }

    override fun anyEntity(): Music {
        return any()
    }

}
