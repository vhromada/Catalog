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
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [MusicRepository]
     */
    @Autowired
    private lateinit var repository: MusicRepository

    /**
     * Test method for get list of music.
     */
    @Test
    fun getMusicList() {
        val musicList = repository.findAll()

        MusicUtils.assertDomainMusicDeepEquals(expected = MusicUtils.getMusicList(), actual = musicList)

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for get music.
     */
    @Test
    fun getMusic() {
        for (i in 1..MusicUtils.MUSIC_COUNT) {
            val music = repository.findById(i).orElse(null)

            MusicUtils.assertMusicDeepEquals(expected = MusicUtils.getMusicDomain(index = i), actual = music)
        }

        assertThat(repository.findById(Int.MAX_VALUE)).isNotPresent

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for add music.
     */
    @Test
    fun add() {
        val music = MusicUtils.newMusicDomain(id = null)
            .copy(position = MusicUtils.MUSIC_COUNT)
        val expectedMusic = MusicUtils.newMusicDomain(id = MusicUtils.MUSIC_COUNT + 1)
            .fillAudit(audit = AuditUtils.newAudit())

        repository.save(music)

        assertSoftly {
            it.assertThat(music.id).isEqualTo(MusicUtils.MUSIC_COUNT + 1)
            it.assertThat(music.createdUser).isEqualTo(TestConstants.ACCOUNT.uuid!!)
            it.assertThat(music.createdTime).isEqualTo(TestConstants.TIME)
            it.assertThat(music.updatedUser).isEqualTo(TestConstants.ACCOUNT.uuid!!)
            it.assertThat(music.updatedTime).isEqualTo(TestConstants.TIME)
        }

        val addedMusic = MusicUtils.getMusic(entityManager, MusicUtils.MUSIC_COUNT + 1)!!
        assertThat(addedMusic).isNotNull
        MusicUtils.assertMusicDeepEquals(expected = expectedMusic, actual = addedMusic)

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT + 1)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for update music.
     */
    @Test
    fun update() {
        val music = MusicUtils.updateMusic(entityManager = entityManager, id = 1)
        val expectedMusic = MusicUtils.getMusicDomain(index = 1)
            .updated()
            .copy(position = MusicUtils.POSITION)
            .fillAudit(audit = AuditUtils.updatedAudit())

        repository.saveAndFlush(music)

        val updatedMusic = MusicUtils.getMusic(entityManager = entityManager, id = 1)
        assertThat(updatedMusic).isNotNull
        MusicUtils.assertMusicDeepEquals(expected = expectedMusic, actual = updatedMusic!!)

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for remove music.
     */
    @Test
    fun remove() {
        repository.delete(MusicUtils.getMusic(entityManager = entityManager, id = 1)!!)

        assertThat(MusicUtils.getMusic(entityManager = entityManager, id = 1)).isNull()

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT - 1)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT - SongUtils.SONGS_PER_MUSIC_COUNT)
        }
    }

    /**
     * Test method for remove all music.
     */
    @Test
    fun removeAll() {
        repository.deleteAll()

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(0)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(0)
        }
    }

    /**
     * Test method for get list of music for user.
     */
    @Test
    fun findByCreatedUser() {
        val musicList = repository.findByCreatedUser(user = AuditUtils.getAudit().createdUser!!)

        MusicUtils.assertDomainMusicDeepEquals(expected = MusicUtils.getMusicList(), actual = musicList)

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

    /**
     * Test method for get music by id for user.
     */
    @Test
    fun findByIdAndCreatedUser() {
        val user = AuditUtils.getAudit().createdUser!!
        for (i in 1..MusicUtils.MUSIC_COUNT) {
            val author = repository.findByIdAndCreatedUser(id = i, user = user).orElse(null)

            MusicUtils.assertMusicDeepEquals(expected = MusicUtils.getMusicDomain(index = i), actual = author)
        }

        assertThat(repository.findByIdAndCreatedUser(id = Int.MAX_VALUE, user = user)).isNotPresent

        assertSoftly {
            it.assertThat(MusicUtils.getMusicCount(entityManager = entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT)
            it.assertThat(SongUtils.getSongsCount(entityManager = entityManager)).isEqualTo(SongUtils.SONGS_COUNT)
        }
    }

}
