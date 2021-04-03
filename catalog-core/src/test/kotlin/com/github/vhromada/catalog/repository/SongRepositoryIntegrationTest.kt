package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.utils.AuditUtils
import com.github.vhromada.catalog.utils.MusicUtils
import com.github.vhromada.catalog.utils.SongUtils
import com.github.vhromada.catalog.utils.TestConstants
import com.github.vhromada.catalog.utils.fillAudit
import com.github.vhromada.catalog.utils.updated
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * A class represents integration test for class [SongRepository].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
@Transactional
@Rollback
class SongRepositoryIntegrationTest {

    /**
     * Instance of [EntityManager]
     */
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [SongRepository]
     */
    @Autowired
    private lateinit var repository: SongRepository

    /**
     * Test method for get song.
     */
    @Test
    fun getSong() {
        for (i in 1..SongUtils.SONGS_COUNT) {
            val song = repository.findById(i).orElse(null)

            SongUtils.assertSongDeepEquals(expected = SongUtils.getSongDomain(index = i), actual = song)
        }

        assertThat(repository.findById(Int.MAX_VALUE)).isNotPresent

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for add song.
     */
    @Test
    fun add() {
        val song = SongUtils.newSongDomain(id = null)
            .copy(position = SongUtils.SONGS_COUNT)
        song.music = MusicUtils.getMusic(entityManager = entityManager, id = 1)
        val expectedSong = SongUtils.newSongDomain(id = SongUtils.SONGS_COUNT + 1)
            .fillAudit(AuditUtils.newAudit())
        expectedSong.music = MusicUtils.getMusic(entityManager = entityManager, id = 1)

        repository.save(song)

        assertSoftly {
            it.assertThat(song.id).isEqualTo(SongUtils.SONGS_COUNT + 1)
            it.assertThat(song.createdUser).isEqualTo(TestConstants.ACCOUNT.uuid!!)
            it.assertThat(song.createdTime).isEqualTo(TestConstants.TIME)
            it.assertThat(song.updatedUser).isEqualTo(TestConstants.ACCOUNT.uuid!!)
            it.assertThat(song.updatedTime).isEqualTo(TestConstants.TIME)
        }

        val addedSong = SongUtils.getSong(entityManager = entityManager, id = SongUtils.SONGS_COUNT + 1)
        assertThat(addedSong).isNotNull
        SongUtils.assertSongDeepEquals(expected = expectedSong, actual = addedSong!!)

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT + 1)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for update song.
     */
    @Test
    fun update() {
        val song = SongUtils.updateSong(entityManager = entityManager, id = 1)
        val expectedSong = SongUtils.getSongDomain(index = 1)
            .updated()
            .copy(position = SongUtils.POSITION)
            .fillAudit(AuditUtils.updatedAudit())
        expectedSong.music = MusicUtils.getMusic(entityManager = entityManager, id = 1)

        repository.saveAndFlush(song)

        val updatedSong = SongUtils.getSong(entityManager = entityManager, id = 1)
        assertThat(updatedSong).isNotNull
        SongUtils.assertSongDeepEquals(expectedSong, updatedSong!!)

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for get songs by music.
     */
    @Test
    fun findAllByMusicId() {
        for (i in 1..MusicUtils.MUSIC_COUNT) {
            val songs = repository.findAllByMusicId(id = i)

            SongUtils.assertDomainSongsDeepEquals(expected = SongUtils.getSongs(music = i), actual = songs)
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for get songs for user by music.
     */
    @Test
    fun findAllByMusicIdAndCreatedUser() {
        val user = AuditUtils.getAudit().createdUser!!
        for (i in 1..MusicUtils.MUSIC_COUNT) {
            val songs = repository.findAllByMusicIdAndCreatedUser(id = i, user = user)

            SongUtils.assertDomainSongsDeepEquals(expected = SongUtils.getSongs(music = i), actual = songs)
        }

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

    /**
     * Test method for get song by ID for user.
     */
    @Test
    fun findByIdAndCreatedUser() {
        val user = AuditUtils.getAudit().createdUser!!
        for (i in 1..SongUtils.SONGS_COUNT) {
            val song = repository.findByIdAndCreatedUser(id = i, user = user).orElse(null)

            SongUtils.assertSongDeepEquals(expected = SongUtils.getSongDomain(index = i), actual = song)
        }

        assertThat(repository.findByIdAndCreatedUser(id = Int.MAX_VALUE, user = user)).isNotPresent

        assertSoftly {
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
        }
    }

}
