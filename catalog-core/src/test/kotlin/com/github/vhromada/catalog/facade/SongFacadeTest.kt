package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Music
import com.github.vhromada.catalog.entity.Song
import com.github.vhromada.catalog.facade.impl.SongFacadeImpl
import com.github.vhromada.catalog.utils.MusicUtils
import com.github.vhromada.catalog.utils.SongUtils
import com.github.vhromada.catalog.utils.TestConstants
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Status
import com.github.vhromada.common.service.ChildService
import com.github.vhromada.common.service.ParentService
import com.github.vhromada.common.validator.Validator
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

/**
 * A class represents test for class [SongFacade].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(MockitoExtension::class)
class SongFacadeTest {

    /**
     * Instance of [ChildService] for songs
     */
    @Mock
    private lateinit var songService: ChildService<com.github.vhromada.catalog.domain.Song>

    /**
     * Instance of [ParentService] for music
     */
    @Mock
    private lateinit var musicService: ParentService<com.github.vhromada.catalog.domain.Music>

    /**
     * Instance of [Mapper] for songs
     */
    @Mock
    private lateinit var mapper: Mapper<Song, com.github.vhromada.catalog.domain.Song>

    /**
     * Instance of [Validator] for songs
     */
    @Mock
    private lateinit var songValidator: Validator<Song, com.github.vhromada.catalog.domain.Song>

    /**
     * Instance of [Validator] for music
     */
    @Mock
    private lateinit var musicValidator: Validator<Music, com.github.vhromada.catalog.domain.Music>

    /**
     * Instance of [SongFacade]
     */
    private lateinit var facade: SongFacade

    /**
     * Initializes facade.
     */
    @BeforeEach
    fun setUp() {
        facade = SongFacadeImpl(songService = songService, musicService = musicService, mapper = mapper, songValidator = songValidator, musicValidator = musicValidator)
    }

    /**
     * Test method for [SongFacade.get] with existing song.
     */
    @Test
    fun getExistingSong() {
        val entity = SongUtils.newSong(id = 1)
        val domain = SongUtils.newSongDomain(id = 1)

        whenever(songService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(mapper.mapBack(source = any<com.github.vhromada.catalog.domain.Song>())).thenReturn(entity)
        whenever(songValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.get(id = entity.id!!)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(entity)
            it.assertThat(result.events()).isEmpty()
        }

        verify(songService).get(id = entity.id!!)
        verify(mapper).mapBack(source = domain)
        verify(songValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(songService, mapper, songValidator)
        verifyZeroInteractions(musicService, musicValidator)
    }

    /**
     * Test method for [SongFacade.get] with not existing song.
     */
    @Test
    fun getNotExistingSong() {
        whenever(songService.get(id = any())).thenReturn(Optional.empty())
        whenever(songValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.get(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(songService).get(id = Int.MAX_VALUE)
        verify(songValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(songService, songValidator)
        verifyZeroInteractions(musicService, mapper, musicValidator)
    }

    /**
     * Test method for [SongFacade.update].
     */
    @Test
    fun update() {
        val entity = SongUtils.newSong(id = 1)
        val domain = SongUtils.newSongDomain(id = 1)

        whenever(songService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(mapper.map(source = any<Song>())).thenReturn(domain)
        whenever(songValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(songValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(songService).get(id = entity.id!!)
        verify(songService).update(data = domain)
        verify(mapper).map(source = entity)
        verify(songValidator).validate(data = entity, update = true)
        verify(songValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(songService, mapper, songValidator)
        verifyZeroInteractions(musicService, musicValidator)
    }

    /**
     * Test method for [SongFacade.update] with invalid song.
     */
    @Test
    fun updateInvalidSong() {
        val entity = SongUtils.newSong(id = Int.MAX_VALUE)

        whenever(songValidator.validate(data = any(), update = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(songValidator).validate(data = entity, update = true)
        verifyNoMoreInteractions(songValidator)
        verifyZeroInteractions(songService, musicService, mapper, musicValidator)
    }

    /**
     * Test method for [SongFacade.update] with not existing song.
     */
    @Test
    fun updateNotExistingSong() {
        val entity = SongUtils.newSong(id = Int.MAX_VALUE)

        whenever(songService.get(id = any())).thenReturn(Optional.empty())
        whenever(songValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(songValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(songService).get(id = entity.id!!)
        verify(songValidator).validate(data = entity, update = true)
        verify(songValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(songService, songValidator)
        verifyZeroInteractions(musicService, mapper, musicValidator)
    }

    /**
     * Test method for [SongFacade.remove].
     */
    @Test
    fun remove() {
        val domain = SongUtils.newSongDomain(id = 1)

        whenever(songService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(songValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.remove(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(songService).get(id = 1)
        verify(songService).remove(data = domain)
        verify(songValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(songService, songValidator)
        verifyZeroInteractions(musicService, mapper, musicValidator)
    }

    /**
     * Test method for [SongFacade.remove] with invalid song.
     */
    @Test
    fun removeInvalidSong() {
        whenever(songService.get(id = any())).thenReturn(Optional.empty())
        whenever(songValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.remove(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(songService).get(id = Int.MAX_VALUE)
        verify(songValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(songService, songValidator)
        verifyZeroInteractions(musicService, mapper, musicValidator)
    }

    /**
     * Test method for [SongFacade.duplicate].
     */
    @Test
    fun duplicate() {
        val domain = SongUtils.newSongDomain(id = 1)

        whenever(songService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(songValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.duplicate(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(songService).get(id = 1)
        verify(songService).duplicate(data = domain)
        verify(songValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(songService, songValidator)
        verifyZeroInteractions(musicService, mapper, musicValidator)
    }

    /**
     * Test method for [SongFacade.duplicate] with invalid song.
     */
    @Test
    fun duplicateInvalidSong() {
        whenever(songService.get(id = any())).thenReturn(Optional.empty())
        whenever(songValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.duplicate(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(songService).get(id = Int.MAX_VALUE)
        verify(songValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(songService, songValidator)
        verifyZeroInteractions(musicService, mapper, musicValidator)
    }

    /**
     * Test method for [SongFacade.moveUp].
     */
    @Test
    fun moveUp() {
        val domain = SongUtils.newSongDomainWithMusic(id = 1)
        val songs = listOf(domain, SongUtils.newSongDomain(id = 2))

        whenever(songService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(songService.find(parent = any())).thenReturn(songs)
        whenever(songValidator.validateExists(data = any())).thenReturn(Result())
        whenever(songValidator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(Result())

        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(songService).get(id = 1)
        verify(songService).find(parent = domain.music!!.id!!)
        verify(songService).moveUp(data = domain)
        verify(songValidator).validateExists(data = Optional.of(domain))
        verify(songValidator).validateMovingData(data = domain, list = songs, up = true)
        verifyNoMoreInteractions(songService, songValidator)
        verifyZeroInteractions(musicService, mapper, musicValidator)
    }

    /**
     * Test method for [SongFacade.moveUp] not existing song.
     */
    @Test
    fun moveUpNotExistingSong() {
        val domain = SongUtils.newSongDomainWithMusic(id = 1)

        whenever(songService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(songValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(songService).get(id = 1)
        verify(songValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(songService, songValidator)
        verifyZeroInteractions(musicService, mapper, musicValidator)
    }

    /**
     * Test method for [SongFacade.moveUp] with not movable song.
     */
    @Test
    fun moveUpNotMovableSong() {
        val domain = SongUtils.newSongDomainWithMusic(id = 1)
        val songs = listOf(domain, SongUtils.newSongDomain(id = 2))

        whenever(songService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(songService.find(parent = any())).thenReturn(songs)
        whenever(songValidator.validateExists(data = any())).thenReturn(Result())
        whenever(songValidator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(songService).get(id = 1)
        verify(songService).find(parent = domain.music!!.id!!)
        verify(songValidator).validateExists(data = Optional.of(domain))
        verify(songValidator).validateMovingData(data = domain, list = songs, up = true)
        verifyNoMoreInteractions(songService, songValidator)
        verifyZeroInteractions(musicService, mapper, musicValidator)
    }

    /**
     * Test method for [SongFacade.moveDown].
     */
    @Test
    fun moveDown() {
        val domain = SongUtils.newSongDomainWithMusic(id = 1)
        val songs = listOf(domain, SongUtils.newSongDomain(id = 2))

        whenever(songService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(songService.find(parent = any())).thenReturn(songs)
        whenever(songValidator.validateExists(data = any())).thenReturn(Result())
        whenever(songValidator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(Result())

        val result = facade.moveDown(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(songService).get(id = 1)
        verify(songService).find(parent = domain.music!!.id!!)
        verify(songService).moveDown(data = domain)
        verify(songValidator).validateExists(data = Optional.of(domain))
        verify(songValidator).validateMovingData(data = domain, list = songs, up = false)
        verifyNoMoreInteractions(songService, songValidator)
        verifyZeroInteractions(musicService, mapper, musicValidator)
    }

    /**
     * Test method for [SongFacade.moveDown] with not existing song.
     */
    @Test
    fun moveDownNotExistingSong() {
        val domain = SongUtils.newSongDomainWithMusic(id = 1)

        whenever(songService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(songValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveDown(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(songService).get(id = 1)
        verify(songValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(songService, songValidator)
        verifyZeroInteractions(musicService, mapper, musicValidator)
    }

    /**
     * Test method for [SongFacade.moveDown] with not movable song.
     */
    @Test
    fun moveDownNotMovableSong() {
        val domain = SongUtils.newSongDomainWithMusic(id = 1)
        val songs = listOf(domain, SongUtils.newSongDomain(id = 2))

        whenever(songService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(songService.find(parent = any())).thenReturn(songs)
        whenever(songValidator.validateExists(data = any())).thenReturn(Result())
        whenever(songValidator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveDown(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(songService).get(id = 1)
        verify(songService).find(parent = domain.music!!.id!!)
        verify(songValidator).validateExists(data = Optional.of(domain))
        verify(songValidator).validateMovingData(data = domain, list = songs, up = false)
        verifyNoMoreInteractions(songService, songValidator)
        verifyZeroInteractions(musicService, mapper, musicValidator)
    }

    /**
     * Test method for [SongFacade.add].
     */
    @Test
    fun add() {
        val entity = SongUtils.newSong(id = 1)
        val domain = SongUtils.newSongDomain(id = 1)
        val music = MusicUtils.newMusicDomain(id = 2)

        whenever(musicService.get(id = any())).thenReturn(Optional.of(music))
        whenever(mapper.map(source = any<Song>())).thenReturn(domain)
        whenever(songValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(musicValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.add(parent = 2, data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(songService).add(data = domain)
        verify(musicService).get(id = 2)
        verify(mapper).map(source = entity)
        verify(songValidator).validate(data = entity, update = false)
        verify(musicValidator).validateExists(data = Optional.of(music))
        verifyNoMoreInteractions(songService, musicService, mapper, songValidator, musicValidator)
    }

    /**
     * Test method for [SongFacade.add] with invalid music.
     */
    @Test
    fun addInvalidMusic() {
        val entity = SongUtils.newSong(id = 1)

        whenever(musicService.get(id = any())).thenReturn(Optional.empty())
        whenever(songValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(musicValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.add(parent = 2, data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(musicService).get(id = 2)
        verify(songValidator).validate(data = entity, update = false)
        verify(musicValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(musicService, songValidator, musicValidator)
        verifyZeroInteractions(songService, mapper)
    }

    /**
     * Test method for [SongFacade.add] with invalid song.
     */
    @Test
    fun addInvalidSong() {
        val entity = SongUtils.newSong(id = Int.MAX_VALUE)
        val music = MusicUtils.newMusicDomain(id = 2)

        whenever(musicService.get(id = any())).thenReturn(Optional.of(music))
        whenever(songValidator.validate(data = any(), update = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)
        whenever(musicValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.add(parent = 2, data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(musicService).get(id = 2)
        verify(songValidator).validate(data = entity, update = false)
        verify(musicValidator).validateExists(data = Optional.of(music))
        verifyNoMoreInteractions(musicService, songValidator, musicValidator)
        verifyZeroInteractions(songService, mapper)
    }

    /**
     * Test method for [SongFacade.find].
     */
    @Test
    fun find() {
        val entityList = listOf(SongUtils.newSong(id = 1), SongUtils.newSong(id = 2))
        val domainList = listOf(SongUtils.newSongDomain(id = 1), SongUtils.newSongDomain(id = 2))
        val music = MusicUtils.newMusicDomain(id = 2)

        whenever(songService.find(parent = any())).thenReturn(domainList)
        whenever(musicService.get(id = any())).thenReturn(Optional.of(music))
        whenever(mapper.mapBack(source = any<List<com.github.vhromada.catalog.domain.Song>>())).thenReturn(entityList)
        whenever(musicValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.find(parent = 2)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(entityList)
            it.assertThat(result.events()).isEmpty()
        }

        verify(songService).find(parent = 2)
        verify(musicService).get(id = 2)
        verify(mapper).mapBack(source = domainList)
        verify(musicValidator).validateExists(data = Optional.of(music))
        verifyNoMoreInteractions(songService, musicService, mapper, musicValidator)
        verifyZeroInteractions(songValidator)
    }

    /**
     * Test method for [SongFacade.find] with invalid music.
     */
    @Test
    fun findInvalidMusic() {
        whenever(musicService.get(id = any())).thenReturn(Optional.empty())
        whenever(musicValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.find(parent = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(musicService).get(id = Int.MAX_VALUE)
        verify(musicValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(musicService, musicValidator)
        verifyZeroInteractions(songService, mapper, songValidator)
    }

}
