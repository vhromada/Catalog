package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.utils.AuditUtils
import com.github.vhromada.catalog.utils.EpisodeUtils
import com.github.vhromada.catalog.utils.SeasonUtils
import com.github.vhromada.catalog.utils.ShowUtils
import com.github.vhromada.catalog.utils.TestConstants
import com.github.vhromada.catalog.utils.fillAudit
import com.github.vhromada.catalog.utils.updated
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * A class represents integration test for class [EpisodeRepository].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
@Transactional
@Rollback
class EpisodeRepositoryIntegrationTest {

    /**
     * Instance of [EntityManager]
     */
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [EpisodeRepository]
     */
    @Autowired
    private lateinit var repository: EpisodeRepository

    /**
     * Test method for get episode.
     */
    @Test
    fun getEpisode() {
        for (i in 1..EpisodeUtils.EPISODES_COUNT) {
            val episode = repository.findById(i).orElse(null)

            EpisodeUtils.assertEpisodeDeepEquals(expected = EpisodeUtils.getEpisodeDomain(index = i), actual = episode)
        }

        assertThat(repository.findById(Int.MAX_VALUE)).isNotPresent

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for add episode.
     */
    @Test
    @DirtiesContext
    fun add() {
        val episode = EpisodeUtils.newEpisodeDomain(id = null)
            .copy(position = EpisodeUtils.EPISODES_COUNT)
        episode.season = SeasonUtils.getSeason(entityManager = entityManager, id = 1)
        val expectedEpisode = EpisodeUtils.newEpisodeDomain(id = EpisodeUtils.EPISODES_COUNT + 1)
            .fillAudit(AuditUtils.newAudit())
        expectedEpisode.season = SeasonUtils.getSeason(entityManager = entityManager, id = 1)

        repository.save(episode)

        assertSoftly {
            it.assertThat(episode.id).isEqualTo(EpisodeUtils.EPISODES_COUNT + 1)
            it.assertThat(episode.createdUser).isEqualTo(TestConstants.ACCOUNT.uuid!!)
            it.assertThat(episode.createdTime).isEqualTo(TestConstants.TIME)
            it.assertThat(episode.updatedUser).isEqualTo(TestConstants.ACCOUNT.uuid!!)
            it.assertThat(episode.updatedTime).isEqualTo(TestConstants.TIME)
        }

        val addedEpisode = EpisodeUtils.getEpisode(entityManager = entityManager, id = EpisodeUtils.EPISODES_COUNT + 1)
        assertThat(addedEpisode).isNotNull
        EpisodeUtils.assertEpisodeDeepEquals(expected = expectedEpisode, actual = addedEpisode!!)

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT + 1)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for update episode.
     */
    @Test
    fun update() {
        val episode = EpisodeUtils.updateEpisode(entityManager = entityManager, id = 1)
        val expectedEpisode = EpisodeUtils.getEpisodeDomain(index = 1)
            .updated()
            .copy(position = EpisodeUtils.POSITION)
            .fillAudit(AuditUtils.updatedAudit())
        expectedEpisode.season = SeasonUtils.getSeason(entityManager = entityManager, id = 1)

        repository.saveAndFlush(episode)

        val updatedEpisode = EpisodeUtils.getEpisode(entityManager = entityManager, id = 1)
        assertThat(updatedEpisode).isNotNull
        EpisodeUtils.assertEpisodeDeepEquals(expectedEpisode, updatedEpisode!!)

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for get episodes by season.
     */
    @Test
    fun findAllBySeasonId() {
        for (i in 1..SeasonUtils.SEASONS_COUNT) {
            val episodes = repository.findAllBySeasonId(id = i)

            EpisodeUtils.assertDomainEpisodesDeepEquals(expected = EpisodeUtils.getEpisodes(season = i), actual = episodes)
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for get episodes for user by season.
     */
    @Test
    fun findAllBySeasonIdAndCreatedUser() {
        val user = AuditUtils.getAudit().createdUser!!
        for (i in 1..SeasonUtils.SEASONS_COUNT) {
            val episodes = repository.findAllBySeasonIdAndCreatedUser(id = i, user = user)

            EpisodeUtils.assertDomainEpisodesDeepEquals(expected = EpisodeUtils.getEpisodes(season = i), actual = episodes)
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for get episode by ID for user.
     */
    @Test
    fun findByIdAndCreatedUser() {
        val user = AuditUtils.getAudit().createdUser!!
        for (i in 1..EpisodeUtils.EPISODES_COUNT) {
            val episode = repository.findByIdAndCreatedUser(id = i, user = user).orElse(null)

            EpisodeUtils.assertEpisodeDeepEquals(expected = EpisodeUtils.getEpisodeDomain(index = i), actual = episode)
        }

        assertThat(repository.findByIdAndCreatedUser(id = Int.MAX_VALUE, user = user)).isNotPresent

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

}
