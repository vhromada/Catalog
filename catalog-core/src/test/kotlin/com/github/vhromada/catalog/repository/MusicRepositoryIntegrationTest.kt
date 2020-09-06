package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.utils.AuditUtils
import com.github.vhromada.catalog.utils.MusicUtils
import com.github.vhromada.catalog.utils.SongUtils
import com.github.vhromada.catalog.utils.updated
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Sort
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

/**
 * A class represents integration test for class [MusicRepository].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
@Transactional
@Rollback
class MusicRepositoryIntegrationTest {

    /**
     * Instance of [EntityManager]
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [MusicRepository]
     */
    @Autowired
    private lateinit var musicRepository: MusicRepository

    /**
     * Test method for get all music.
     */
    @Test
    fun getMusicAll() {
        val music = musicRepository.findAll(Sort.by("position", "id"))

        MusicUtils.assertMusicDeepEquals(MusicUtils.getMusic(), music)

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for get one music.
     */
    @Test
    @Suppress("UsePropertyAccessSyntax")
    fun getMusicOne() {
        for (i in 1..MusicUtils.MUSIC_COUNT) {
            val music = musicRepository.findById(i).orElse(null)

            MusicUtils.assertMusicDeepEquals(MusicUtils.getMusic(i), music)
        }

        assertThat(musicRepository.findById(Int.MAX_VALUE).isPresent).isFalse()

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for add music.
     */
    @Test
    fun add() {
        val audit = AuditUtils.getAudit()
        val music = MusicUtils.newMusicDomain(null)
                .copy(position = MusicUtils.MUSIC_COUNT, audit = audit)

        musicRepository.save(music)

        assertThat(music.id).isEqualTo(MusicUtils.MUSIC_COUNT + 1)

        val addedMusic = MusicUtils.getMusic(entityManager, MusicUtils.MUSIC_COUNT + 1)!!
        val expectedAddMusic = MusicUtils.newMusicDomain(null)
                .copy(id = MusicUtils.MUSIC_COUNT + 1, position = MusicUtils.MUSIC_COUNT, audit = audit)
        MusicUtils.assertMusicDeepEquals(expectedAddMusic, addedMusic)

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT + 1)
            it.assertThat(SongUtils.getSongsCount(entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for update music with updated data.
     */
    @Test
    fun updateData() {
        val music = MusicUtils.updateMusic(entityManager, 1)

        musicRepository.save(music)

        val updatedMusic = MusicUtils.getMusic(entityManager, 1)!!
        val expectedUpdatedMusic = MusicUtils.getMusic(1)
                .updated()
                .copy(position = MusicUtils.POSITION)
        MusicUtils.assertMusicDeepEquals(expectedUpdatedMusic, updatedMusic)

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for update music with added song.
     */
    @Test
    fun updateAddedSong() {
        val audit = AuditUtils.getAudit()
        val song = SongUtils.newSongDomain(null)
                .copy(position = SongUtils.SONGS_COUNT, audit = audit)
        entityManager.persist(song)

        var music = MusicUtils.getMusic(entityManager, 1)!!
        val songs = music.songs.toMutableList()
        songs.add(song)
        music = music.copy(songs = songs)

        musicRepository.save(music)

        val updatedMusic = MusicUtils.getMusic(entityManager, 1)!!
        val expectedSong = SongUtils.newSongDomain(null)
                .copy(id = SongUtils.SONGS_COUNT + 1, position = SongUtils.SONGS_COUNT, audit = audit)
        var expectedUpdatedMusic = MusicUtils.getMusic(1)
        val expectedSongs = expectedUpdatedMusic.songs.toMutableList()
        expectedSongs.add(expectedSong)
        expectedUpdatedMusic = expectedUpdatedMusic.copy(songs = expectedSongs)
        MusicUtils.assertMusicDeepEquals(expectedUpdatedMusic, updatedMusic)

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager)).isEqualTo(SongUtils.SONGS_COUNT + 1)
        }
    }

    /**
     * Test method for remove music.
     */
    @Test
    fun remove() {
        val songsCount = MusicUtils.getMusic(1).songs.size

        musicRepository.delete(MusicUtils.getMusic(entityManager, 1)!!)

        assertThat(MusicUtils.getMusic(entityManager, 1)).isNull()

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT - 1)
            it.assertThat(SongUtils.getSongsCount(entityManager)).isEqualTo(SongUtils.SONGS_COUNT - songsCount)
        }
    }

    /**
     * Test method for remove all music.
     */
    @Test
    fun removeAll() {
        musicRepository.deleteAll()

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager)).isEqualTo(0)
            it.assertThat(SongUtils.getSongsCount(entityManager)).isEqualTo(0)
        }
    }

    /**
     * Test method for get music for user.
     */
    @Test
    fun findByAuditCreatedUser() {
        val music = musicRepository.findByAuditCreatedUser(AuditUtils.getAudit().createdUser)

        MusicUtils.assertMusicDeepEquals(MusicUtils.getMusic(), music)

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

}
