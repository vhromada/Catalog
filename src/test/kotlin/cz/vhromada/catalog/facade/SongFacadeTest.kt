package cz.vhromada.catalog.facade

import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import cz.vhromada.catalog.entity.Music
import cz.vhromada.catalog.entity.Song
import cz.vhromada.catalog.facade.impl.SongFacadeImpl
import cz.vhromada.catalog.utils.MusicUtils
import cz.vhromada.catalog.utils.SongUtils
import cz.vhromada.common.facade.MovableChildFacade
import cz.vhromada.common.test.facade.MovableChildFacadeTest

/**
 * A class represents test for class [SongFacade].
 *
 * @author Vladimir Hromada
 */
class SongFacadeTest : MovableChildFacadeTest<Song, cz.vhromada.catalog.domain.Song, Music, cz.vhromada.catalog.domain.Music>() {

    override fun getFacade(): MovableChildFacade<Song, Music> {
        return SongFacadeImpl(service, accountProvider, timeProvider, mapper, parentMovableValidator, childMovableValidator)
    }

    override fun newParentEntity(id: Int): Music {
        return MusicUtils.newMusic(id)
    }

    override fun newParentDomain(id: Int): cz.vhromada.catalog.domain.Music {
        return MusicUtils.newMusicWithSongs(id)
    }

    override fun newParentDomainWithChildren(id: Int, children: List<cz.vhromada.catalog.domain.Song>): cz.vhromada.catalog.domain.Music {
        return newParentDomain(id)
                .copy(songs = children)
    }

    override fun newChildEntity(id: Int?): Song {
        return SongUtils.newSong(id)
    }

    override fun newChildDomain(id: Int?): cz.vhromada.catalog.domain.Song {
        return SongUtils.newSongDomain(id)
    }

    override fun getParentRemovedData(parent: cz.vhromada.catalog.domain.Music, child: cz.vhromada.catalog.domain.Song): cz.vhromada.catalog.domain.Music {
        val songs = parent.songs.toMutableList()
        songs.remove(child)
        return parent.copy(songs = songs)
    }

    override fun anyParentEntity(): Music {
        return any()
    }

    override fun anyChildEntity(): Song {
        return any()
    }

    override fun anyChildDomain(): cz.vhromada.catalog.domain.Song {
        return any()
    }

    override fun argumentCaptorParentDomain(): KArgumentCaptor<cz.vhromada.catalog.domain.Music> {
        return argumentCaptor()
    }

    override fun assertParentDeepEquals(expected: cz.vhromada.catalog.domain.Music, actual: cz.vhromada.catalog.domain.Music) {
        MusicUtils.assertMusicDeepEquals(expected, actual)
    }

}
