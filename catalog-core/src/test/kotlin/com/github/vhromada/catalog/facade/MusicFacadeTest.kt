package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Music
import com.github.vhromada.catalog.facade.impl.MusicFacadeImpl
import com.github.vhromada.catalog.utils.MusicUtils
import com.github.vhromada.catalog.utils.TestConstants
import com.github.vhromada.common.entity.Time
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Status
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
 * A class represents test for class [MusicFacade].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(MockitoExtension::class)
class MusicFacadeTest {

    /**
     * Instance of [ParentService] for music
     */
    @Mock
    private lateinit var service: ParentService<com.github.vhromada.catalog.domain.Music>

    /**
     * Instance of [Mapper] for music
     */
    @Mock
    private lateinit var mapper: Mapper<Music, com.github.vhromada.catalog.domain.Music>

    /**
     * Instance of [Validator] for music
     */
    @Mock
    private lateinit var validator: Validator<Music, com.github.vhromada.catalog.domain.Music>

    /**
     * Instance of [MusicFacade]
     */
    private lateinit var facade: MusicFacade

    /**
     * Initializes facade.
     */
    @BeforeEach
    fun setUp() {
        facade = MusicFacadeImpl(musicService = service, mapper = mapper, musicValidator = validator)
    }

    /**
     * Test method for [MusicFacade.get] with existing music.
     */
    @Test
    fun getExistingMusic() {
        val entity = MusicUtils.newMusic(id = 1)
        val domain = MusicUtils.newMusicDomain(id = 1)

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(mapper.mapBack(source = any<com.github.vhromada.catalog.domain.Music>())).thenReturn(entity)
        whenever(validator.validateExists(data = any())).thenReturn(Result())

        val result = facade.get(id = entity.id!!)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(entity)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).get(id = entity.id!!)
        verify(mapper).mapBack(source = domain)
        verify(validator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(service, mapper, validator)
    }

    /**
     * Test method for [MusicFacade.get] with not existing music.
     */
    @Test
    fun getNotExistingMusic() {
        whenever(service.get(id = any())).thenReturn(Optional.empty())
        whenever(validator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.get(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(service).get(id = Int.MAX_VALUE)
        verify(validator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [MusicFacade.update].
     */
    @Test
    fun update() {
        val entity = MusicUtils.newMusic(id = 1)
        val domain = MusicUtils.newMusicDomain(id = 1)

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(mapper.map(source = any<Music>())).thenReturn(domain)
        whenever(validator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(validator.validateExists(data = any())).thenReturn(Result())

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).get(id = entity.id!!)
        verify(service).update(data = domain)
        verify(mapper).map(source = entity)
        verify(validator).validate(data = entity, update = true)
        verify(validator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(service, mapper, validator)
    }

    /**
     * Test method for [MusicFacade.update] with invalid music.
     */
    @Test
    fun updateInvalidMusic() {
        val entity = MusicUtils.newMusic(id = Int.MAX_VALUE)

        whenever(validator.validate(data = any(), update = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(validator).validate(data = entity, update = true)
        verifyNoMoreInteractions(validator)
        verifyZeroInteractions(service, mapper)
    }

    /**
     * Test method for [MusicFacade.update] with not existing music.
     */
    @Test
    fun updateNotExistingMusic() {
        val entity = MusicUtils.newMusic(id = Int.MAX_VALUE)

        whenever(service.get(id = any())).thenReturn(Optional.empty())
        whenever(validator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(validator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(service).get(id = entity.id!!)
        verify(validator).validate(data = entity, update = true)
        verify(validator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [MusicFacade.remove].
     */
    @Test
    fun remove() {
        val domain = MusicUtils.newMusicDomain(id = 1)

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(validator.validateExists(data = any())).thenReturn(Result())

        val result = facade.remove(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).get(id = 1)
        verify(service).remove(data = domain)
        verify(validator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [MusicFacade.remove] with invalid music.
     */
    @Test
    fun removeInvalidMusic() {
        whenever(service.get(id = any())).thenReturn(Optional.empty())
        whenever(validator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.remove(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(service).get(id = Int.MAX_VALUE)
        verify(validator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [MusicFacade.duplicate].
     */
    @Test
    fun duplicate() {
        val domain = MusicUtils.newMusicDomain(id = 1)

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(validator.validateExists(data = any())).thenReturn(Result())

        val result = facade.duplicate(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).get(id = 1)
        verify(service).duplicate(data = domain)
        verify(validator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [MusicFacade.duplicate] with invalid music.
     */
    @Test
    fun duplicateInvalidMusic() {
        whenever(service.get(id = any())).thenReturn(Optional.empty())
        whenever(validator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.duplicate(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(service).get(id = Int.MAX_VALUE)
        verify(validator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [MusicFacade.moveUp].
     */
    @Test
    fun moveUp() {
        val domain = MusicUtils.newMusicDomain(id = 1)
        val musicList = listOf(domain, MusicUtils.newMusicDomain(id = 2))

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(service.getAll()).thenReturn(musicList)
        whenever(validator.validateExists(data = any())).thenReturn(Result())
        whenever(validator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(Result())

        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).get(id = 1)
        verify(service).getAll()
        verify(service).moveUp(data = domain)
        verify(validator).validateExists(data = Optional.of(domain))
        verify(validator).validateMovingData(data = domain, list = musicList, up = true)
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [MusicFacade.moveUp] not existing music.
     */
    @Test
    fun moveUpNotExistingMusic() {
        val domain = MusicUtils.newMusicDomain(id = 1)

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(validator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(service).get(id = 1)
        verify(validator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [MusicFacade.moveUp] with not movable music.
     */
    @Test
    fun moveUpNotMovableMusic() {
        val domain = MusicUtils.newMusicDomain(id = 1)
        val musicList = listOf(domain, MusicUtils.newMusicDomain(id = 2))

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(service.getAll()).thenReturn(musicList)
        whenever(validator.validateExists(data = any())).thenReturn(Result())
        whenever(validator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(service).get(id = 1)
        verify(service).getAll()
        verify(validator).validateExists(data = Optional.of(domain))
        verify(validator).validateMovingData(data = domain, list = musicList, up = true)
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [MusicFacade.moveDown].
     */
    @Test
    fun moveDown() {
        val domain = MusicUtils.newMusicDomain(id = 1)
        val musicList = listOf(domain, MusicUtils.newMusicDomain(id = 2))

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(service.getAll()).thenReturn(musicList)
        whenever(validator.validateExists(data = any())).thenReturn(Result())
        whenever(validator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(Result())

        val result = facade.moveDown(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).get(id = 1)
        verify(service).getAll()
        verify(service).moveDown(data = domain)
        verify(validator).validateExists(data = Optional.of(domain))
        verify(validator).validateMovingData(data = domain, list = musicList, up = false)
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [MusicFacade.moveDown] with not existing music.
     */
    @Test
    fun moveDownNotExistingMusic() {
        val domain = MusicUtils.newMusicDomain(id = 1)

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(validator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveDown(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(service).get(id = 1)
        verify(validator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [MusicFacade.moveDown] with not movable music.
     */
    @Test
    fun moveDownNotMovableMusic() {
        val domain = MusicUtils.newMusicDomain(id = 1)
        val musicList = listOf(domain, MusicUtils.newMusicDomain(id = 2))

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(service.getAll()).thenReturn(musicList)
        whenever(validator.validateExists(data = any())).thenReturn(Result())
        whenever(validator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveDown(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(service).get(id = 1)
        verify(service).getAll()
        verify(validator).validateExists(data = Optional.of(domain))
        verify(validator).validateMovingData(data = domain, list = musicList, up = false)
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [MusicFacade.newData].
     */
    @Test
    fun newData() {
        val result = facade.newData()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).newData()
        verifyNoMoreInteractions(service)
        verifyZeroInteractions(mapper, validator)
    }

    /**
     * Test method for [MusicFacade.getAll].
     */
    @Test
    fun getAll() {
        val entityList = listOf(MusicUtils.newMusic(id = 1), MusicUtils.newMusic(id = 2))
        val domainList = listOf(MusicUtils.newMusicDomain(id = 1), MusicUtils.newMusicDomain(id = 2))

        whenever(service.getAll()).thenReturn(domainList)
        whenever(mapper.mapBack(source = any<List<com.github.vhromada.catalog.domain.Music>>())).thenReturn(entityList)

        val result = facade.getAll()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(entityList)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).getAll()
        verify(mapper).mapBack(source = domainList)
        verifyNoMoreInteractions(service, mapper)
        verifyZeroInteractions(validator)
    }

    /**
     * Test method for [MusicFacade.add].
     */
    @Test
    fun add() {
        val entity = MusicUtils.newMusic(id = 1)
        val domain = MusicUtils.newMusicDomain(id = 1)

        whenever(mapper.map(source = any<Music>())).thenReturn(domain)
        whenever(validator.validate(data = any(), update = any())).thenReturn(Result())

        val result = facade.add(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).add(data = domain)
        verify(mapper).map(source = entity)
        verify(validator).validate(data = entity, update = false)
        verifyNoMoreInteractions(service, mapper, validator)
    }

    /**
     * Test method for [MusicFacade.add] with invalid music.
     */
    @Test
    fun addInvalidMusic() {
        val entity = MusicUtils.newMusic(id = Int.MAX_VALUE)

        whenever(validator.validate(data = any(), update = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.add(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(validator).validate(data = entity, update = false)
        verifyNoMoreInteractions(validator)
        verifyZeroInteractions(service, mapper)
    }

    /**
     * Test method for [MusicFacade.updatePositions].
     */
    @Test
    fun updatePositions() {
        val result = facade.updatePositions()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).updatePositions()
        verifyNoMoreInteractions(service)
        verifyZeroInteractions(mapper, validator)
    }

    /**
     * Test method for [MusicFacade.getTotalMediaCount].
     */
    @Test
    fun getTotalMediaCount() {
        val music1 = MusicUtils.newMusicDomain(id = 1)
        val music2 = MusicUtils.newMusicDomain(id = 2)
        val expectedCount = music1.mediaCount + music2.mediaCount

        whenever(service.getAll()).thenReturn(listOf(music1, music2))

        val result = facade.getTotalMediaCount()

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
        val musicList = listOf(MusicUtils.newMusicDomainWithSongs(id = 1), MusicUtils.newMusicDomainWithSongs(id = 2))
        var totalLength = 0
        for (music in musicList) {
            for (song in music.songs) {
                totalLength += song.length
            }
        }
        val expectedTotalLength = totalLength

        whenever(service.getAll()).thenReturn(musicList)

        val result = facade.getTotalLength()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(Time(length = expectedTotalLength))
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
        val music1 = MusicUtils.newMusicDomainWithSongs(id = 1)
        val music2 = MusicUtils.newMusicDomainWithSongs(id = 2)
        val expectedSongs = music1.songs.size + music2.songs.size

        whenever(service.getAll()).thenReturn(listOf(music1, music2))

        val result = facade.getSongsCount()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(expectedSongs)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).getAll()
        verifyNoMoreInteractions(service)
        verifyZeroInteractions(mapper, validator)
    }

}
