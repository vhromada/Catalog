package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Music
import com.github.vhromada.catalog.entity.Song
import com.github.vhromada.catalog.facade.impl.SongFacadeImpl
import com.github.vhromada.catalog.utils.MusicUtils
import com.github.vhromada.catalog.utils.SongUtils
import com.github.vhromada.common.facade.MovableChildFacade
import com.github.vhromada.common.test.facade.MovableChildFacadeTest
import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor

/**
 * A class represents test for class [SongFacade].
 *
 * @author Vladimir Hromada
 */
class SongFacadeTest : MovableChildFacadeTest<Song, com.github.vhromada.catalog.domain.Song, Music, com.github.vhromada.catalog.domain.Music>() {

    override fun getFacade(): MovableChildFacade<Song, Music> {
        return SongFacadeImpl(service, accountProvider, timeProvider, mapper, parentMovableValidator, childMovableValidator)
    }

    override fun newParentEntity(id: Int): Music {
        return MusicUtils.newMusic(id)
    }

    override fun newParentDomain(id: Int): com.github.vhromada.catalog.domain.Music {
        return MusicUtils.newMusicWithSongs(id)
    }

    override fun newParentDomainWithChildren(id: Int, children: List<com.github.vhromada.catalog.domain.Song>): com.github.vhromada.catalog.domain.Music {
        return newParentDomain(id)
                .copy(songs = children)
    }

    override fun newChildEntity(id: Int?): Song {
        return SongUtils.newSong(id)
    }

    override fun newChildDomain(id: Int?): com.github.vhromada.catalog.domain.Song {
        return SongUtils.newSongDomain(id)
    }

    override fun getParentRemovedData(parent: com.github.vhromada.catalog.domain.Music, child: com.github.vhromada.catalog.domain.Song): com.github.vhromada.catalog.domain.Music {
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

    override fun anyChildDomain(): com.github.vhromada.catalog.domain.Song {
        return any()
    }

    override fun argumentCaptorParentDomain(): KArgumentCaptor<com.github.vhromada.catalog.domain.Music> {
        return argumentCaptor()
    }

    override fun assertParentDeepEquals(expected: com.github.vhromada.catalog.domain.Music, actual: com.github.vhromada.catalog.domain.Music) {
        MusicUtils.assertMusicDeepEquals(expected, actual)
    }

}
